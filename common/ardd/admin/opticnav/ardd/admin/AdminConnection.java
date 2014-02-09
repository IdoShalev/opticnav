package opticnav.ardd.admin;

import java.io.IOException;

import opticnav.ardd.protocol.HexCode;

public interface AdminConnection extends AutoCloseable {
    public int registerARDWithConfCode(HexCode confcode) throws AdminConnectionException;
    
    public void close() throws IOException;
}
