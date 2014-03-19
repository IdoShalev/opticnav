package opticnav.ardroid.connection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import opticnav.ardd.ard.ARDConnection;
import opticnav.ardd.ard.ARDConnectionException;
import opticnav.ardd.broker.ard.ARDBroker;
import opticnav.ardd.protocol.ConfCode;
import opticnav.ardd.protocol.PassCode;
import opticnav.ardd.protocol.chan.Channel;
import opticnav.ardroid.ui.RegisterARDActivity;
import opticnav.ardroid.ui.WelcomeActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerUIHandler {
    private final Channel channel;
    private final ARDBroker broker;
    private final ExecutorService threadPool;
    private final Context context;

    public ServerUIHandler(Context context, Channel channel) {
        this.context    = context;
        this.channel    = channel;
        this.threadPool = Executors.newCachedThreadPool();
        this.broker     = null;//new ARDBroker(this.channel, this.threadPool);
    }

    public void connect(final WelcomeActivity source) {
        // if a passcode is stored and connection works, go to the Lobby activity
        // if not, go to the RegisterARD activity to request a passcode

        boolean running = false;

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                Intent intent = new Intent(source, RegisterARDActivity.class);
                intent.putExtra("confcode", "AABBCCDD");
                source.startActivity(intent);
            }
        }.execute();
    }

    public void requestCodes() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                broker.requestPassConfCodes(new ARDConnection.RequestPassConfCodesCallback() {
                    @Override
                    public void confCode(ConfCode confCode, ARDConnection.Cancellation cancellation) {

                    }

                    @Override
                    public void registered(PassCode passCode, int ardID) {

                    }

                    @Override
                    public void couldnotregister() {

                    }

                    @Override
                    public void cancelled() {

                    }
                });
                } catch (ARDConnectionException e) {

                }
                return null;
            }
        }.execute();
    }
}
