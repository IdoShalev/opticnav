package opticnav.ardroid.ui;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import opticnav.ardroid.R;

import java.io.IOException;

public class WelcomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // don't include a title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_welcome);

        ImageView opticNavLogo = (ImageView)findViewById(R.id.opticNavLogo);
        try {
            SVG svg = SVG.getFromAsset(getAssets(), "opticnavlogo.svg");
            Drawable drawable = new PictureDrawable(svg.renderToPicture());
            opticNavLogo.setImageDrawable(drawable);
        } catch (SVGParseException e) {
        } catch (IOException e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}