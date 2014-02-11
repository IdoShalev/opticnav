package opticnav.web.arddbrokerpool;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

import opticnav.ardd.admin.AdminConnection;
import opticnav.ardd.admin.AdminConnectionException;
import opticnav.ardd.broker.admin.AdminBroker;
import opticnav.ardd.protocol.BlockingInputStream;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;

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
            Socket sock = new Socket(host, port);
            
            BlockingInputStream blocking_in;
            BufferedOutputStream buffered_out;
            blocking_in = new BlockingInputStream(sock.getInputStream());
            buffered_out = new BufferedOutputStream(sock.getOutputStream());
            
            PrimitiveReader in;
            PrimitiveWriter out;
            in = new PrimitiveReader(blocking_in);
            out = new PrimitiveWriter(buffered_out);
            
            return new AdminBroker(sock, in, out);
        } catch (IOException e) {
            throw new BrokerNotAvailableException(e);
        }
    }

    @Override
    public void close() throws Exception {
        // Do nothing for now
    }
}
