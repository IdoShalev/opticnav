package opticnav.web.rest.pojo.map;

import opticnav.persistence.web.Marker;

public class MarkerPOJO {
    public String name;
    public int lng, lat;
    
    public MarkerPOJO(Marker m) {
        this.name = m.getName();
        this.lng = m.getLng();
        this.lat = m.getLat();
    }
}
