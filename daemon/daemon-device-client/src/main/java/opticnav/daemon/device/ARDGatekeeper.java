package opticnav.daemon.device;

import java.io.Closeable;
import java.io.IOException;

import opticnav.daemon.protocol.ConfCode;
import opticnav.daemon.protocol.PassCode;

/**
 * An ARD broker connection in the Gatekeeper state.
 * 
 * @author Danny Spencer
 *
 */
public interface ARDGatekeeper extends Closeable {
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
            throws ARDGatekeeperException;

    public ARDConnectionStatus connect(PassCode passCode) throws ARDGatekeeperException;

    public void close() throws IOException;
}
