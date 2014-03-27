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
    public Float getCurrentDirection() {
        final boolean srcD, dstD;
        srcD = this.src.hasDirection();
        dstD = this.dst.hasDirection();

        if (srcD && dstD) {
            // interpolate between the directions
            return lerpWrap(this.src.getDirection(), this.dst.getDirection(), easeErp(phase));
        } else if (srcD && !dstD) {
            // marker is losing direction, keep direction of source
            return this.src.getDirection();
        } else if (!srcD && dstD) {
            // marker is getting direction, keep direction of destination
            return this.dst.getDirection();
        } else {
            // there is no direction
            return null;
        }
    }

    @Override
    public float getCurrentVisibility() {
        return 1.0f;
    }

    private float easeErp(float in) {
        return (float)Math.sin(in*Math.PI/2);
    }

    /**
     * Perform a linear interpolation in such a way it travels the least amount possible if it wraps 0..1
     * @param begin The beginning value, a float in the range 0..1
     * @param end The ending value, float in the range 0..1
     * @param phase The phase value, float in the range 0..1
     */
    private static float lerpWrap(float begin, float end, float phase) {
        if (Math.abs(end-begin) > 0.5) {
            // wrap around
            if (begin < end) {
                // begin -> 0, 1 -> end
                if (phase < begin) {
                    return lerp(begin, 0, phase/begin);
                } else {
                    return lerp(1, end, (phase-begin)/(1-begin));
                }
            } else {
                // begin -> 1, 0 -> end
                if (phase < begin) {
                    return lerp(begin, 1, phase/begin);
                } else {
                    return lerp(0, end, (phase-begin)/(1-begin));
                }
            }
        } else {
            // don't wrap around
            return lerp(begin, end, phase);
        }
    }

    /**
     * Linear intERPolation
     *
     * @param begin The beginning value, a float in the range 0..1
     * @param end The ending value, float in the range 0..1
     * @param phase The phase value, float in the range 0..1
     */
    private static float lerp(float begin, float end, float phase) {
        return (end-begin)*phase + begin;
    }
}
