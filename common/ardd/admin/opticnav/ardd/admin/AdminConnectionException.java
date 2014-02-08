package opticnav.ardd.admin;

public class AdminConnectionException extends Exception {
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
