package opticnav.ardroid;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import opticnav.ardroid.connection.ServerUIHandler;
import opticnav.ardroid.ui.MapActivity;
import opticnav.ardroid.ui.ServerConfigActivity;
import opticnav.ardroid.ui.WelcomeActivity;
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

    public final Lifecycle<WelcomeActivity> welcome = new Lifecycle<WelcomeActivity>();
    public final Lifecycle<ServerConfigActivity> serverConfig = new Lifecycle<ServerConfigActivity>();
    public final Lifecycle<MapActivity> map = new Lifecycle<MapActivity>();

    private SharedPreferences prefs;
    private ServerUIHandler serverUIHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        LOG.info("Application created");
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
