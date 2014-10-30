package opticnav.ardd.instance;

import opticnav.ardd.protocol.GeoCoordFine;

/**
 * An EntitySubscriber is notified when an entity is added, moved, or removed.
 * 
 * @author Danny Spencer
 *
 */
public interface EntitySubscriber {
    /**
     * A new marker is added.
     * 
     * @param markerID A unique, unused ID identifying a marker
     * @param name The name of the marker
     * @param geoCoord The location of the marker
     */
    void newMarker(int markerID, String name, GeoCoordFine geoCoord);
    /**
     * An existing marker is moved.
     * 
     * @param markerID An existing ID identifying a marker
     * @param geoCoord The new location of the marker
     */
    void moveMarker(int markerID, GeoCoordFine geoCoord);
    /**
     * An existing marker is removed.
     * 
     * @param markerID An existing ID identifying a marker
     */
    void removeMarker(int markerID);
}
