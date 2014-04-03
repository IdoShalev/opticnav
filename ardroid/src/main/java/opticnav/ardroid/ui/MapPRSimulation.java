package opticnav.ardroid.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import opticnav.ardroid.model.*;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class MapPRSimulation implements Runnable {
    private final Bitmap bitmap;
    private final MapModel mapModel;

    public MapPRSimulation(Context context) throws IOException {
        InputStream in = context.getAssets().open("saitcampus.png");
        try {
            bitmap = BitmapFactory.decodeStream(in);
        } finally {
            IOUtils.closeQuietly(in);
        }

        mapModel = new MapModel(bitmap);
    }

    @Override
    public void run() {
        final Random random = new Random(32438905534534353L);

        try {
            int markers = 0;
        for (int i = 0; i < 150; i++) {
            int mode = random.nextInt(10);
            int x = random.nextInt(900);
            int y = random.nextInt(900);
            final Coordinate coord = new Coordinate(x, y);
            System.out.println(x+", "+y);
            if (mode == 0) {
                // create
                mapModel.addMarker(markers, new Marker("test-"+markers, coord));
                markers++;
            } else if (mode == 1) {
                // create with direction
                float direction = random.nextFloat();
                mapModel.addMarker(markers, new Marker("test-"+markers, coord, direction));
                markers++;
            } else if (mode == 2) {
                // remove marker
                int num = random.nextInt(markers+1);
                if (mapModel.getMarkerList().get(num) != null)
                mapModel.removeMarker(num);
            } else {
                // move marker
                float direction = random.nextFloat();
                int num = random.nextInt(markers+1);
                Marker marker = mapModel.getMarkerList().get(num);
                if (marker != null)
                    if (marker.hasDirection()) {
                        mapModel.moveMarker(num, coord, direction);
                    } else {
                        mapModel.moveMarker(num, coord);
                    }
            }
            Thread.sleep(100);
        }
        } catch (InterruptedException e) {
        }
    }

    public MapModel getModel() {
        return this.mapModel;
    }
}
