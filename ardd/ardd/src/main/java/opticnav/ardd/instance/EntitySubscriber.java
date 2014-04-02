package opticnav.ardd.instance;

import opticnav.ardd.protocol.GeoCoordFine;

public interface EntitySubscriber {
    void newMarker(int markerID, String name, GeoCoordFine geoCoord);
    void moveMarker(int markerID, GeoCoordFine geoCoord);
    void removeMarker(int markerID);
}
