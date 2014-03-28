package opticnav.persistence.web.exceptions;

public class WebAccountDAOException extends Exception {
    private static final long serialVersionUID = 1L;

    public WebAccountDAOException(Throwable exception) {
        super(exception);
    }

    public WebAccountDAOException(String message) {
        super(message);
    }
}
