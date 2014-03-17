package opticnav.ardroid.ui;

import android.app.Activity;
import android.os.Bundle;
import opticnav.ardroid.R;

public class MapActivity extends Activity {
    private MapView mapView;
    private Thread timerThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        this.mapView = (MapView)findViewById(R.id.map);
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
                                mapView.invalidate();
                            }
                        });
                        Thread.sleep(1000/60);
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
