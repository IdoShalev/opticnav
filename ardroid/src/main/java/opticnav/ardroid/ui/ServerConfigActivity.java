package opticnav.ardroid.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.google.inject.Inject;
import opticnav.ardd.protocol.Protocol;
import opticnav.ardroid.Application;
import opticnav.ardroid.R;
import opticnav.ardroid.connection.CancellableSocket;
import opticnav.ardroid.connection.ServerUIHandler;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_serverconfig)
public class ServerConfigActivity extends RoboActivity {
    private boolean connecting;

    @InjectView(R.id.configureServerHost)
    private EditText configureServerHost;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        final Application app = Application.getInstance();

        this.connecting = false;

        app.serverConfig.onCreate(this);

        if (savedInstanceBundle == null) {
            String host = app.getPreferencesServerHost();
            int port = app.getPreferencesServerPort();
            String portString;

            if (port == Protocol.DEFAULT_ARD_PORT) {
                portString = "";
            } else {
                portString = Integer.toString(port);
            }

            configureServerHost.setText(host);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Application app = (Application)getApplicationContext();
        app.serverConfig.onDestroy(this);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        setConnecting(savedInstanceState.getBoolean("connecting"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("connecting", this.connecting);
    }

    @Override
    public void onBackPressed() {
        Application.getInstance().getServerUIHandler()
                .tryCancelConnection(this, new ServerUIHandler.ConnectionCancelEvent() {
            @Override
            public void cancel() {
                ServerConfigActivity.this.finish();
            }
        });
    }

    // onClick
    public void connect(View view) {
        setConnecting(true);

        String host = configureServerHost.getText().toString();

        Application.getInstance().connectToServer(host, Protocol.DEFAULT_ARD_PORT);
    }

    public void setConnecting(boolean connecting) {
        this.connecting = connecting;
        configureServerHost.setEnabled(!connecting);
        findViewById(R.id.configureServerConnect).setEnabled(!connecting);
        findViewById(R.id.configureServerProgressBar).setVisibility(connecting ? View.VISIBLE : View.INVISIBLE);
    }
}
