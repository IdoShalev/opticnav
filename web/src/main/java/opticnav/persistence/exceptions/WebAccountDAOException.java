package opticnav.persistence.exceptions;

/**
 * Exception that is thrown if a critical backend error occurs in the WebAccountDAO class
 * 
 * @author Danny Spencer
 */
public class WebAccountDAOException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a WebAccountDAO exception to be thrown
     * 
     * @param exception The exception
     */
    public WebAccountDAOException(Throwable exception) {
        super(exception);
    }

    /**
     * Constructs a WebAccountDAO exception to be thrown
     * 
     * @param message The message specifying the error
     */
    public WebAccountDAOException(String message) {
        super(message);
    }
}
