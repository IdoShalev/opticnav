package opticnav.persistence.web;

public class Marker {
    private int lng, lat;

    public Marker(int lng, int lat) {
        super();
        this.lng = lng;
        this.lat = lat;
    }

    public int getLng() {
        return lng;
    }

    public int getLat() {
        return lat;
    }
}
