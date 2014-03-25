package opticnav.ardroid.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import opticnav.ardd.protocol.Protocol;
import opticnav.ardroid.Application;
import opticnav.ardroid.R;
import opticnav.ardroid.connection.CancellableSocket;
import opticnav.ardroid.connection.ServerUIHandler;

public class ServerConfigActivity extends Activity {
    private boolean connecting;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        final Application app = Application.getInstance();

        setContentView(R.layout.activity_serverconfig);
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

            ((EditText)findViewById(R.id.configureServerHost)).setText(host);
            ((EditText)findViewById(R.id.configureServerPort)).setText(portString);
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

        String host = ((EditText)findViewById(R.id.configureServerHost)).getText().toString();
        String portText = ((EditText)findViewById(R.id.configureServerPort)).getText().toString();
        int port;
        if (portText.isEmpty()) {
            port = Protocol.DEFAULT_ARD_PORT;
        } else {
            port = Integer.parseInt(portText);
        }

        Application.getInstance().connectToServer(host, port);
    }

    public void setConnecting(boolean connecting) {
        this.connecting = connecting;
        findViewById(R.id.configureServerHost).setEnabled(!connecting);
        findViewById(R.id.configureServerPort).setEnabled(!connecting);
        findViewById(R.id.configureServerConnect).setEnabled(!connecting);
        findViewById(R.id.configureServerProgressBar).setVisibility(connecting ? View.VISIBLE : View.INVISIBLE);
    }
}
