package opticnav.ardroid.ui;

import android.app.Activity;
import android.os.Bundle;
import opticnav.ardroid.Application;
import opticnav.ardroid.R;
import opticnav.ardroid.model.*;
import opticnav.ardroid.ui.marker.MarkerState;

public class MapActivity extends Activity {
    private MapModel mapModel;
    private MapView mapView;
    private Thread timerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        this.mapView = (MapView)findViewById(R.id.map);

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
