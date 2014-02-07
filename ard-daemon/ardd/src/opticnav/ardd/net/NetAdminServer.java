package opticnav.ardd.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.ServerSocket;

import opticnav.ardd.AdminClientConnection;

public class NetAdminServer implements Runnable {
    private ServerSocket socket;
    
    public NetAdminServer(int port) throws IOException {
        this.socket = new ServerSocket(port);
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Socket client = socket.accept();
                    dispatchClient(client);
                } catch (IOException e) {
                    // TODO - Log this somewhere
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            // Thread has been interrupted - let it fall through to the end
        }
        
        try { socket.close(); } catch (IOException e) {}
    }
    
    private void dispatchClient(Socket client)
            throws InterruptedException, IOException
    {
        InputStream input = client.getInputStream();
        OutputStream output = client.getOutputStream();
        AdminClientConnection conn = new AdminClientConnection(client, input, output);
        new Thread(conn).start();
    }
}
