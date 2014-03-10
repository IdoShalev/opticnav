package opticnav.ardd.protocol.chan;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.io.IOUtils;

import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;

public class ChannelMultiplexer {
    public static final int CTL_DATA = 0;
    public static final int CTL_EOF = 1;
    
    private Channel channel;
    private Writer writer;
    private ChannelMap channelMap;
    
    private static class ChannelMap {
        private Map<Integer, ChannelMultiplexee> channelMap;
        
        public ChannelMap() {
            this.channelMap = new HashMap<>();
        }
        
        public synchronized boolean isEmpty() {
            return this.channelMap.isEmpty();
        }
        
        public synchronized ChannelMultiplexee put(int channelID,
                ChannelMultiplexee cm) {
            return this.channelMap.put(channelID, cm);
        }
        
        public synchronized ChannelMultiplexee get(int channelID) {
            return this.channelMap.get(channelID);
        }
        
        public synchronized ChannelMultiplexee remove(int channelID)
                throws IOException {
            ChannelMultiplexee cm = this.channelMap.remove(channelID);
            cm.close();
            return cm;
        }
    }
    
    public class Writer {
        private PrimitiveWriter output;
        
        protected Writer() {
            this.output = new PrimitiveWriter(channel.getOutputStream());
        }
        
        public synchronized void write(int channelID, byte[] b) throws IOException {
            output.writeUInt8(ChannelMultiplexer.CTL_DATA);
            output.writeUInt8(channelID);
            output.writeUInt31(b.length);
            output.writeFixedBlob(b);
            output.flush();
        }
        
        public synchronized void writeEOF(int channelID) throws IOException {
            output.writeUInt8(ChannelMultiplexer.CTL_EOF);
            output.writeUInt8(channelID);
            output.flush();
            channelMap.remove(channelID);
        }
    }
    
    public ChannelMultiplexer(Channel channel) {
        this.channel = channel;
        this.channelMap = new ChannelMap();
        this.writer = new Writer();
    }
    
    /**
     * @param channelID
     * @return A thread-safe Channel that is encapsulated by ChannelMultiplexer
     */
    public Channel createChannel(int channelID) {
        ChannelMultiplexee cm;
        cm = new ChannelMultiplexee(this.channel, channelID, this.writer);
        channelMap.put(channelID, cm);
        
        return cm.getChannel();
    }
    
    public Listener createListener() {
        return new Listener(this.channel, this.channelMap);
    }

    public static class Listener implements Callable<Void> {
        private PrimitiveReader input;
        private Channel channel;
        private ChannelMap channelMap;

        public Listener(Channel chan, ChannelMap channelMap) {
            this.input = new PrimitiveReader(chan.getInputStream());
            this.channel = chan;
            this.channelMap = channelMap;
        }
        
        @Override
        public Void call() throws IOException {
            try {
                runC();
            } catch (EOFException e) {
                // ignore - EOF is expected
            } finally {
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
                    
                    int bytes = input.readUInt31();
                    c.read(bytes);
                } else if (control == CTL_EOF) {
                    // receive an end-of-file control code
                    int channelID = input.readUInt8();
                    ChannelMultiplexee c = channelMap.remove(channelID);
                    if (c == null) { 
                        throw new IllegalStateException("Channel to EOF doesn't exist: " + channelID);
                    }
                    
                    c.close();
                } else {
                    throw new IllegalStateException("Invalid control code: " + control);
                }
            }
        }
    }
}