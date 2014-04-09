package opticnav.persistence.web.map;

/**
 * Class that represents a Marker on a map
 * holds the name, latitude, longitude, and the resource ID of a marker 
 * 
 * @author Kay Bernhardt
 */
public class Marker {
    private int lng, lat, resourceID;
    private String name;

    /**
     * Constructor
     * Sets the name, latitude, longitude, and the resource ID of the marker
     * 
     * @param lng The longitude of the marker
     * @param lat The latitude of the marker
     * @param name The name of the marker
     * @param resourceID The resource ID of the marker
     */
    public Marker(int lng, int lat, String name, int resourceID) {
        this.resourceID = resourceID;
        this.lng = lng;
        this.lat = lat;
        this.name = name;
    }

    /**
     * Gets the longitude of the marker
     * 
     * @return The longitude of the marker
     */
    public int getLng() {
        return lng;
    }

    /**
     * Gets the latitude of the marker
     * 
     * @return The latitude of the marker
     */
    public int getLat() {
        return lat;
    }

    /**
     * Gets the name of the name of the marker
     * 
     * @return The name of the marker
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the resource ID of the marker
     * 
     * @return The resource ID of the marker
     */
    public int getResourceID() {
        return resourceID;
    }
}
