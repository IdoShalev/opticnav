package opticnav.daemon.protocol.chan;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Callable;

import org.apache.commons.io.IOUtils;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import opticnav.daemon.protocol.PrimitiveReader;
import opticnav.daemon.protocol.PrimitiveWriter;

/**
 * Channel multiplexing is designed to transport many communication channels
 * onto a single channel.
 * For example, many InputStreams/OutputStreams can be used on a single Socket.
 * This way, only one Socket is needed instead of many.
 * 
 * This is needed in order to address message blocking issues with Sockets.
 * If one end is blocking because it's waiting for a reply, then it also
 * means that end cannot receive any other messages that are non-dependent
 * on the one being waited on. This gets in the way of being able to cancel
 * commands or communicating concurrently.
 */
public class ChannelMultiplexer {
    private static final XLogger LOG = XLoggerFactory
            .getXLogger(ChannelMultiplexer.class);
    
    /** Maximum channel ID */
    public static final int MAX_CHANNEL_ID = 255;

    /**
     * A maximum data size must be declared to prevent starvation issues.
     * This value must be in the range 0..65535
     */
    private static final int DEFAULT_MAX_DATA_SIZE = 1024;
    /** Data control code */
    private static final int CTL_DATA = 0;
    /** End-of-file control code */
    private static final int CTL_EOF = 1;
    
    private final Channel channel;
    private final Writer writer;
    private final ChannelMap channelMap;
    private final int maxDataSize;
    
    class Writer {
        private OutputStream os;
        private PrimitiveWriter output;
        
        protected Writer() {
            this.os = channel.getOutputStream();
            this.output = new PrimitiveWriter(os);
        }
        
        public int getMaxDataSize() {
            return maxDataSize;
        }
        
        public void write(int channelID, byte[] b) throws IOException {
            int off = 0;
            int len = b.length;
            while (len > 0) {
                /* Only write in segments of MAX_DATA_SIZE.
                 * the monitor is locked/unlocked for every write in order to
                 * let other waiting threads write */
                synchronized (this) {
                    int write_len  = len > maxDataSize ? maxDataSize : len;
                    
                    output.writeUInt8(ChannelMultiplexer.CTL_DATA);
                    output.writeUInt8(channelID);
                    output.writeUInt16(write_len);
                    os.write(b, off, write_len);
                    output.flush();
                    
                    off += maxDataSize;
                    len -= write_len;
                }
            }
        }
        
        public synchronized void writeEOF(int channelID) throws IOException {
            output.writeUInt8(ChannelMultiplexer.CTL_EOF);
            output.writeUInt8(channelID);
            output.flush();
            channelMap.remove(channelID);
        }
    }
    
    public ChannelMultiplexer(Channel channel) {
        this(channel, DEFAULT_MAX_DATA_SIZE);
    }
    
    public ChannelMultiplexer(Channel channel, int maxDataSize) {
        this.channel = channel;
        this.channelMap = new ChannelMap();
        this.writer = new Writer();
        this.maxDataSize = maxDataSize;
    }
    
    /**
     * Creates a thread-safe Channel that is encapsulated by ChannelMultiplexer.
     * This method is meant to be called on both the client and server ends for
     * any given channel ID.
     * 
     * @param channelID A channel ID that's established by the implementing
     *                  protocol.
     * @return A new Channel object
     */
    public Channel createChannel(int channelID) {
        if (channelID < 0 || channelID > MAX_CHANNEL_ID) {
            throw new IllegalArgumentException("Channel ID outside of valid range");
        }
        
        ChannelMultiplexee cm;
        cm = new ChannelMultiplexee(channelID, this.writer);
        if (channelMap.put(channelID, cm) != null) {
            throw new IllegalArgumentException("Channel already exists");
        }
        
        LOG.debug("Channel created: " + channelID);
        return cm.getChannel();
    }
    
    public Listener createListener() {
        return new Listener(this.channel, this.channelMap, maxDataSize);
    }

    public static class Listener implements Callable<Void> {
        private final PrimitiveReader input;
        private final Channel channel;
        private final ChannelMap channelMap;
        private final int maxDataSize;

        public Listener(Channel chan, ChannelMap channelMap, int maxDataSize) {
            this.input = new PrimitiveReader(chan.getInputStream());
            this.channel = chan;
            this.channelMap = channelMap;
            this.maxDataSize = maxDataSize;
        }
        
        @Override
        public Void call() throws IOException {
            try {
                runC();
            } catch (EOFException e) {
                // ignore - EOF is expected
                LOG.debug("Multiplexer EOF");
            } catch (Exception e) {
                LOG.catching(e);
            } finally {
                LOG.debug("Multiplexer Channel finished");
                // All channels needs to be closed
                channelMap.removeAll();
                // Close the stream
                IOUtils.closeQuietly(this.channel.getOutputStream());
            }
            return null;
        }
        
        private void runC() throws IOException {
            while (!Thread.currentThread().isInterrupted() &&
                    !this.channelMap.isEmpty()) {
                int control = input.readUInt8();
                
                if (control == CTL_DATA) {
                    // receive data
                    int channelID;
                    
                    channelID = input.readUInt8();
                    ChannelMultiplexee c = channelMap.get(channelID);
                    if (c == null) { 
                        throw new IllegalStateException("Channel doesn't exist: " + channelID);
                    }
                    
                    int bytes = input.readUInt16();
                    if (bytes == 0) {
                        throw new IllegalStateException("Data cannot be 0 bytes");
                    } else if (bytes > maxDataSize) { 
                        throw new IllegalStateException("Data cannot exceed " + maxDataSize + " bytes: got " + bytes);
                    }
                    byte[] buf = input.readFixedBlob(bytes);
                    c.read(buf);
                } else if (control == CTL_EOF) {
                    // receive an end-of-file control code
                    int channelID = input.readUInt8();
                    LOG.debug("Receive EOF: " + channelID);
                    channelMap.remove(channelID);
                } else {
                    throw new IllegalStateException("Invalid control code: " + control);
                }
            }
            LOG.debug("Channel map empty, about to close Multiplexer...");
        }
    }
}