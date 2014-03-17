package opticnav.web.rest.pojo.map;

import java.util.LinkedList;
import java.util.List;

import opticnav.persistence.web.map.Anchor;
import opticnav.persistence.web.map.GetMap;
import opticnav.persistence.web.map.Marker;

public class GetMapPOJO {
    public String name;
    public int imageResource;
    public List<MarkerPOJO> marker;
    public List<AnchorPOJO> anchor;
    
    public GetMapPOJO(GetMap map) {
        this.name = map.getName();
        this.imageResource = map.getImageResource();
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
