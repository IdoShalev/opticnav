package opticnav.persistence.web.map;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Holds a Marker list and a Anchor list for a map to be modified
 * 
 * @author Danny Spencer
 */
public class ModifyMap {
    private LinkedList<Marker> markers;
    private LinkedList<Anchor> anchors;
    
    /**
     * Constructor
     * Initializes markerList and anchorList to LinkedList
     */
    public ModifyMap() {
        this.markers = new LinkedList<Marker>();
        this.anchors = new LinkedList<Anchor>();
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
     * Gets an unmodifiable list of Marker objects
     * 
     * @return An unmodifiable list of Marker objects
     */
    public List<Marker> getMarkers() {
        return Collections.unmodifiableList(markers);
    }

    /**
     * Gets an unmodifiable list of Anchor objects
     * 
     * @return An unmodifiable list of Anchor objects
     */
    public List<Anchor> getAnchors() {
        return Collections.unmodifiableList(anchors);
    }
}
