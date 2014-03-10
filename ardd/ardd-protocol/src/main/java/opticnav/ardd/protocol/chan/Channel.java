package opticnav.ardd.protocol.chan;

import java.io.OutputStream;
import java.io.InputStream;

import opticnav.ardd.protocol.BlockingInputStream;

public class Channel {
    private InputStream input;
    private OutputStream output;
    
    public Channel(InputStream input, OutputStream output) {
        this.input = new BlockingInputStream(input);
        this.output = output;
    }

    public InputStream getInputStream() {
        return input;
    }

    public OutputStream getOutputStream() {
        return output;
    }
}
