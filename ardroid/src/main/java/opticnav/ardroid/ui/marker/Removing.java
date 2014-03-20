package opticnav.ardroid.ui.marker;

import opticnav.ardroid.model.Coordinate;
import opticnav.ardroid.model.Marker;

public class Removing implements MarkerState {
    private static final float SECONDS = 0.5f;
    private final Marker marker;
    private float phase = 0.0f;

    public Removing(Marker marker) {
        this.marker = marker;
    }

    @Override
    public Type getStateType() {
        return Type.REMOVING;
    }

    @Override
    public void step() {
        this.phase += 1.0f/(MarkerState.FPS*SECONDS);
    }

    @Override
    public boolean isFinished() {
        return this.phase >= 1.0f;
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
        return 1.0f-phase;
    }
}
