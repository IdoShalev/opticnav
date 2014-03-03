package opticnav.web.components;

import java.io.Serializable;

public interface UserSession extends Serializable {
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
    
    public User getUser();
    public void setUser(String username, int id);
    public void resetUser();
}
