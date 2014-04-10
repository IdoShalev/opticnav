package opticnav.ardroid.model;

import android.app.Activity;

public class MapModelObserverAsync implements MapModelObserver {
    private final Activity activity;
    private final MapModelObserver observer;

    public MapModelObserverAsync(Activity activity, MapModelObserver observer) {
        this.activity = activity;
        this.observer = observer;
    }

    @Override
    public void postMarkerCreating(final int id, final Marker marker) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                observer.postMarkerCreating(id, marker);
            }
        });
    }

    @Override
    public void postMarkerMoving(final int id, final Marker marker) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                observer.postMarkerMoving(id, marker);
            }
        });
    }

    @Override
    public void postMarkerRemoving(final int id) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                observer.postMarkerRemoving(id);
            }
        });
    }
}
