package opticnav.ardd.mockdevice;

import opticnav.ardd.protocol.GeoCoordFine;

public class GeoCoordFineUtil {
    public static GeoCoordFine reprToGeoCoord(String line) {
        final String[] arr = line.split("\\s+");
        final double lat = Double.parseDouble(arr[0]);
        final double lng = Double.parseDouble(arr[1]);
        
        return GeoCoordFine.fromDouble(lng, lat);
    }
}
