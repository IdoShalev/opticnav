package opticnav.ardd.net;

import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;

/**
 * PrimitiveWriter can write any data type from the ardd protocols.
 * 
 * All integers are little-endian.
 *
 */
public class PrimitiveWriter implements Flushable {
    private OutputStream out;
    
    public PrimitiveWriter(OutputStream output) {
        this.out = output;
    }
    
    public void writeBlob(byte[] buf) throws IOException {
        writeSInt32(buf.length);
        this.out.write(buf);
    }
    
    public void writeString(String s) throws IOException {
        byte[] buf = s.getBytes("UTF-8");
        writeUInt16(buf.length);
        this.out.write(buf);
    }
    
    public void writeUInt8(int value) throws IOException {
        this.out.write(value);
    }
    
    public void writeUInt16(int value) throws IOException {
        writeUInt(value, 2);
    }

    public void writeSInt32(int value) throws IOException {
        writeUInt(value, 4);
    }

    private void writeUInt(long value, int bytes) throws IOException {
        assert bytes < 8;
        
        byte[] buf = new byte[bytes];
        
        long temp_val = value;
        
        for (int i = 0; i < bytes; i++) {
            buf[i] = (byte)(temp_val & 0xFF);
            temp_val >>= 8;
        }

        this.out.write(buf);
    }

    @Override
    public void flush() throws IOException {
        this.out.flush();
    }
}
