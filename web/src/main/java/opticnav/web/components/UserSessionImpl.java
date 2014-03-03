package opticnav.web.components;

public class UserSessionImpl implements UserSession {
    private static final long serialVersionUID = 1L;
    
    private User user;

    public User getUser() {
        return this.user;
    }
    
    public void setUser(String username, int id) {
        this.user = new User(username, id);
    }
    
    public void resetUser() {
        this.user = null;
    }
}
