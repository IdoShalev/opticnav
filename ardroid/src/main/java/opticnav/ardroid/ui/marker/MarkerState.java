package opticnav.ardroid.ui.marker;

import opticnav.ardroid.model.Coordinate;
import opticnav.ardroid.model.Marker;

public interface MarkerState {
    public static final int FPS = 60;
    public static enum Type {
        CREATING, STILL, MOVING, REMOVING
    };

    public Type getStateType();
    public void step();
    public boolean isFinished();
    public Marker getMarker();
    public Coordinate getCurrentCoordinate();
    public float getCurrentVisibility();
}
