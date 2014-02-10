package opticnav.ardd.net;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import opticnav.ardd.connections.AdminClientConnection;

public class AdminListener implements Runnable {
    private final class Spawner implements Listener.ConnectionSpawner {
        @Override
        public Runnable create(Closeable closeable, InputStream input,
                OutputStream output) {
            return new AdminClientConnection(closeable, input, output);
        }
    }

    private Listener listener;
    
    public AdminListener(int port) throws IOException {
        Logger logger = Logger.getLogger("AdminListener");
        this.listener = new Listener(port, new Spawner(), logger);
    }

    @Override
    public void run() {
        this.listener.run();
    }
}
