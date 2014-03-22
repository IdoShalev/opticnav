package opticnav.ardroid.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import opticnav.ardroid.R;
import opticnav.ardroid.model.*;
import opticnav.ardroid.ui.marker.MarkerState;

import java.io.IOException;
import java.io.InputStream;

public class MapActivity extends Activity {
    private MapView mapView;
    private Thread timerThread;
    private Thread mapModelUpdateThread;
    private MapModel mapModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        this.mapView = (MapView)findViewById(R.id.map);

        Bitmap bitmap;

        try {
            InputStream in = getAssets().open("saitcampus.png");
            try {
                bitmap = BitmapFactory.decodeStream(in);
            } finally {
                try {in.close();}catch(IOException e){}
            }

            mapModel = new MapModel(bitmap);
            mapModel.setObserver(new MapModelObserverAsync(this, mapView));
            this.mapView.setModel(mapModel);

            mapModelUpdateThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mapModel.addMarker(0, new Marker("Test1", new Coordinate(100, 100)));
                        Thread.sleep(500);
                        mapModel.addMarker(1, new Marker("Test2", new Coordinate(50, 150)));
                        mapModel.moveMarker(0, new Coordinate(50, 240));
                        Thread.sleep(1000);
                        mapModel.moveMarker(0, new Coordinate(200, 120));
                        Thread.sleep(2000);
                        mapModel.removeMarker(0);
                        mapModel.moveMarker(1, new Coordinate(300, 300));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            mapModelUpdateThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapModelUpdateThread.interrupt();
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
