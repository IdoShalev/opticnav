package opticnav.ardd.protocol.chan;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.apache.commons.io.IOUtils;

/**
 * Represents a channel being multiplexed into a ChannelMultiplexer.
 */
class ChannelMultiplexee {
    private PipedInputStream input;
    private PipedOutputStream output_to_input;
    private SOutput output;
    
    public ChannelMultiplexee(int channelID, ChannelMultiplexer.Writer writer) {
        try {
            this.input = new PipedInputStream(writer.getMaxDataSize());
            this.output_to_input = new PipedOutputStream(this.input);
        } catch (IOException e) {
            throw new IllegalStateException("This should never happen", e);
        }
        this.output = new SOutput(writer, channelID);
    }
    
    public void read(byte[] b) throws IOException {
        this.output_to_input.write(b);
        this.output_to_input.flush();
    }
    
    public void close() throws IOException {
        this.output_to_input.close();
    }
    
    public Channel getChannel() {
        return new Channel(this.input, this.output);
    }
}