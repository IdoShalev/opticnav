package opticnav.ardroid.connection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;
import opticnav.ardd.ard.ARDConnection;
import opticnav.ardd.ard.ARDConnectionException;
import opticnav.ardd.broker.ard.ARDBroker;
import opticnav.ardd.protocol.ConfCode;
import opticnav.ardd.protocol.PassCode;
import opticnav.ardd.protocol.chan.Channel;
import opticnav.ardroid.Application;
import opticnav.ardroid.ui.RegisterARDActivity;
import opticnav.ardroid.ui.WelcomeActivity;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerUIHandler {
    private final Channel channel;
    private final ARDConnection broker;
    private final ExecutorService threadPool;
    private final Context context;

    public ServerUIHandler(Context context, Channel channel) {
        this.context    = context;
        this.channel    = channel;
        this.threadPool = Executors.newCachedThreadPool();
        this.broker     = new ARDBroker(this.channel, this.threadPool);
    }

    public void close() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    broker.close();
                } catch (IOException e) {
                    // XXX
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    public void connect(final PassCode passcode) {
        // if a passcode is stored and connection works, go to the Lobby activity
        // if not, go to the RegisterARD activity to request a passcode

        boolean running = false;

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (passcode == null) {
                    requestCodes();
                } else {
                    connectToLobby(passcode);
                }
                return null;
            }
        }.execute();
    }

    private void connectToLobby(final PassCode passCode) {
    }

    private void showLobby() {
    }

    private void requestCodes() {
        final Handler handler = new Handler(context.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                broker.requestPassConfCodes(new ARDConnection.RequestPassConfCodesCallback() {
                    @Override
                    public void confCode(final ConfCode confCode, ARDConnection.Cancellation cancellation) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(context, RegisterARDActivity.class);
                                intent.putExtra("confcode", confCode.getString());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void registered(PassCode passCode, int ardID) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Registered!", Toast.LENGTH_LONG).show();
                            }
                        });
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
            }
        }).start();
    }
}
