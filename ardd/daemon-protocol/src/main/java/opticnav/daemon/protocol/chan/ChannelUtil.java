package opticnav.daemon.protocol.chan;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ChannelUtil {
    /**
     * This will catch any SocketExceptions and treat it as an end-of-stream.
     * If there's a better way, that'd be preferred...
     */
    private static class SocketInputStream extends InputStream {
        private InputStream input;

        public SocketInputStream(InputStream input) {
            this.input = input;
        }

        @Override
        public int available() throws IOException {
            return input.available();
        }

        @Override
        public void close() throws IOException {
            input.close();
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
            int ret;
            try {
                ret = input.read();
            } catch (SocketException e) {
                ret = -1;
            }
            return ret;
        }

        @Override
        public int read(byte[] arg0, int arg1, int arg2) throws IOException {
            int ret;
            try {
                ret = input.read(arg0, arg1, arg2);
            } catch (SocketException e) {
                ret = -1;
            }
            return ret;
        }

        @Override
        public int read(byte[] b) throws IOException {
            int ret;
            try {
                ret = input.read(b);
            } catch (SocketException e) {
                ret = -1;
            }
            return ret;
        }

        @Override
        public synchronized void reset() throws IOException {
            input.reset();
        }

        @Override
        public long skip(long arg0) throws IOException {
            return input.skip(arg0);
        }

    }
    
    public static Channel fromSocket(Socket sock) throws IOException {
        InputStream socket_in;
        OutputStream buffered_out;
        
        socket_in = new SocketInputStream(sock.getInputStream());
        buffered_out = new BufferedOutputStream(sock.getOutputStream());
        sock.setKeepAlive(true);
        
        return new Channel(socket_in, buffered_out);
    }
}
