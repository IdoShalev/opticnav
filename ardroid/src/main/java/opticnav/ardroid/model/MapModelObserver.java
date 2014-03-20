package opticnav.ardroid.model;

public interface MapModelObserver {
    public void postMarkerCreating(int id, Marker marker);
    public void postMarkerMoving(int id, Marker marker);
    public void postMarkerRemoving(int id);
}
