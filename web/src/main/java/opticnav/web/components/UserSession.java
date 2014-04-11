package opticnav.web.components;

import java.io.Serializable;

/**
 * UserSession is a Spring session bean interface. This implementation of this object is unique to each Java EE session.
 * 
 * @author Danny Spencer
 *
 */
public interface UserSession extends Serializable {
    /**
     * Represents a logged in user.
     * 
     * @author Danny Spencer
     *
     */
    public static class User implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String username;
        private int id;
        
        public User(String username, int id) {
            this.username = username;
            this.id = id;
        }
        
        public String getUsername() {
            return username;
        }
        
        public int getId() {
            return id;
        }
    }
    
    /**
     * Get the currently logged in user.
     * @return A User object, or null if no user is logged in
     */
    public User getUser();
    /**
     * Set a user the session.
     * 
     * @param username The username
     * @param id The account id
     */
    public void setUser(String username, int id);
    /**
     * Unset a user in the session. This effectively "logs them out".
     */
    public void resetUser();
}
