package opticnav.ardd.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;

import org.junit.Before;
import org.junit.Test;

public class PrimitiveReaderWriterTests {
    private PrimitiveReader in;
    private PrimitiveWriter out;
    
    @Before
    public void startUp() throws IOException {
        PipedInputStream pipedInput = new PipedInputStream();
        PipedOutputStream pipedOutput = new PipedOutputStream(pipedInput);
        
        in = new PrimitiveReader(pipedInput);
        out = new PrimitiveWriter(pipedOutput);
    }
    
    @Test
    public void writeTest() throws IOException {
        // Just in case there are doubts about the piped streams
        ByteArrayOutputStream bout = new ByteArrayOutputStream(4);
        PrimitiveWriter w = new PrimitiveWriter(bout);
        w.writeUInt8(100);
        w.flush();
        
        assertEquals(bout.toByteArray()[0], 100);
    }
    
    @Test
    public void readWriteTest() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream(4);
        PrimitiveWriter w = new PrimitiveWriter(bout);
        w.writeUInt8(100);
        w.flush();
        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        PrimitiveReader r = new PrimitiveReader(bin);
        assertEquals(100, r.readUInt8());
    }

    @Test
    public void string() throws IOException {
        out.writeString("");
        assertEquals("", in.readString());
        
        out.writeString("Hello");
        assertEquals("Hello", in.readString());
    }
    
    @Test
    public void uint8() throws IOException {
        for (int i = 0; i <= 0xFF; i++) {
            out.writeUInt8(i);
            assertEquals(i, in.readUInt8());
        }
    }
    
    @Test
    public void uint16() throws IOException {
        for (int i = 0; i <= 0xFFFF; i++) {
            out.writeUInt16(i);
            assertEquals(i, in.readUInt16());
        }
    }
    
    @Test
    public void uint31() throws IOException {
        for (long i = 0; i <= 0x7FFFFFFFL; i += 0x27F) {
            int j = (int)i;
            out.writeUInt31(j);
            assertEquals(j, in.readUInt31());
        }
    }
    
    @Test
    public void sint32() throws IOException {
        out.writeSInt32(0x7FFFFFFF);
        assertEquals(0x7FFFFFFF, in.readSInt32());
        
        out.writeSInt32(-0x80000000);
        assertEquals(-0x80000000, in.readSInt32());
        
        out.writeSInt32(-1);
        assertEquals(-1, in.readSInt32());
    }
    
    @Test
    public void blob() throws IOException {
        byte[] buf = {1, 2, 44, 123, (byte)255, 55};
        byte[] buf_clone = buf.clone();
        
        out.writeBlob(buf);
        assertArrayEquals(buf_clone, in.readBlob(6));
    }
    
    @Test
    public void fixedBlob() throws IOException {
        byte[] buf = {1, 2, 44, 123, (byte)255, 55};
        byte[] buf_clone = buf.clone();
        
        out.writeFixedBlob(buf);
        assertArrayEquals(buf_clone, in.readFixedBlob(buf.length));
    }
}
