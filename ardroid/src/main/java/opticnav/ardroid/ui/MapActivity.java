package opticnav.ardroid.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import opticnav.ardroid.R;
import opticnav.ardroid.model.*;
import opticnav.ardroid.ui.marker.MarkerState;
import org.apache.commons.io.IOUtils;

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
                IOUtils.closeQuietly(in);
            }

            // hard-coded anchor points for SAIT
            final int[] local = {463,346, 714,409, 937,200};
            final int[] internalGPS = {-41072424,18383378, -41071669,18383259, -41071005,18383656};
            final Anchor[] anchors = new Anchor[3];

            for (int i = 0; i < anchors.length; i++) {
                anchors[i] = new Anchor(local[i*2+0], local[i*2+1], GPSCoordinate.fromInternalInt(internalGPS[i*2+0], internalGPS[i*2+1]));
            }

            final MapTransform mapTransform = new MapTransform(anchors[0], anchors[1], anchors[2]);

            mapModel = new MapModel(bitmap);
            mapModel.setObserver(new MapModelObserverAsync(this, mapView));
            this.mapView.setModel(mapModel);

            mapModelUpdateThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mapModel.addMarker(0, new Marker("Test1", new Coordinate(100, 100), 0.0f));
                        Thread.sleep(500);
                        mapModel.addMarker(1, new Marker("Test2", new Coordinate(50, 150)));
                        mapModel.moveMarker(0, new Coordinate(50, 240), 0.15f);
                        Thread.sleep(1000);
                        mapModel.moveMarker(0, new Coordinate(200, 120), 0.55f);
                        Thread.sleep(2000);
                        mapModel.moveMarker(1, new Coordinate(300, 300));
                        Thread.sleep(3000);
                        mapModel.removeMarker(1);
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
