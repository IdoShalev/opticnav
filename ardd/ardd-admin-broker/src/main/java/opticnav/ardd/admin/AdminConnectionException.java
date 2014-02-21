package opticnav.ardd.admin;

import java.io.IOException;

public class AdminConnectionException extends IOException {
    private static final long serialVersionUID = 1L;

    public AdminConnectionException(String message) {
        super(message);
    }
    
    public AdminConnectionException(Throwable cause) {
        super(cause);
    }

    public AdminConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
