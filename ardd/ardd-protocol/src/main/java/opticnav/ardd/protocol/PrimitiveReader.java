package opticnav.ardd.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

/**
 * PrimitiveReader can read any data type from the ardd protocols.
 * 
 * All integers are little-endian.
 *
 */
public final class PrimitiveReader {
    private InputStream in;
    
    public PrimitiveReader(InputStream input) {
        this.in = input;
    }

    public byte[] readBlob(int max_length) throws IOException {
        int length = readSInt32();
        if (length <= 0 || length > max_length) {
            throw new IOException("Illegal length: " + length);
        }
        
        byte[] buf = new byte[length];
        this.in.read(buf);
        return buf;
    }
    
    public byte[] readFixedBlob(int length) throws IOException {
        if (length <= 0) {
            throw new IOException("Illegal length: " + length);
        }
        
        byte[] buf = new byte[length];
        this.in.read(buf);
        return buf;
    }
    
    public String readString() throws IOException {
        int length = readUInt16();
        
        byte[] buf = new byte[length];
        this.in.read(buf);
        return new String(buf, "UTF-8");
    }
    
    public int readUInt8() throws IOException {
        return (int)readUInt(1);
    }
    
    public int readUInt16() throws IOException {
        return (int)readUInt(2);
    }
    
    public int readUInt31() throws IOException {
        int value = (int)readUInt(4);
        if (value >= 0) {
            return value;
        } else {
            throw new IOException("Out of range: " + value);
        }
    }
    
    public int readSInt32() throws IOException {
        return (int)readUInt(4);
    }
    
    private long readUInt(int bytes) throws IOException {
        assert bytes > 0 && bytes < 8;
        
        byte[] buf = new byte[bytes];
        this.in.read(buf);
        
        long value = (int)buf[0] & 0xFF;
        
        for (int i = 1; i < bytes; i++) {
            value |= ((int)buf[i] & 0xFF) << (i*8);
        }
        
        return value;
    }

    public void readFixedBlobToOutputStream(int length, OutputStream output) throws IOException {
        long writeLength = IOUtils.copyLarge(this.in, output, 0, length);
        if (writeLength != length) {
            throw new IOException("Expected to copy " + length + " bytes, copied  " + writeLength);
        }
    }
}
