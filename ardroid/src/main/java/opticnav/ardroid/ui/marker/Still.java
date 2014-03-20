package opticnav.ardroid.ui.marker;

import opticnav.ardroid.model.Coordinate;
import opticnav.ardroid.model.Marker;

public class Still implements MarkerState {
    private final Marker marker;

    public Still(Marker marker) {
        this.marker = marker;
    }

    @Override
    public Type getStateType() {
        return Type.STILL;
    }

    @Override
    public void step() {
        // do nothing
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public Marker getMarker() {
        return this.marker;
    }

    @Override
    public Coordinate getCurrentCoordinate() {
        return this.marker.getCoordinate();
    }

    @Override
    public float getCurrentVisibility() {
        return 1.0f;
    }
}
