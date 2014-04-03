package opticnav.ardd.ard;

import opticnav.ardd.protocol.GeoCoordFine;

public interface ARDInstanceSubscriber {
    public void markerCreate(int id, String name, GeoCoordFine geoCoord);
    public void markerMove(int id, GeoCoordFine geoCoord);
    public void markerRemove(int id);
}
