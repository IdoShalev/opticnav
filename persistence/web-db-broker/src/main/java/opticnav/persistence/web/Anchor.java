package opticnav.persistence.web;

public class Anchor {
    private int lng, lat;
    private int localX, localY;
    
    public Anchor(int lng, int lat, int localX, int localY) {
        this.lng = lng;
        this.lat = lat;
        this.localX = localX;
        this.localY = localY;
    }

    public int getLng() {
        return lng;
    }

    public int getLat() {
        return lat;
    }

    public int getLocalX() {
        return localX;
    }

    public int getLocalY() {
        return localY;
    }
    
}
