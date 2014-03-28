package opticnav.web.controllers.rest.pojo.map;

import opticnav.persistence.web.map.Anchor;

public class AnchorPOJO {
    public GPSPOJO gps;
    public LocalPOJO local;
    
    public AnchorPOJO(Anchor a) {
        this.gps = new GPSPOJO();
        this.local = new LocalPOJO();
        this.gps.lng = a.getLng();
        this.gps.lat = a.getLat();
        this.local.x = a.getLocalX();
        this.local.y = a.getLocalY();
    }
    
    public AnchorPOJO() {
    }
}
