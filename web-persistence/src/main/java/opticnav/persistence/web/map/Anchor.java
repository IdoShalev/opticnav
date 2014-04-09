package opticnav.persistence.web.map;

/**
 * Class that represents one o the maps anchor points
 * 
 * @author Kay Bernhardt
 */
public class Anchor {
    private int lng, lat;
    private int localX, localY;
    
    /**
     * Constructor
     * Sets the anchors longitude, latitude, localX, and localY coordinates 
     * 
     * @param lng The longitude
     * @param lat The latitude
     * @param localX The images x coordinate
     * @param localY The images y coordinate
     */
    public Anchor(int lng, int lat, int localX, int localY) {
        this.lng = lng;
        this.lat = lat;
        this.localX = localX;
        this.localY = localY;
    }

    /**
     * 
     * Gets the anchors longitude
     * @return The anchors longitude
     */
    public int getLng() {
        return lng;
    }

    /**
     * Gets the anchors latitude
     * @return The anchors latitude
     */
    public int getLat() {
        return lat;
    }

    /**
     * Gets the anchor's images x coordinate
     * @return The anchors images x coordinate
     */
    public int getLocalX() {
        return localX;
    }

    /**
     * Gets the anchor's images y coordinate
     * @return The anchors images y coordinate
     */
    public int getLocalY() {
        return localY;
    }
    
}
