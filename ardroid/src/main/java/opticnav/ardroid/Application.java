package opticnav.ardroid;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;
import opticnav.ardd.ard.ARDGatekeeper;
import opticnav.ardd.protocol.ConfCode;
import opticnav.ardd.protocol.PassCode;
import opticnav.ardd.protocol.Protocol;
import opticnav.ardroid.connection.ServerUIHandler;
import opticnav.ardroid.ui.*;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * Because Android development is brain damaged, it's very hard to keep the same state in Activitys during changes
 * within the Activity lifecycle (creation and destruction due to rotating the screen, and many more).
 *
 * _Bad_ suggestions to address the problem: Setting android:configChanges, using Services, using Fragments,
 * and anything else that mandates confusing flow or terrifying amounts of boilerplate code.
 *
 * Any variables that need to be persistent in the same Activity as it goes through lifecycle changes are kept here.
 * It's essentially global state, but it just goes to show how broken Android is. :/
 *
 * This class is not thread safe! Only use this class from the UI thread.
 */
public class Application extends android.app.Application {
    private static final XLogger LOG = XLoggerFactory.getXLogger(Application.class);

    public static class Lifecycle<T extends Activity> {
        private T activity = null;

        public void onCreate(T activity) {
            if (this.activity != null) {
                throw new IllegalStateException("Activity already created");
            }
            this.activity = activity;
        }
        
        public void onDestroy(T activity) {
            this.activity = null;
        }

        public T getActivity() {
            return this.activity;
        }
    }

    /** evil singleton variable. Why is Android so stupid? */
    private static Application context;

    /** evil singleton method. */
    public static Application getInstance() {
        return context;
    }

    private static final int ADB_PORT = 6666;
    private static final String PREFS_SERVERHOST = "serverHost";
    private static final String PREFS_SERVERPORT = "serverPort";
    private static final String PREFS_PASSCODE = "passCode";

    public final Lifecycle<WelcomeActivity> welcome = new Lifecycle<WelcomeActivity>();
    public final Lifecycle<ServerConfigActivity> serverConfig = new Lifecycle<ServerConfigActivity>();
    public final Lifecycle<MapActivity> map = new Lifecycle<MapActivity>();

    private SharedPreferences prefs;
    private ServerUIHandler serverUIHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        LOG.info("Application created");
        this.prefs = getSharedPreferences("OpticNav", 0);
        this.context = this;
        this.serverUIHandler = new ServerUIHandler(this, new ServerUIHandler.OnDisconnect() {
            @Override
            public void onDisconnect() {
                // Clear the back stack up to the Welcome activity
                Intent intent = new Intent(context, WelcomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    public ServerUIHandler getServerUIHandler() {
        return this.serverUIHandler;
    }

    private class ConnectEvents implements ServerUIHandler.ConnectEvents {
        @Override
        public void connectionError(Exception e) {
            LOG.catching(e);
            Toast.makeText(context, "Connection error\nReason: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void connectionCancelled() {
            Toast.makeText(context, "Connection cancelled", Toast.LENGTH_LONG).show();
        }

        @Override
        public void authenticate() {
            Intent intent = new Intent(context, InstancesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        @Override
        public void confCode(ConfCode confCode, ARDGatekeeper.Cancellation cancellation) {
            Intent intent = new Intent(context, ConfCodeActivity.class);
            intent.putExtra("confcode", confCode.getString());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        @Override
        public void registered(PassCode passCode) {
            Toast.makeText(context, "Registered!", Toast.LENGTH_LONG).show();
            setPassCode(passCode);
        }

        @Override
        public void couldNotRegister() {

        }

        @Override
        public void confCodeCancelled() {

        }
    }

    public void connectWithADBForward() {
        this.serverUIHandler.connectWithADBForward(ADB_PORT, getPassCode(), new ConnectEvents());
    }

    public void connectToServer(final String host, final int port) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREFS_SERVERHOST, host);
        editor.putInt(PREFS_SERVERPORT, port);
        editor.commit();

        this.serverUIHandler.connect(host, port, getPassCode(), new ConnectEvents());
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

    private void setPassCode(PassCode passCode) {
        LOG.info("Setting passCode: " + passCode.getString());

        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREFS_PASSCODE, passCode.getString());
        editor.commit();
    }

    private void createConnectionNotification() {
        NotificationManager notMgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // TODO - a better way?
        if (Build.VERSION.SDK_INT >= 16) {
            /* notification builder only works for android SDK 16+ */

            Intent intent = new Intent(this, MapActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

            Notification not = new Notification.Builder(this)
                    .setContentTitle("Connected")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(pIntent)
                    .build();

            not.flags |= Notification.FLAG_NO_CLEAR;

            notMgr.notify(1, not);
        }
    }
}
