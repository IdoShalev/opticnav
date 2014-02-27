package opticnav.ardd.ard;

import java.io.IOException;

public class ARDConnectionException extends IOException {
    private static final long serialVersionUID = 1L;

    public ARDConnectionException(String message) {
        super(message);
    }
    
    public ARDConnectionException(Throwable cause) {
        super(cause);
    }

    public ARDConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
