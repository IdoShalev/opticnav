package opticnav.ardroid.connection;

import org.apache.commons.io.IOUtils;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

/**
 * This class adds cancellation mechanisms on top of java.util.Socket.
 * Unfortunately, the Java API doesn't have a real way to cancel Socket.connect()
 * (eg. if the user gets impatient and wants to abort).
 *
 */
public final class CancellableSocket {
    public interface Cancellable {
        /**
         * Try to cancel the connection.
         *
         * @return Did the socket connection get cancelled? True if it did, false if socket connected successfully.
         */
        public boolean cancel();
    }

    /**
     * Callbacks for when a connection event occurs. For any single connection attempt,
     * only ONE of these methods will be invoked.
     */
    public interface ConnectionEvent {
        public void cancelled();
        public void error(IOException e);
        public void connected(Socket socket);
    }

    private static class CancellableImpl implements Cancellable {
        private final Closeable c;
        private final ConnectionEvent event;
        private boolean cancelled;
        private boolean finished;

        public CancellableImpl(Closeable c, ConnectionEvent event) {
            this.c = c;
            this.event = event;
            this.cancelled = false;
            this.finished = false;
        }

        @Override
        public synchronized boolean cancel() {
            if (!finished) {
                cancelled = true;
                IOUtils.closeQuietly(c);
                event.cancelled();
                return true;
            } else {
                return false;
            }
        }

        public synchronized boolean setFinished() {
            this.finished = true;
            return !this.cancelled;
        }

        public synchronized boolean isCancelled() {
            return this.cancelled;
        }
    }

    /**
     * Non-blocking socket connection. When connection succeeds or fails, one of the callbacks in the ConnectionEvent
     * object is called. The program can cancel the connection using the returned Cancellable object.
     *
     * @param addr The address and port of the remote host to connect to
     * @param timeout The timeout value in milliseconds, or 0 for an infinite timeout
     * @param event The callback events to call on success or failure
     * @return The Cancellable object used to cancel a connection
     */
    public static Cancellable connect(final SocketAddress addr, final int timeout, final ConnectionEvent event) {
        final Socket socket = new Socket();
        final CancellableImpl cancellable = new CancellableImpl(socket, event);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket.connect(addr, timeout);
                    // It is possible that cancel() is called at this exact point (before setFinished).
                    // In such a case, the socket will be closed by cancel() even though it connected.
                    if (cancellable.setFinished()) {
                        event.connected(socket);
                    }
                } catch (IOException e) {
                    if (!cancellable.isCancelled()) {
                        // Exception is unrelated to cancellation - report it
                        event.error(e);
                    }
                }
            }
        }).start();

        return cancellable;
    }
}
