package opticnav.ardd.protocol.chan;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import opticnav.ardd.protocol.chan.ChannelMultiplexer.Writer;

final class SOutput extends OutputStream {
    private Writer mpWriter;
    private int channelID;

    public SOutput(ChannelMultiplexer.Writer mpWriter, int channelID) {
        this.mpWriter = mpWriter;
        this.channelID = channelID;
    }
    
    @Override
    public void close() throws IOException {
        mpWriter.writeEOF(channelID);
    }
    
    @Override
    public void write(int b) throws IOException {
        byte[] arr = {(byte)b};
        write(arr);
    }
    
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (off == 0 && len == b.length) {
            write(b);
        } else {
            byte[] arr = Arrays.copyOfRange(b, off, off+len);
            write(arr);
        }
    }
    
    @Override
    public void write(byte[] b) throws IOException {
        mpWriter.write(channelID, b);
    }
}