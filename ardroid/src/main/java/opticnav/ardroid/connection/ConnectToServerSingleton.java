package opticnav.ardroid.connection;

import android.content.*;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;
import com.google.inject.Singleton;
import opticnav.ardd.ard.ARDGatekeeper;
import opticnav.ardd.protocol.ConfCode;
import opticnav.ardd.protocol.PassCode;
import opticnav.ardd.protocol.Protocol;
import opticnav.ardroid.Application;
import opticnav.ardroid.ui.ConfCodeActivity;
import opticnav.ardroid.ui.InstancesActivity;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * ConnectToServerSingleton is an dependency-injected singleton that manages any ongoing connection requests.
 * This is needed to make the Android activities remember that connections are happening.
 */
@Singleton
public class ConnectToServerSingleton {
    private static final XLogger LOG = XLoggerFactory.getXLogger(ConnectToServerSingleton.class);

    private static final int ADB_PORT = 6666;
    private static final String ACTION_SERVER_CONNECTION = "server-connection";
    private static final String PREFS_SERVERHOST = "serverHost";
    private static final String PREFS_SERVERPORT = "serverPort";
    private static final String PREFS_PASSCODE = "passCode";
    private final SharedPreferences prefs;

    private Application application;
    private BroadcastReceiver broadcastReceiver;
    private boolean connecting = false;

    private class ConnectEvents implements ServerUIHandler.ConnectEvents {
        @Override
        public void connectionError(Exception e) {
            setConnecting(false);
            LOG.catching(e);
            Toast.makeText(application, "Connection error\nReason: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void connectionCancelled() {
            setConnecting(false);
            Toast.makeText(application, "Connection cancelled", Toast.LENGTH_LONG).show();
        }

        @Override
        public void authenticate() {
            setConnecting(false);
            Intent intent = new Intent(application, InstancesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            application.startActivity(intent);
        }

        @Override
        public void confCode(ConfCode confCode, ARDGatekeeper.Cancellation cancellation) {
            setConnecting(false);
            Intent intent = new Intent(application, ConfCodeActivity.class);
            intent.putExtra("confcode", confCode.getString());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            application.startActivity(intent);
        }

        @Override
        public void registered(PassCode passCode) {
            Toast.makeText(application, "Registered!", Toast.LENGTH_LONG).show();
            setPassCode(passCode);
        }

        @Override
        public void couldNotRegister() {

        }

        @Override
        public void confCodeCancelled() {

        }
    }

    public ConnectToServerSingleton() {
        this.application = Application.getInstance();
        this.prefs = application.getSharedPreferences("OpticNav", 0);
    }

    public void subscribe(Context source, BroadcastReceiver receiver) {
        this.broadcastReceiver = receiver;
        LocalBroadcastManager.getInstance(source).registerReceiver(receiver,
                new IntentFilter(ACTION_SERVER_CONNECTION));
    }

    public void unsubscribe(Context source) {
        LocalBroadcastManager.getInstance(source).unregisterReceiver(broadcastReceiver);
    }

    public boolean isConnecting() {
        return connecting;
    }

    private void setConnecting(boolean connecting) {
        this.connecting = connecting;
        LocalBroadcastManager.getInstance(application).sendBroadcast(new Intent(ACTION_SERVER_CONNECTION));
    }

    public void connectWithADBForward() {
        connecting = true;
        application.getServerUIHandler().connectWithADBForward(ADB_PORT, getPassCode(), new ConnectEvents());
    }

    public void connectToServer(final String host, final int port) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREFS_SERVERHOST, host);
        editor.putInt(PREFS_SERVERPORT, port);
        editor.commit();

        connecting = true;
        application.getServerUIHandler().connect(host, port, getPassCode(), new ConnectEvents());
    }

    public String getPreferencesServerHost() {
        return prefs.getString(PREFS_SERVERHOST, "");
    }

    public int getPreferencesServerPort() {
        return prefs.getInt(PREFS_SERVERPORT, Protocol.DEFAULT_ARD_PORT);
    }

    private PassCode getPassCode() {
        final String passCodeString = prefs.getString(PREFS_PASSCODE, null);
        boolean isValid = passCodeString != null && PassCode.isStringCodeValid(passCodeString);
        if (isValid) {
            return new PassCode(passCodeString);
        } else {
            return null;
        }
    }

    public void setPassCode(PassCode passCode) {
        LOG.info("Setting passCode: " + passCode.getString());

        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREFS_PASSCODE, passCode.getString());
        editor.commit();
    }
}
