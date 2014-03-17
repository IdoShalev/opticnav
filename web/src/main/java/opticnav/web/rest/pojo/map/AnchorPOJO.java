package opticnav.web.rest.pojo.map;

import opticnav.persistence.web.map.Anchor;

public class AnchorPOJO {
    public int lng, lat;
    public int localX, localY;
    
    public AnchorPOJO(Anchor a) {
        this.lng = a.getLng();
        this.lat = a.getLat();
        this.localX = a.getLocalX();
        this.localY = a.getLocalY();
    }
}
