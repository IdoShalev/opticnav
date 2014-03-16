package opticnav.web.rest.pojo.map;

import java.util.LinkedList;
import java.util.List;

import opticnav.persistence.web.Anchor;
import opticnav.persistence.web.Map;
import opticnav.persistence.web.Marker;

public class GetMapPOJO {
    public String name;
    public List<MarkerPOJO> marker;
    public List<AnchorPOJO> anchor;
    
    public GetMapPOJO(Map map) {
        this.marker = new LinkedList<>();
        this.anchor = new LinkedList<>();
        
        for (Marker m: map.getMarkers()) {
            this.marker.add(new MarkerPOJO(m));
        }
        
        for (Anchor a: map.getAnchors()) {
            this.anchor.add(new AnchorPOJO(a));
        }
    }
}
