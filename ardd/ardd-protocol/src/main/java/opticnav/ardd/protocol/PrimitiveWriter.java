package opticnav.ardd.protocol;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

/**
 * PrimitiveWriter can write any data type from the ardd protocols.
 * 
 * All integers are little-endian.
 *
 */
public final class PrimitiveWriter implements Flushable, Closeable {
    private OutputStream out;
    
    public PrimitiveWriter(OutputStream output) {
        this.out = output;
    }
    
    public void writeBlob(byte[] buf) throws IOException {
        writeSInt32(buf.length);
        this.out.write(buf);
    }
    
    public void writeFixedBlob(byte[] buf) throws IOException {
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
    
    public void writeUInt31(int value) throws IOException {
        if (value >= 0) {
            writeUInt(value, 4);
        } else {
            throw new IOException("Out of range: " + value);
        }
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

    @Override
    public void close() throws IOException {
        this.out.close();
    }

    public void writeFixedBlobFromInputStream(int length, InputStream input) throws IOException {
        long writeLength = IOUtils.copyLarge(input, this.out, 0, length);
        if (length != writeLength) {
            throw new IOException("Expected to write " + length + " bytes, got " + writeLength);
        }
    }
}
