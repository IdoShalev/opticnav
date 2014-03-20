package opticnav.ardroid.opticnav.ardroid.model;

public class Coordinate {
    /** How many centiseconds are from 180 degrees to Greenwich */
    private static final int LONGITUDE_CENTISECONDS_180 = 180*60*60*100;
    /** How many centiseconds are from 90 degrees to the Equator */
    private static final int LATITUDE_CENTISECONDS_90 = 90*60*60*100;

    /** Longitude, expressed as hundredths of a longitude second */
    private double lng;
    /** Latitude, expressed as hundredths of a longitude second */
    private double lat;

    /**
     *
     * @param lng Longitude, expressed as hundredths of a longitude second
     * @param lat Latitude, expressed as hundredths of a latitude second
     */
    public Coordinate(int lng, int lat) {
        this.lng = (double)lng/LONGITUDE_CENTISECONDS_180 * 180.0;
        this.lat = (double)lat/LATITUDE_CENTISECONDS_90 * 90.0;
    }

    /**
     *
     * @param lng Longitude, expressed as a floating point value from -180 to +180
     * @param lat Latitude, expressed as a floating point value from -90 to +90
     */
    public Coordinate(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
    }
}
