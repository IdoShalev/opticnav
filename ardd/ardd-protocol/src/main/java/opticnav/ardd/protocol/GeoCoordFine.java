package opticnav.ardd.protocol;

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
    
    public double getLongitudeDouble() {
        return (double)this.lng/LNG_MAX;
    }
    
    public double getLatitudeDouble() {
        return (double)this.lat/LAT_MAX;
    }
    
    public int getLongitudeInt() {
        return this.lng;
    }
    
    public int getLatitudeInt() {
        return this.lat;
    }
}
