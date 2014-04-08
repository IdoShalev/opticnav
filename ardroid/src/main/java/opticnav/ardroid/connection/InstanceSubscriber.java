package opticnav.ardroid.connection;

import opticnav.ardd.ard.ARDInstanceSubscriber;
import opticnav.ardd.ard.MapTransform;
import opticnav.ardd.protocol.GeoCoordFine;
import opticnav.ardroid.model.Coordinate;
import opticnav.ardroid.model.MapModel;
import opticnav.ardroid.model.Marker;

public class InstanceSubscriber implements ARDInstanceSubscriber {
    private final MapModel mapModel;
    private final MapTransform transform;

    public InstanceSubscriber(MapModel mapModel, MapTransform transform) {
        this.mapModel = mapModel;
        this.transform = transform;
    }

    @Override
    public void markerCreate(int id, String name, GeoCoordFine geoCoord) {
        final MapTransform.Coordinate c = transform.geoToImageLocal(geoCoord);
        this.mapModel.addMarker(id, new Marker(name, new Coordinate(c.x, c.y)));
    }

    @Override
    public void markerMove(int id, GeoCoordFine geoCoord) {
        final MapTransform.Coordinate c = transform.geoToImageLocal(geoCoord);
        this.mapModel.moveMarker(id, new Coordinate(c.x, c.y));
    }

    @Override
    public void markerRemove(int id) {
        this.mapModel.removeMarker(id);
    }
}
