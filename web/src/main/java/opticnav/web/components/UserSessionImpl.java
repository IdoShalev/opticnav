package opticnav.web.components;

/**
 * A concrete implementation of UserSession. This is so simple. It does what you would expect.
 * 
 * @author Danny Spencer
 *
 */
public class UserSessionImpl implements UserSession {
    private static final long serialVersionUID = 1L;
    
    private User user;

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public void setUser(String username, int id) {
        this.user = new User(username, id);
    }

    @Override
    public void resetUser() {
        this.user = null;
    }
}
