package opticnav.ardroid.model;

public class Anchor {
    private int localX, localY;
    private GPSCoordinate coordinate;

    public Anchor(int localX, int localY, GPSCoordinate coordinate) {
        this.localX = localX;
        this.localY = localY;
        this.coordinate = coordinate;
    }

    public Anchor(int localX, int localY, double lng, double lat) {
        this.localX = localX;
        this.localY = localY;
        this.coordinate = new GPSCoordinate(lng, lat);
    }

    public int getLocalX() {
        return localX;
    }

    public int getLocalY() {
        return localY;
    }

    public GPSCoordinate getGPSCoordinate() {
        return coordinate;
    }
}
