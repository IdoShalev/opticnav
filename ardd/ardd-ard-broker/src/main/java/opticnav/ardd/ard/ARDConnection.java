package opticnav.ardd.ard;

import java.io.IOException;

import opticnav.ardd.protocol.ConfCode;
import opticnav.ardd.protocol.PassCode;

public interface ARDConnection extends AutoCloseable {
    public interface RequestPassConfCodesCallback {
        public void passConfCodes(PassCode passCode, ConfCode confCode);
    }
    
    public int requestPassConfCodes(RequestPassConfCodesCallback c)
            throws ARDConnectionException;
    public void close() throws IOException;
}
