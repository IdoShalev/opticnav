package opticnav.ardd.ard;

import java.io.IOException;

public class ARDGatekeeperException extends IOException {
    private static final long serialVersionUID = 1L;

    public ARDGatekeeperException(String message) {
        super(message);
    }
    
    public ARDGatekeeperException(Throwable cause) {
        super(cause);
    }

    public ARDGatekeeperException(String message, Throwable cause) {
        super(message, cause);
    }
}
