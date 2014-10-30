package opticnav.ardroid.connection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Pair;
import opticnav.daemon.device.ARDConnected;
import opticnav.daemon.device.ARDGatekeeper;
import opticnav.daemon.device.InstanceInfo;
import opticnav.daemon.protocol.ConfCode;
import opticnav.daemon.protocol.PassCode;
import opticnav.daemon.protocol.chan.Channel;
import opticnav.daemon.protocol.chan.ChannelUtil;
import opticnav.ardroid.model.MapModel;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;

/**
 * ServerUIHandler is used by Activities in the application to summon Server tasks.
 *
 * ServerUIHandler cooperates with the Android UI thread, but never directly with specific Activities in the
 * application. Callbacks are passed by the caller (usually an Activity) to ServerUIHandler methods to achieve this.
 */
public class ServerUIHandler {
    private static final XLogger LOG = XLoggerFactory.getXLogger(ServerUIHandler.class);

    /** The server connection timeout value in milliseconds */
    private static final int CONNECTION_TIMEOUT = 10 * 1000;

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

        public void confCode(final ConfCode confCode, ARDGatekeeper.Cancellation cancellation);
        public void registered(final PassCode passCode);
        public void couldNotRegister();
        public void confCodeCancelled();
    }

    public interface OnDisconnect {
        /** The server is disconnected. onDisconnect() runs on the Android UI thread. */
        public void onDisconnect();
    }

    public interface ListInstancesEvent {
        /** The list of actively running instances is received. listInstances() runs on the Android UI thread. */
        public void listInstances(List<InstanceInfo> list);
    }

    public interface JoinInstanceEvent {
        public void noInstance();
        public void alreadyJoined();
        public void joined();
    }

    private final Context context;
    private final OnDisconnect onDisconnect;
    private final ServerManager serverManager;
    private SynchronizedOptional<CancellableSocket.Cancellable> cancellableSocket;

    public ServerUIHandler(Context context, final OnDisconnect onDisconnect) {
        this.context = context;
        this.serverManager = new ServerManager(new Handler(context.getMainLooper()), new ServerManager.DisconnectEvent() {
            @Override
            public void gateKeeperDisconnect() {
                LOG.trace("gateKeeperDisconnect()");
                onDisconnect.onDisconnect();
            }

            @Override
            public void connectedDisconnect() {
                LOG.trace("connectedDisconnect()");
            }

            @Override
            public void instanceDisconnect() {
                LOG.trace("instanceDisconnect()");
            }
        });
        this.cancellableSocket = new SynchronizedOptional<CancellableSocket.Cancellable>();
        this.onDisconnect = onDisconnect;
    }

    /**
     * Connect using a listening port established by "adb forward". This is weird in that the device acts as a
     * "server" by accepting connections instead of being the client. However, this is needed in order to test the
     * application on devices without WiFi.
     *
     * @param port The listening port on this device
     * @param passCode
     * @param connectEvents
     */
    public void connectWithADBForward(final int port, final PassCode passCode, final ConnectEvents connectEvents) {
        final Handler handler = new Handler(context.getMainLooper());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ServerSocket serverSocket = new ServerSocket(port);
                    final Socket socket;
                    try {
                        serverSocket.setSoTimeout(10000);
                        socket = serverSocket.accept();
                    } finally {
                        serverSocket.close();
                    }

                    connectWithSocket(socket, passCode, connectEvents);
                } catch (final IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            connectEvents.connectionError(e);
                        }
                    });
                }
            }
        }).start();
    }

    public void connect(final String host, final int port, final PassCode passCode,
                                                 final ConnectEvents connectEvents) {
        // if a passcode is stored and connection works, go to the Lobby activity
        // if not, go to the RegisterARD activity to request a passcode

        new Thread(new Runnable() {
            @Override
            public void run() {
                final SocketAddress addr = new InetSocketAddress(host, port);
                final Handler handler = new Handler(context.getMainLooper());

                cancellableSocket.set(CancellableSocket.connect(addr, CONNECTION_TIMEOUT, new CancellableSocket.ConnectionEvent() {
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
                            }
                        });
                    }

                    @Override
                    public void connected(Socket socket) {
                        try {
                            connectWithSocket(socket, passCode, connectEvents);
                        } catch (IOException e) {
                            error(e);
                        }
                    }
                }));
            }
        }).start();
    }

    private void connectWithSocket(Socket socket, final PassCode passCode, final ConnectEvents connectEvents)
            throws IOException {
        try {
            Channel channel = ChannelUtil.fromSocket(socket);
            serverManager.connectToServer(channel);
            LOG.info("Connected to server");
            ServerUIHandler.this.cancellableSocket.empty();

            tryConnectToLobby(passCode, connectEvents);
        } catch (IOException e) {
            serverManager.closeConnections();
            throw e;
        }
    }

    public void cancelConnection() {
        if (this.cancellableSocket.isPresent()) {
            final Pair<Boolean, CancellableSocket.Cancellable> cancellable;
            cancellable = ServerUIHandler.this.cancellableSocket.getIfPresent();

            // If a Cancellable is found, cancel it
            if (cancellable.first) {
                ServerUIHandler.this.cancellableSocket.empty();
                cancellable.second.cancel();
            }
        }
    }

    public void tryDisconnect(final Activity source) {
        new AlertDialog.Builder(source)
                .setMessage("You are currently connected to a server. Are you sure you want to disconnect?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        serverManager.closeConnections();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    
    public void listInstances(final ListInstancesEvent listInstancesEvent) {
        // runs in UI thread

        serverManager.enqueueConnectedCommand(new ServerManager.CommandWithParam<List<InstanceInfo>, ARDConnected>() {
            @Override
            public List<InstanceInfo> background(ARDConnected connected) throws IOException {
                return connected.listInstances();
            }

            @Override
            public void ui(List<InstanceInfo> list) {
                listInstancesEvent.listInstances(list);
            }
        });
    }

    public void joinInstance(final int instanceID, final JoinInstanceEvent joinInstanceEvent) {
        // runs in UI thread
        serverManager.joinInstance(instanceID, new ServerManager.JoinInstanceEvent() {
            @Override
            public void noInstance() {
                joinInstanceEvent.noInstance();
            }

            @Override
            public void alreadyJoined() {
                joinInstanceEvent.alreadyJoined();
            }

            @Override
            public void joined() {
                joinInstanceEvent.joined();
            }
        });
    }

    private void tryConnectToLobby(final PassCode passCode, final ConnectEvents connectEvents) {
        // runs in background thread

        if (passCode == null) {
            LOG.info("No passCode provided - requesting one...");
            requestCodes(connectEvents);
        } else {
            LOG.info("Using passCode: " + passCode.getString());

            final Handler handler = new Handler(context.getMainLooper());
            serverManager.connectWithPassCode(passCode, new ServerManager.ConnectedEvent() {
                @Override
                public void noPassCode() {
                    LOG.info("Could not authenticate with passCode - requesting one...");
                    requestCodes(connectEvents);
                }

                @Override
                public void alreadyConnected() {
                    LOG.info("Device with the same passCode already connected");
                    serverManager.closeConnections();
                }

                @Override
                public void connected() {
                    LOG.info("Authenticated with passCode successfully");
                    connectEvents.authenticate();
                }
            });
        }
    }

    private void requestCodes(final ConnectEvents connectEvents) {
        // runs in background thread

        final Handler handler = new Handler(context.getMainLooper());
        serverManager.enqueueGatekeeperCommand(new ServerManager.CommandWithParam<Void, ARDGatekeeper>() {
            @Override
            public Void background(ARDGatekeeper gateKeeper) throws IOException {
                gateKeeper.requestPassConfCodes(new ARDGatekeeper.RequestPassConfCodesCallback() {
                    @Override
                    public void confCode(final ConfCode confCode, final ARDGatekeeper.Cancellation cancellation) {
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
                        tryConnectToLobby(passCode, connectEvents);
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
                return null;
            }

            @Override
            public void ui(Void result) {
            }
        });
    }

    public MapModel getMapModel() {
        return this.serverManager.getMapModel();
    }
}
