package opticnav.persistence.web.map;

public class Marker {
    private int lng, lat, resourceID;
    private String name;

    public Marker(int lng, int lat, String name, int resourceID) {
        this.resourceID = resourceID;
        this.lng = lng;
        this.lat = lat;
        this.name = name;
    }

    public int getLng() {
        return lng;
    }

    public int getLat() {
        return lat;
    }

    public String getName() {
        return name;
    }

    public int getResourceID() {
        return resourceID;
    }
}
