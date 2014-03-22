package opticnav.ardroid.ui;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import opticnav.ardroid.model.Coordinate;
import opticnav.ardroid.model.MapModel;
import opticnav.ardroid.model.MapModelObserver;
import opticnav.ardroid.model.Marker;
import opticnav.ardroid.ui.marker.*;

import java.util.*;

public class MapView extends View implements MapModelObserver {
    // TODO - look into SurfaceView
    private MapModel model;

    private final Map<Integer, MarkerState> markerStates;
    private final Map<Integer, Queue<Pair<MarkerState.Type, Marker>>> markerStateQueues;

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.markerStates = new HashMap<Integer, MarkerState>();
        this.markerStateQueues = new HashMap<Integer, Queue<Pair<MarkerState.Type, Marker>>>();
    }

    public void setModel(MapModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Map model already set");
        }
        this.model = model;

        // All existing markers are added to markerStates as STILL
        for (Map.Entry<Integer, Marker> entry: model.getMarkerList().entrySet()) {
            final int id = entry.getKey();
            final Marker marker = entry.getValue();
            this.markerStates.put(id, new Still(marker));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final float density = getResources().getDisplayMetrics().density;

        Paint paint = new Paint();
        paint.setColor(Color.rgb(0, 0, 0));
        for (MarkerState markerState: markerStates.values()) {
            Coordinate coordinate = markerState.getCurrentCoordinate();
            final float x = (float)coordinate.getLongitudeDouble()*density;
            final float y = (float)coordinate.getLatitudeDouble()*density;
            final float radius = 10*visibilityErp(markerState.getCurrentVisibility())*density;

            canvas.drawCircle(x, y, radius, paint);
        }
    }

    /**
     *
     * @param in An input value ranging 0..1
     * @return The new interpolated value
     */
    private static float visibilityErp(float in) {
        // a nice overshoot interpolation
        final float o = (float)(Math.PI/2 * 1.4);
        return (float)(Math.sin(in*o)/Math.sin(o));
    }

    /** Update the view by one frame */
    public void step() {
        // Redraw the view
        invalidate();

        markerStatesStep();
    }

    /**
     * Update (animate) all markers and their states
     */
    private void markerStatesStep() {
        List<Integer> pendingStateChange = new ArrayList<Integer>();

        Set<Map.Entry<Integer, MarkerState>> set;
        set = markerStates.entrySet();

        Iterator<Map.Entry<Integer, MarkerState>> it;

        for (it = this.markerStates.entrySet().iterator(); it.hasNext(); ) {
            final Map.Entry<Integer, MarkerState> entry = it.next();
            final int id = entry.getKey();
            final MarkerState markerState = entry.getValue();

            markerState.step();

            if (markerState.isFinished()) {
                if (markerState.getStateType() == MarkerState.Type.REMOVING) {
                    // Marker is fully removed
                    // Remove from markerStates
                    it.remove();
                } else {
                    // Set marker as changing states
                    pendingStateChange.add(id);
                }
            }
        }

        for (int id: this.markerStates.keySet()) {
            final MarkerState markerState = this.markerStates.get(id);

            if (markerState.isFinished()) {
                final Pair<MarkerState.Type, Marker> p = dequeueMarkerState(id);

                if (p == null) {
                    // No pending state changes - keep it still
                    Marker marker = markerState.getMarker();
                    this.markerStates.put(id, new Still(marker));
                } else {
                    final MarkerState.Type type = p.first;
                    final Marker marker = p.second;

                    switch (type) {
                    case MOVING:
                        this.markerStates.put(id, new Moving(markerState.getMarker(), marker));
                        break;
                    case REMOVING:
                        this.markerStates.put(id, new Removing(markerState.getMarker()));
                        break;
                    default:
                        throw new IllegalStateException("Illegal marker state: " + type);
                    }
                }
            }
        }
    }

    @Override
    public void postMarkerCreating(int id, Marker marker) {
        this.markerStates.put(id, new Creating(marker));
    }

    @Override
    public void postMarkerMoving(int id, Marker marker) {
        enqueueMarkerState(id, MarkerState.Type.MOVING, marker);
    }

    @Override
    public void postMarkerRemoving(int id) {
        enqueueMarkerState(id, MarkerState.Type.REMOVING, null);
    }

    private void enqueueMarkerState(int id, MarkerState.Type type, Marker marker) {
        // TODO - override certain queued states (eg. no need to queue up two consecutive MOVINGs)
        Queue<Pair<MarkerState.Type, Marker>> q = this.markerStateQueues.get(id);

        if (q == null) {
            // Queue doesn't exist, create one
            q = new LinkedList<Pair<MarkerState.Type, Marker>>();
            this.markerStateQueues.put(id, q);
        }

        q.add(new Pair<MarkerState.Type, Marker>(type, marker));
    }

    private Pair<MarkerState.Type, Marker> dequeueMarkerState(int id) {
        Queue<Pair<MarkerState.Type, Marker>> q = this.markerStateQueues.get(id);

        if (q == null) {
            // No queue to dequeue from
            return null;
        } else {
            Pair<MarkerState.Type, Marker> result = q.remove();
            if (q.isEmpty()) {
                // Queue is empty, get rid of it
                this.markerStateQueues.remove(id);
            }
            return result;
        }
    }
}
