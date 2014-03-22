package opticnav.ardroid.connection;

import android.content.Context;
import android.os.Handler;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

public final class AsyncConnectionEvent implements CancellableSocket.ConnectionEvent {
    private final CancellableSocket.ConnectionEvent event;
    public final Handler h = new Handler();

    public AsyncConnectionEvent(CancellableSocket.ConnectionEvent event) {
        this.event = event;
    }

    @Override
    public void cancelled() {
        h.post(new Runnable() {
            @Override
            public void run() {
                event.cancelled();
            }
        });
    }

    @Override
    public void error(final IOException e) {
        h.post(new Runnable() {
            @Override
            public void run() {
                event.error(e);
            }
        });
    }

    @Override
    public void connected(final Socket socket) {
        h.post(new Runnable() {
            @Override
            public void run() {
                event.connected(socket);
            }
        });
    }
}
