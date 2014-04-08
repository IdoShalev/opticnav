package opticnav.ardroid.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import opticnav.ardroid.Application;
import opticnav.ardroid.R;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.io.IOException;

public class WelcomeActivity extends Activity {
    private static final XLogger LOG = XLoggerFactory.getXLogger(WelcomeActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        try {
            final SVG svg = SVG.getFromAsset(getAssets(), "opticnavlogo.svg");
            final Drawable drawable = new PictureDrawable(svg.renderToPicture());
            final ImageView opticNavLogo = (ImageView)findViewById(R.id.opticNavLogo);
            opticNavLogo.setImageDrawable(drawable);
            opticNavLogo.invalidate();
        } catch (SVGParseException e) {
            LOG.catching(e);
        } catch (IOException e) {
            LOG.catching(e);
        }
    }

    // onClick
    public void configureServer(View view) {
        Intent intent = new Intent(this, ServerConfigActivity.class);
        startActivity(intent);
    }

    // onClick
    public void testingMode(View view) {
        Application.getInstance().connectWithADBForward();
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