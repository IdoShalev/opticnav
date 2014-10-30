package opticnav.persistence.map;

/**
 * Holds the map name and ID
 * 
 * @author Danny Spencer
 */
public class MapsListEntry {
    private String name;
    private int id;

    /**
     * Constructor
     * Sets the name and ID of the map
     * 
     * @param name The map name
     * @param id The map ID
     */
    public MapsListEntry(String name, int id) {
        this.name = name;
        this.id = id;
    }
    
    /**
     * Gets the name of the map name
     * 
     * @return The map name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the map ID
     * 
     * @return The map ID
     */
    public int getId() {
        return id;
    }
}
