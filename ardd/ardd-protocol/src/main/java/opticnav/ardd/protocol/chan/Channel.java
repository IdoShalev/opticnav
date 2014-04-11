package opticnav.ardd.protocol.chan;

import java.io.OutputStream;
import java.io.InputStream;

import opticnav.ardd.protocol.BlockingInputStream;

/**
 * A channel represents a two-way stream. It's simply a composite of InputStream and OutputStream.
 * 
 * @author Danny Spencer
 *
 */
public class Channel {
    private InputStream input;
    private OutputStream output;
    
    public Channel(InputStream input, OutputStream output) {
        this.input = new BlockingInputStream(input);
        this.output = output;
    }

    /**
     * Get the input stream of the channel
     * 
     * @return An input stream object
     */
    public InputStream getInputStream() {
        return input;
    }


    /**
     * Get the output stream of the channel
     * 
     * @return An output stream object
     */
    public OutputStream getOutputStream() {
        return output;
    }
}
