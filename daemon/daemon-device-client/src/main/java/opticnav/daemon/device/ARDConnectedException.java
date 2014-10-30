package opticnav.daemon.device;

import java.io.IOException;

public class ARDConnectedException extends IOException {
    private static final long serialVersionUID = 1L;

    public ARDConnectedException(String message) {
        super(message);
    }
    
    public ARDConnectedException(Throwable cause) {
        super(cause);
    }

    public ARDConnectedException(String message, Throwable cause) {
        super(message, cause);
    }
}
