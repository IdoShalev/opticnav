package opticnav.ardd.ard;

import java.io.IOException;

import opticnav.ardd.protocol.HexCode;

public interface ARDConnection extends AutoCloseable {
    public interface RequestPassConfCodesCallback {
        public void passConfCodes(HexCode passCode, HexCode confCode);
    }
    
    public int requestPassConfCodes(RequestPassConfCodesCallback c)
            throws ARDConnectionException;
    public void close() throws IOException;
}
