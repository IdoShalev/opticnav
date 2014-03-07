package opticnav.ardroid.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;

public class MapView extends View {
    private ShapeDrawable mDrawable;

    private float x, y;

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode())
        setXY(10, 10);
    }

    public void offsetXY(float x, float y) {
        setXY(this.x + x, this.y + y);
    }

    public void setXY(float x, float y) {
        float density = getResources().getDisplayMetrics().density;

        float width = 50*density;
        float height = 50*density;

        this.x = x;
        this.y = y;

        float sx = x*density;
        float sy = y*density;

        mDrawable = new ShapeDrawable(new OvalShape());
        mDrawable.getPaint().setColor(0x80ff8000);
        mDrawable.setBounds((int)sx, (int)sy, (int)(sx + width), (int)(sy + height));
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mDrawable.draw(canvas);
    }
}
