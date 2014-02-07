package opticnav.ardd.net;

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
        this.logger.info("Lol");
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
        InputStream input = client.getInputStream();
        OutputStream output = client.getOutputStream();
        Runnable conn = this.cs.create(client, input, output);
        this.logger.info("Client connection accepted: " + client.getInetAddress());
        new Thread(conn).start();
    }
}
