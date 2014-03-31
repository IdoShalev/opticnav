package opticnav.web.arddbrokerpool;

import java.io.IOException;
import java.net.Socket;

import opticnav.ardd.admin.ARDdAdmin;
import opticnav.ardd.admin.ARDdAdminException;
import opticnav.ardd.broker.admin.ARDdAdminBroker;
import opticnav.ardd.protocol.chan.ChannelUtil;

public class ARDdAdminPool implements AutoCloseable {
    public static class BrokerNotAvailableException extends ARDdAdminException {
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
    
    public ARDdAdmin getAdminBroker()
            throws BrokerNotAvailableException {
        try {
            Socket socket = new Socket(host, port);
            
            return new ARDdAdminBroker(ChannelUtil.fromSocket(socket));
        } catch (IOException e) {
            throw new BrokerNotAvailableException(e);
        }
    }

    @Override
    public void close() throws Exception {
        // Do nothing for now
    }
}
