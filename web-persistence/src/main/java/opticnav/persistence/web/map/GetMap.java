package opticnav.persistence.web.map;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Stores map data retrieved from a database
 * 
 * @author Danny Spencer
 */
public class GetMap {
    private String name;
    private int imageResource;
    private LinkedList<Marker> markers;
    private LinkedList<Anchor> anchors;
    
    /**
     * Constructor
     * Initializes the marker and anchor linked lists
     * Sets the map name and resource ID
     * 
     * @param name The map name
     * @param imageResource The resource id of the map
     */
    public GetMap(String name, int imageResource) {
        this.name = name;
        this.imageResource = imageResource;
        this.markers = new LinkedList<Marker>();
        this.anchors = new LinkedList<Anchor>();
    }
    
    /**
     * Gets the name of the map
     * 
     * @return The map name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the resource id of the map
     * 
     * @return The resource ID of the map
     */
    public int getImageResource() {
        return imageResource;
    }

    /**
     * Adds a Marker object to the marker LinkedList
     * 
     * @param marker The Marker object to be added
     */
    public void addMarker(Marker marker){
        this.markers.add(marker);
    }
    /**
     * Adds an Anchor object to the anchor LinkedList
     * 
     * @param anchor The Anchor object to be added
     */
    public void addAnchor(Anchor anchor){
        this.anchors.add(anchor);
    }

    /**
     * Gets an unmodifiable list of the maps Marker objects
     * 
     * @return An unmodifiable list of the maps Marker objects
     */
    public List<Marker> getMarkers() {
        return Collections.unmodifiableList(markers);
    }

    /**
     * Gets an unmodifiable list of the maps Anchor objects
     * 
     * @return An unmodifiable list of the maps Anchor objects
     */
    public List<Anchor> getAnchors() {
        return Collections.unmodifiableList(anchors);
    }
}
