package opticnav.ardd.net;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Listener implements Runnable {
    public interface ConnectionSpawner {
        public Runnable create(Closeable closeable, InputStream input, OutputStream output);
    }
    
    private ServerSocket socket;
    private ConnectionSpawner cs;
    private Logger logger;
    
    public Listener(int port, ConnectionSpawner cs, Logger logger) throws IOException {
        this.socket = new ServerSocket(port);
        this.logger = logger;
        this.cs = cs;

        logger.info("Listening on port " + port);
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Socket client = socket.accept();
                    dispatchClient(client);
                } catch (IOException e) {
                    this.logger.log(Level.SEVERE, "IO exception", e);
                }
            }
        } catch (InterruptedException e) {
            // Thread has been interrupted - let it fall through to the end
        }
        
        try { this.socket.close(); } catch (IOException e) {}
    }

    private void dispatchClient(Socket client)
            throws InterruptedException, IOException
    {
        InputStream input;
        OutputStream output;
        
        // Ensure the connection doesn't die
        client.setKeepAlive(true);
        // BlockingInputStream will not read incomplete buffers
        input = new BlockingInputStream(client.getInputStream());
        // BufferedOutputStream avoids sending small buffers over a network
        // (the payload should be as big as it makes sense)
        output = new BufferedOutputStream(client.getOutputStream());
        
        Thread connThread = new Thread(this.cs.create(client, input, output));
        connThread.start();
        
        this.logger.info("Client connection accepted: " + client.getInetAddress());
    }
}
