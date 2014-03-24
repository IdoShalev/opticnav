package opticnav.ardroid.connection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;
import opticnav.ardd.ard.ARDConnection;
import opticnav.ardd.ard.ARDConnectionException;
import opticnav.ardd.ard.ARDLobbyConnection;
import opticnav.ardd.broker.ard.ARDBroker;
import opticnav.ardd.protocol.ConfCode;
import opticnav.ardd.protocol.PassCode;
import opticnav.ardd.protocol.chan.Channel;
import opticnav.ardd.protocol.chan.ChannelUtil;
import opticnav.ardroid.ui.RegisterARDActivity;
import opticnav.ardroid.ui.WelcomeActivity;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ServerUIHandler is used by Activities in the application to summon Server tasks.
 *
 * ServerUIHandler cooperates with the Android UI thread, but never directly with specific Activities in the
 * application. Callbacks are passed by the caller (usually an Activity) to ServerUIHandler methods to achieve this.
 */
public class ServerUIHandler {
    /** The server connection timeout value in milliseconds */
    private static final int CONNECTION_TIMEOUT = 10 * 1000;
    /**
     * Server is separated from any Android UI-isms and has no UI code.
     */
    private static class Server {
        public final ARDConnection broker;

        private final Channel channel;
        private final ExecutorService threadPool;

        public Server(Channel channel) {
            this.channel    = channel;
            this.threadPool = Executors.newCachedThreadPool();
            this.broker     = new ARDBroker(channel, threadPool);
        }
    }

    /**
     * The ConnectEvents interface resembles ARDConnection.RequestPassConfCodesCallback.
     *
     * This interface is scoped inside ServerUIHandler to keep the API consistent.
     * All methods are run on the Android UI thread.
     */
    public interface ConnectEvents {
        /** There was an error. connectionError() runs on the Android UI thread.
         *
         * @return The exception thrown by the connection (usually a SocketException or IOException)
         * */
        public void connectionError(Exception e);
        /** The user cancelled connection. onCancel() runs on the Android UI thread. */
        public void connectionCancelled();
        /** The server authenticated the device successfully. authenticate() runs on the Android UI thread. */
        public void authenticate();

        public void confCode(final ConfCode confCode, ARDConnection.Cancellation cancellation);
        public void registered(final PassCode passCode);
        public void couldNotRegister();
        public void confCodeCancelled();
    }

    public interface OnDisconnect {
        /** The server is disconnected. onDisconnect() runs on the Android UI thread. */
        public void onDisconnect();
    }

    private final Context context;
    private final OnDisconnect onDisconnect;
    private SynchronizedOptional<Server> server;

    public ServerUIHandler(Context context, OnDisconnect onDisconnect) {
        this.context    = context;
        this.server = new SynchronizedOptional<Server>();
        this.onDisconnect = onDisconnect;
    }

    public CancellableSocket.Cancellable connect(String host, int port, final PassCode passCode,
                                                 final ConnectEvents connectEvents) {
        // if a passcode is stored and connection works, go to the Lobby activity
        // if not, go to the RegisterARD activity to request a passcode

        final SocketAddress addr = new InetSocketAddress(host, port);
        final Handler handler = new Handler(context.getMainLooper());

        return CancellableSocket.connect(addr, CONNECTION_TIMEOUT, new CancellableSocket.ConnectionEvent() {
            @Override
            public void cancelled() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        connectEvents.connectionCancelled();
                    }
                });
            }

            @Override
            public void error(final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        connectEvents.connectionError(e);
                        //Toast.makeText(context, "Could not connect\nReason: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void connected(Socket socket) {
                try {
                    Channel channel = ChannelUtil.fromSocket(socket);
                    ServerUIHandler.this.server.set(new Server(channel));

                    tryConnectToLobby(passCode, connectEvents);
                } catch (IOException e) {
                    error(e);
                }
            }
        });
    }

    public void tryDisconnect(final Activity source) {
        if (!this.server.isPresent()) {
            // If this method is being called in this condition, chances are there's a race condition
            // (boo) and the UI should do as it normally would
            onDisconnect.onDisconnect();
            return;
        }

        new AlertDialog.Builder(source)
                .setMessage("You are currently connected to a server. Are you sure you want to disconnect?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                disconnect();
                            }
                        }).start();
                        onDisconnect.onDisconnect();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void disconnect() {
        // runs in background thread

        Server server = this.server.getAndEmpty();
        try {
            server.broker.close();
        } catch (IOException e) {
            // TODO - log somewhere
            e.printStackTrace();
        }
    }


    private void tryConnectToLobby(final PassCode passCode, final ConnectEvents connectEvents)
            throws ARDConnectionException {
        // runs in background thread

        if (passCode == null) {
            requestCodes(connectEvents);
        } else {
            Server server = this.server.get();

            ARDLobbyConnection conn = server.broker.connectToLobby(passCode);
            if (conn == null) {
                // could not authenticate with passCode
                requestCodes(connectEvents);
            } else {
                // authenticated successfully
                final Handler handler = new Handler(context.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        connectEvents.authenticate();
                    }
                });
            }
        }
    }

    private void requestCodes(final ConnectEvents connectEvents) throws ARDConnectionException {
        // runs in background thread

        final Handler handler = new Handler(context.getMainLooper());
        Server server = this.server.get();
        server.broker.requestPassConfCodes(new ARDConnection.RequestPassConfCodesCallback() {
            @Override
            public void confCode(final ConfCode confCode, final ARDConnection.Cancellation cancellation) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        connectEvents.confCode(confCode, cancellation);
                    }
                });
            }

            @Override
            public void registered(final PassCode passCode) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        connectEvents.registered(passCode);
                    }
                });
            }

            @Override
            public void couldNotRegister() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        connectEvents.couldNotRegister();
                    }
                });
            }

            @Override
            public void cancelled() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        connectEvents.confCodeCancelled();
                    }
                });
            }
        });
    }
}
