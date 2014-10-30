package opticnav.daemon.device;

import opticnav.daemon.protocol.GeoCoordFine;

/**
 * The ARDInstanceSubscriber interface gets notified when the {@link ARDInstance} broker receives marker changes.
 * 
 * @author Danny Spencer
 *
 */
public interface ARDInstanceSubscriber {
    public void markerCreate(int id, String name, GeoCoordFine geoCoord);
    public void markerMove(int id, GeoCoordFine geoCoord);
    public void markerRemove(int id);
}
