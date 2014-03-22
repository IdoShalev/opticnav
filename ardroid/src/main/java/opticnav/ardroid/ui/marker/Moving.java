package opticnav.ardroid.ui.marker;

import opticnav.ardroid.model.Coordinate;
import opticnav.ardroid.model.Marker;

public class Moving implements MarkerState {
    private final static float SECONDS = 0.5f;
    private final Marker src;
    private final Marker dst;
    private float phase = 0.0f;

    public Moving(Marker src, Marker dst) {
        this.src = src;
        this.dst = dst;
    }

    @Override
    public Type getStateType() {
        return Type.MOVING;
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
        return this.dst;
    }

    @Override
    public Coordinate getCurrentCoordinate() {
        return Coordinate.lerp(this.src.getCoordinate(), this.dst.getCoordinate(), easeErp(phase));
    }

    @Override
    public float getCurrentVisibility() {
        return 1.0f;
    }

    private float easeErp(float in) {
        return (float)Math.sin(in*Math.PI/2);
    }
}
