package opticnav.ardroid.ui;

import android.app.Activity;
import android.content.Context;
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
import opticnav.ardroid.connection.ServerUIHandler;

import java.io.IOException;

public class WelcomeActivity extends Activity {
    private Application.Lifecycle<WelcomeActivity> lifecycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        this.lifecycle = ((Application)getApplicationContext()).welcome;
        lifecycle.onCreate(this);

        ImageView opticNavLogo = (ImageView)findViewById(R.id.opticNavLogo);
        try {
            SVG svg = SVG.getFromAsset(getAssets(), "opticnavlogo.svg");
            Drawable drawable = new PictureDrawable(svg.renderToPicture());
            opticNavLogo.setImageDrawable(drawable);
        } catch (SVGParseException e) {
        } catch (IOException e) {
        }

        Button detectServer = (Button)findViewById(R.id.detectServer);
        detectServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        Button configureServer = (Button)findViewById(R.id.configureServer);
        configureServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, ServerConfigActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lifecycle.onDestroy(this);
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