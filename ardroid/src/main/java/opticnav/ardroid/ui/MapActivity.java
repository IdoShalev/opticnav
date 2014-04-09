package opticnav.ardroid.ui;

import android.os.Bundle;
import opticnav.ardroid.Application;
import opticnav.ardroid.R;
import opticnav.ardroid.model.MapModel;
import opticnav.ardroid.model.MapModelObserverAsync;
import opticnav.ardroid.ui.marker.MarkerState;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_map)
public class MapActivity extends RoboActivity {
    private MapModel mapModel;
    private Thread timerThread;

    @InjectView(R.id.map)
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mapModel = Application.getInstance().getServerUIHandler().getMapModel();
        this.mapModel.setObserver(new MapModelObserverAsync(this, mapView));
        this.mapView.setModel(mapModel);
    }

    @Override
    protected void onDestroy() {
        mapModel.unsetObserver();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // create update timer
        this.timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        mapView.post(new Runnable() {
                            @Override
                            public void run() {
                                mapView.step();
                            }
                        });
                        Thread.sleep(1000/MarkerState.FPS);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.timerThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // destroy update timer
        if (this.timerThread != null) {
            this.timerThread.interrupt();
            try {
                this.timerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.timerThread = null;
        }
    }
}
