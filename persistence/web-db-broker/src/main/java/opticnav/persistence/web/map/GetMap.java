package opticnav.persistence.web.map;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class GetMap {
    private String name;
    private int imageResource;
    private LinkedList<Marker> markers;
    private LinkedList<Anchor> anchors;
    
    public GetMap(String name, int imageResource) {
        this.name = name;
        this.imageResource = imageResource;
        this.markers = new LinkedList<Marker>();
        this.anchors = new LinkedList<Anchor>();
    }
    
    public String getName() {
        return name;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void addMarker(Marker marker){
        this.markers.add(marker);
    }
    public void addAnchor(Anchor anchor){
        this.anchors.add(anchor);
    }

    public List<Marker> getMarkers() {
        return Collections.unmodifiableList(markers);
    }

    public List<Anchor> getAnchors() {
        return Collections.unmodifiableList(anchors);
    }
}
