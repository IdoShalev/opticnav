package opticnav.web.test.beans;

public class User {
    private String name;
    private long id;
    
    public User(String name, long id) {
        this.name = name;
        this.id = id;
    }
    
    public String getUsername() {
        return name;
    }
    
    public long getID() {
        return id;
    }
}
