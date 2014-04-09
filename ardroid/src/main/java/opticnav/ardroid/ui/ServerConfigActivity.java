package opticnav.ardroid.ui;

import android.app.ProgressDialog;
import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.google.inject.Inject;
import opticnav.ardd.protocol.Protocol;
import opticnav.ardroid.Application;
import opticnav.ardroid.R;
import opticnav.ardroid.connection.ConnectToServerSingleton;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_serverconfig)
public class ServerConfigActivity extends RoboActivity {
    @InjectView(R.id.configureServerHost)
    private EditText configureServerHost;

    @Inject
    private ConnectToServerSingleton connectToServer;

    private ProgressDialog connectingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);

        if (savedInstanceBundle == null) {
            String host = connectToServer.getPreferencesServerHost();

            configureServerHost.setText(host);
        }

        setConnecting();

        connectToServer.subscribe(this, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setConnecting();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Application app = (Application)getApplicationContext();

        connectToServer.unsubscribe(this);
    }

    // onClick
    public void connect(View view) {
        String host = configureServerHost.getText().toString();

        connectToServer.connectToServer(host, Protocol.DEFAULT_ARD_PORT);
        setConnecting();
    }

    public void setConnecting() {
        if (connectToServer.isConnecting()) {
            connectingDialog = ProgressDialog.show(this, "", "Connecting to server...", true, true, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    Application.getInstance().getServerUIHandler().cancelConnection();
                }
            });
        } else if (connectingDialog != null) {
            connectingDialog.hide();
        }
    }
}
