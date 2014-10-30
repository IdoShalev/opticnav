package opticnav.daemon.protocol;

/**
 * 32x more fine-grained than the standard geocoordinate representation (ISO 6709). Still fits in an integer.
 *
 * 1 second of latitude ~= 30.86m
 * 1 centisecond ~= 30.86cm
 * 1 GeoCoordFine unit ~= 0.964cm
 * 
 * Longitude measures about the same as latitude at the equator, and gets shorter as it travels towards the poles.
 * 
 */
public class GeoCoordFine {
    public static final int LNG_MAX=(180*60*60*100)<<5;
    public static final int LAT_MAX=(90*60*60*100)<<5;
            
    private final int lng, lat;
    
    public GeoCoordFine(int lng, int lat) {
        this.lng = lng;
        this.lat = lat;
    }

    /**
     * Creates a GeoCoordFine object using doubles
     *
     * @param lng Longitude - a double from -180 to +180
     * @param lat Latitude - a double from -90 to +90
     * @return
     */
    public static GeoCoordFine fromDouble(double lng, double lat) {
        final int x = (int)Math.round((lng/180)*LNG_MAX);
        final int y = (int)Math.round((lat/90)*LAT_MAX);
        return new GeoCoordFine(x, y);
    }
    
    public double getLongitudeDouble() {
        return (double)this.lng*180/LNG_MAX;
    }
    
    public double getLatitudeDouble() {
        return (double)this.lat*90/LAT_MAX;
    }
    
    public int getLongitudeInt() {
        return this.lng;
    }
    
    public int getLatitudeInt() {
        return this.lat;
    }
    
    @Override
    public String toString() {
        return String.format("%+.7f N/S %+.7f E/W", getLatitudeDouble(), getLongitudeDouble());
    }
}
