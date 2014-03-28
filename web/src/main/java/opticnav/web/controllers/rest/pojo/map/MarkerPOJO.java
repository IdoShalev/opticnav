package opticnav.web.controllers.rest.pojo.map;

import opticnav.persistence.web.map.Marker;

public class MarkerPOJO {
    public static class InfoPOJO {
        public String name;
    }
    public InfoPOJO info;
    public GPSPOJO gps;
    
    public MarkerPOJO(Marker m) {
        this.info = new InfoPOJO();
        this.gps = new GPSPOJO();
        this.info.name = m.getName();
        this.gps.lng = m.getLng();
        this.gps.lat = m.getLat();
    }
    
    public MarkerPOJO() {
    }
}
