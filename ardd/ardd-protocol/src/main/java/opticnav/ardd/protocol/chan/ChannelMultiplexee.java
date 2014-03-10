package opticnav.ardd.protocol.chan;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.apache.commons.io.IOUtils;

public class ChannelMultiplexee {
    private Channel channel;
    private PipedInputStream input;
    private PipedOutputStream output_to_input;
    private SOutput output;
    
    public ChannelMultiplexee(Channel channel, int channelID, ChannelMultiplexer.Writer writer) {
        try {
            this.input = new PipedInputStream();
            this.output_to_input = new PipedOutputStream(this.input);
        } catch (IOException e) {
            throw new IllegalStateException("This should never happen", e);
        }
        this.channel = channel;
        this.output = new SOutput(writer, channelID);
    }
    
    public void read(int bytes) throws IOException {
        InputStream in = this.channel.getInputStream();
        
        // TODO - more efficient copy
        for (int i = 0; i < bytes; i++) {
            int b = in.read();
            this.output_to_input.write(b);
        }
        this.output_to_input.flush();
    }
    
    public void close() throws IOException {
        this.output_to_input.close();
    }
    
    public Channel getChannel() {
        return new Channel(this.input, this.output);
    }
}