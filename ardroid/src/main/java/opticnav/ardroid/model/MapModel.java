package opticnav.ardroid.model;

import android.graphics.Bitmap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MapModel {
    private final boolean hasMapImage;
    private final Bitmap bitmap;
    private final Map<Integer, Marker> markerList;
    private MapModelObserver observer;

    public MapModel() {
        this.hasMapImage = false;
        this.bitmap = null;
        this.markerList = new HashMap<Integer, Marker>();
        this.observer = null;
    }

    public MapModel(Bitmap bitmap) {
        this.hasMapImage = true;
        this.bitmap = bitmap;
        this.markerList = new HashMap<Integer, Marker>();
        this.observer = null;
    }

    public void setObserver(MapModelObserver observer) {
        this.observer = observer;
    }

    public void unsetObserver() {
        this.observer = null;
    }

    public boolean hasMapImage() {
        return this.hasMapImage;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void addMarker(int id, Marker marker) {
        Marker old = this.markerList.put(id, marker);
        if (old != null) {
            throw new IllegalStateException("No existing marker should exist: " + id);
        }

        if (this.observer != null) {
            this.observer.postMarkerCreating(id, marker);
        }
    }

    public void moveMarker(int id, Coordinate coordinate, float direction) {
        Marker oldMarker = this.markerList.get(id);
        if (oldMarker == null) {
            throw new IllegalStateException("Marker to move must exist: " + id);
        }

        Marker newMarker = oldMarker.withNewCoordinateAndDirection(coordinate, direction);
        // Replace the old marker with a new object
        this.markerList.put(id, newMarker);

        if (this.observer != null) {
            this.observer.postMarkerMoving(id, newMarker);
        }
    }

    public void moveMarker(int id, Coordinate coordinate) {
        Marker oldMarker = this.markerList.get(id);
        if (oldMarker == null) {
            throw new IllegalStateException("Marker to move must exist: " + id);
        }

        Marker newMarker = oldMarker.withNewCoordinate(coordinate);
        // Replace the old marker with a new object
        this.markerList.put(id, newMarker);

        if (this.observer != null) {
            this.observer.postMarkerMoving(id, newMarker);
        }
    }

    public void removeMarker(int id) {
        Marker old = this.markerList.remove(id);
        if (old == null) {
            throw new IllegalStateException("Marker to delete must exist: " + id);
        }

        if (this.observer != null) {
            this.observer.postMarkerRemoving(id);
        }
    }

    public Map<Integer, Marker> getMarkerList() {
        return Collections.unmodifiableMap(this.markerList);
    }
}
