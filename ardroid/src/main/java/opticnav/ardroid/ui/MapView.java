package opticnav.ardroid.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;

public class MapView extends View {
    private ShapeDrawable mDrawable;

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);

        int x = 10;
        int y = 10;
        int width = 300;
        int height = 300;

        mDrawable = new ShapeDrawable(new OvalShape());
        mDrawable.getPaint().setColor(0x80ff8000);
        mDrawable.setBounds(x, y, x + width, y + height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(50, 50, 20, );
        mDrawable.draw(canvas);
    }
}
