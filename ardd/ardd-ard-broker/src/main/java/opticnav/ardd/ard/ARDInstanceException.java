package opticnav.ardd.ard;

import java.io.IOException;

public class ARDInstanceException extends IOException {
    private static final long serialVersionUID = 1L;

    public ARDInstanceException(String message) {
        super(message);
    }
    
    public ARDInstanceException(Throwable cause) {
        super(cause);
    }

    public ARDInstanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
