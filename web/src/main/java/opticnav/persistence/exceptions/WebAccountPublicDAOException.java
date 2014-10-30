package opticnav.persistence.exceptions;

/**
 * Exception that is thrown if a critical backend error occurs in the WebAccountPublicDAO class
 * 
 * @author Danny Spencer
 */
public class WebAccountPublicDAOException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a WebAccountPublicDAO exception to be thrown
     * 
     * @param exception The exception
     */
    public WebAccountPublicDAOException(Throwable exception) {
        super(exception);
    }
}
