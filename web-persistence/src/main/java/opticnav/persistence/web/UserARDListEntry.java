package opticnav.persistence.web;

/**
 * Holds a username and the ARD ID that belongs to it
 * 
 * @author Kay Bernhardt
 */
public class UserARDListEntry {
    private String name;
    private int ard;
    
    /**
     * Constructor
     * Sets the username and the ARD ID that belongs to it
     * 
     * @param name The username
     * @param ard The ARD ID belonging to the username
     */
    public UserARDListEntry(String name, int ard) {
        this.name = name;
        this.ard = ard;
    }

    /**
     * Get the username
     * 
     * @return The username
     */
    public String getName() {
        return name;
    }

    /**
     * Get the ARD ID
     * 
     * @return The ARD ID
     */
    public int getArd() {
        return ard;
    }
}
