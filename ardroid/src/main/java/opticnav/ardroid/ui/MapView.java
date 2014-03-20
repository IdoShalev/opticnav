package opticnav.ardroid.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;
import opticnav.ardroid.opticnav.ardroid.model.MapModel;

import java.io.IOException;
import java.io.InputStream;

public class MapView extends View {
    // TODO - look into SurfaceView
    private Bitmap bitmap;
    private float rotate = 0;

    private MapModel model;

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        try {
        InputStream in = getContext().getAssets().open("saitcampus.png");
        try {
            bitmap = BitmapFactory.decodeStream(in);
        } finally {
            try {in.close();}catch(IOException e){}
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float originX = bitmap.getWidth()/2;
        float originY = bitmap.getHeight()/2;
        Matrix matrix = new Matrix();
        matrix.postTranslate(-originX, -originY);
        matrix.postRotate(rotate);
        matrix.postTranslate(+originX, +originY);
        rotate++;
        canvas.drawBitmap(bitmap, matrix, null);
    }
}
