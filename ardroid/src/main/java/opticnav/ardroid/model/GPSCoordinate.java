package opticnav.ardroid.model;

public class GPSCoordinate {
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
     * @param lng Longitude, expressed as a floating point value from -180 to +180
     * @param lat Latitude, expressed as a floating point value from -90 to +90
     */
    public GPSCoordinate(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    /**
     *
     * @param lng Longitude, expressed as hundredths of a longitude second
     * @param lat Latitude, expressed as hundredths of a latitude second
     */
    public static GPSCoordinate fromInternalInt(int lng, int lat) {
        double dlng = (double)lng/LONGITUDE_CENTISECONDS_180 * 180.0;
        double dlat = (double)lat/LATITUDE_CENTISECONDS_90 * 90.0;
        return new GPSCoordinate(dlng, dlat);
    }

    public double getLongitudeDouble() {
        return this.lng;
    }

    public double getLatitudeDouble() {
        return this.lat;
    }
}
