package opticnav.persistence.web.exceptions;

/**
 * Exception that is thrown if a critical backend error occurs in the WebResourceDAO class
 * 
 * @author Danny Spencer
 */
public class WebResourceDAOExcpetion extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a WebResourceDAO exception to be thrown
     * 
     * @param exception The exception
     */
    public WebResourceDAOExcpetion(Throwable exception) {
        super(exception);
    }
}
