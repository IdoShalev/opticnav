package opticnav.web.arddbrokerpool;

import java.io.IOException;
import java.net.Socket;

import opticnav.ardd.admin.AdminConnection;
import opticnav.ardd.admin.AdminConnectionException;
import opticnav.ardd.broker.admin.AdminBroker;

public class ARDdAdminPool implements AutoCloseable {
    public static class BrokerNotAvailableException extends AdminConnectionException {
        private static final long serialVersionUID = 1L;
        
        public BrokerNotAvailableException(Throwable e) {
            super(e);
        }
    }

    private final String host;
    private final int port;
    
    public ARDdAdminPool(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    public AdminConnection getAdminBroker()
            throws BrokerNotAvailableException {
        try {
            Socket socket = new Socket(host, port);
            
            return AdminBroker.fromSocket(socket);
        } catch (IOException e) {
            throw new BrokerNotAvailableException(e);
        }
    }

    @Override
    public void close() throws Exception {
        // Do nothing for now
    }
}
