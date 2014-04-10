package opticnav.ardroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.inject.Inject;
import opticnav.ardroid.R;
import opticnav.ardroid.connection.ConnectToServerSingleton;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_welcome)
public class WelcomeActivity extends RoboActivity {
    private static final XLogger LOG = XLoggerFactory.getXLogger(WelcomeActivity.class);

    @Inject
    private ConnectToServerSingleton connectToServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // onClick
    public void configureServer(View view) {
        Intent intent = new Intent(this, ServerConfigActivity.class);
        startActivity(intent);
    }

    // onClick
    public void testingMode(View view) {
        connectToServer.connectWithADBForward();
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