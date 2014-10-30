package opticnav.daemon.admin;

import java.io.IOException;

public class ARDdAdminException extends IOException {
    private static final long serialVersionUID = 1L;

    public ARDdAdminException(String message) {
        super(message);
    }
    
    public ARDdAdminException(Throwable cause) {
        super(cause);
    }

    public ARDdAdminException(String message, Throwable cause) {
        super(message, cause);
    }
}
