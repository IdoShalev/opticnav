package testutil;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class InputStreamDiagnose extends InputStream {
    private InputStream input;
    private PrintStream out;

    public InputStreamDiagnose(InputStream input, PrintStream out) {
        this.input = input;
        this.out = out;
    }

    @Override
    public int available() throws IOException {
        return input.available();
    }

    @Override
    public void close() throws IOException {
        input.close();
        out.flush();
    }

    @Override
    public synchronized void mark(int readlimit) {
        input.mark(readlimit);
    }

    @Override
    public boolean markSupported() {
        return input.markSupported();
    }

    @Override
    public int read() throws IOException {
        int ret = input.read();
        if (ret > 0) {
            printByte(ret);
        }
        return ret;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int ret = input.read(b, off, len);
        if (ret > 0) {
            for (int i = off; i < off+ret; i++) {
                printByte(b[i]);
            }
        }
        return ret;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int ret = input.read(b);
        if (ret > 0) {
            for (int i = 0; i < ret; i++) {
                printByte(b[i]);
            }
        }
        return ret;
    }

    @Override
    public synchronized void reset() throws IOException {
        input.reset();
    }

    @Override
    public long skip(long n) throws IOException {
        return input.skip(n);
    }

    @Override
    public boolean equals(Object obj) {
        return input.equals(obj);
    }

    @Override
    public int hashCode() {
        return input.hashCode();
    }

    @Override
    public String toString() {
        return input.toString();
    }
    
    private void printByte(int b) {
        this.out.printf("%02X ", b);
    }
}
