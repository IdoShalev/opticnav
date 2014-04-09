package opticnav.ardd.instance;

import opticnav.ardd.protocol.GeoCoordFine;

/**
 * An EntitySubscriber is notified when a marker is added, moved, or removed.
 * 
 * @author Danny Spencer
 *
 */
public interface EntitySubscriber {
    void newMarker(int markerID, String name, GeoCoordFine geoCoord);
    void moveMarker(int markerID, GeoCoordFine geoCoord);
    void removeMarker(int markerID);
}
