package opticnav.ardd.admin;

import java.io.IOException;

import opticnav.ardd.protocol.ConfCode;

public interface AdminConnection extends AutoCloseable {
    public int registerARDWithConfCode(ConfCode confcode) throws AdminConnectionException;
    
    public void close() throws IOException;
}
