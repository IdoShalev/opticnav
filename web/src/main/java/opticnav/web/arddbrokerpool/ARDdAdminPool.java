package opticnav.web.arddbrokerpool;

import java.io.IOException;
import java.net.Socket;

import opticnav.daemon.manager.ARDdAdmin;
import opticnav.daemon.manager.ARDdAdminBroker;
import opticnav.daemon.manager.ARDdAdminException;
import opticnav.daemon.protocol.chan.ChannelUtil;

/**
 * ARDdAdminPool serves {@link opticnav.daemon.manager.ARDdAdmin ARDdAdmin} objects to controllers that request them.
 * It is intended to keep a cache of pooled connections, but it currently just makes connections on demand.
 * 
 * @author Danny Spencer
 *
 */
public class ARDdAdminPool implements AutoCloseable {
    /**
     * Thrown if no connection to ardd could be made.
     * 
     * @author Danny Spencer
     *
     */
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
    
    /**
     * Get an ARDdAdmin connection
     * 
     * @return An ARDdAdmin connection
     * @throws BrokerNotAvailableException Thrown if no connection to ardd could be made.
     */
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
