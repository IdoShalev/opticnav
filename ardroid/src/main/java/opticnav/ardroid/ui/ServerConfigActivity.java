package opticnav.ardroid.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import opticnav.ardd.protocol.Protocol;
import opticnav.ardroid.Application;
import opticnav.ardroid.R;
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

    // onClick
    public void connect(View view) {
        setConnecting(true);

        String host = configureServerHost.getText().toString();

        Application.getInstance().connectToServer(host, Protocol.DEFAULT_ARD_PORT);
    }

    public void setConnecting(boolean connecting) {
        this.connecting = connecting;
        if (connecting) {
            ProgressDialog.show(this, "", "Connecting to server...", true, true, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    Application.getInstance().getServerUIHandler().cancelConnection();
                }
            });
        }
    }
}
