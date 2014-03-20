package opticnav.ardroid.opticnav.ardroid.model;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.util.Map;

public class MapModel {
    private Bitmap bitmap;

    private Map<Integer, Marker> markerList;
    private Matrix gpsToImageTransform;
}
