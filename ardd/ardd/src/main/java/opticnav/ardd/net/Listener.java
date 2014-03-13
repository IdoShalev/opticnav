package opticnav.ardd.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import opticnav.ardd.protocol.chan.Channel;
import opticnav.ardd.protocol.chan.ChannelUtil;

public final class Listener implements Runnable {
    public interface ConnectionSpawner {
        public Runnable create(Channel channel);
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
        
        IOUtils.closeQuietly(this.socket);
    }

    private void dispatchClient(Socket client)
            throws InterruptedException, IOException
    {
        Channel channel = ChannelUtil.fromSocket(client);
        Thread connThread = new Thread(this.cs.create(channel));
        connThread.start();
        
        this.logger.info("Client connection accepted: " + client.getInetAddress());
    }
}
