package opticnav.ardd.ard;

import java.io.IOException;

import opticnav.ardd.protocol.ConfCode;
import opticnav.ardd.protocol.PassCode;

public interface ARDConnection extends AutoCloseable {
    public interface Cancellation {
        public void cancel();
    }
    
    public interface RequestPassConfCodesCallback {
        public void confCode(ConfCode confCode, Cancellation cancellation);
        public void registered(PassCode passCode);
        public void couldNotRegister();
        public void cancelled();
    }
    
    /**
     * Blocks until a passcode+ARD id is received, or an error occurs
     */
    public void requestPassConfCodes(RequestPassConfCodesCallback c)
            throws ARDConnectionException;

    public ARDLobbyConnectionStatus connectToLobby(PassCode passCode) throws ARDConnectionException;

    public void close() throws IOException;
}
