package opticnav.ardroid.connection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import opticnav.ardd.ard.*;
import opticnav.ardd.broker.ard.ARDBroker;
import opticnav.ardd.protocol.GeoCoordFine;
import opticnav.ardd.protocol.PassCode;
import opticnav.ardd.protocol.chan.Channel;
import opticnav.ardroid.location.LocationMagic;
import opticnav.ardroid.model.MapModel;
import org.apache.commons.io.IOUtils;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ServerManager {
    private static final XLogger LOG = XLoggerFactory.getXLogger(ServerManager.class);

    public interface CommandWithParam<E, P> {
        public E background(P param) throws IOException;
        public void ui(E result) throws Exception;
    }

    public interface ConnectedEvent {
        public void noPassCode();
        public void alreadyConnected();
        public void connected();
    }

    public interface JoinInstanceEvent {
        public void noInstance();
        public void alreadyJoined();
        public void joined();
    }

    public interface DisconnectEvent {
        public void gateKeeperDisconnect();
        public void connectedDisconnect();
        public void instanceDisconnect();
    }

    private final ExecutorService threadPool;
    private final Handler handler;
    private final DisconnectEvent disconnectEvent;
    private ServerCommandQueue gateKeeperCommandQueue;
    private ServerCommandQueue connectedCommandQueue;
    private ServerCommandQueue instanceCommandQueue;
    private ARDGatekeeper gateKeeper;
    private ARDConnected connected;
    private ARDInstance instance;
    private MapModel mapModel;

    public ServerManager(final Handler handler, final DisconnectEvent disconnectEvent) {
        this.threadPool = Executors.newCachedThreadPool();
        this.handler = handler;
        this.disconnectEvent = disconnectEvent;
        init();
    }

    private void init() {
        // yeah, it's bad that this is a method to be recalled. but honestly, Android concurrency BLOWS.
        this.gateKeeper = null;
        this.connected  = null;
        this.instance   = null;
        this.gateKeeperCommandQueue = new ServerCommandQueue(handler, new ServerCommandQueue.FinishedEvent() {
            @Override
            public void finished() {
                if (gateKeeper != null) {
                    IOUtils.closeQuietly(gateKeeper);
                    gateKeeper = null;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            disconnectEvent.gateKeeperDisconnect();
                        }
                    });
                }
            }
        });
        this.connectedCommandQueue = new ServerCommandQueue(handler, new ServerCommandQueue.FinishedEvent() {
            @Override
            public void finished() {
                if (connected != null) {
                    IOUtils.closeQuietly(connected);
                    connected = null;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            disconnectEvent.connectedDisconnect();
                        }
                    });
                }
            }
        });
        this.instanceCommandQueue = new ServerCommandQueue(handler, new ServerCommandQueue.FinishedEvent() {
            @Override
            public void finished() {
                if (instance != null) {
                    IOUtils.closeQuietly(instance);
                    instance = null;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            disconnectEvent.instanceDisconnect();
                        }
                    });
                }
            }
        });

        threadPool.submit(gateKeeperCommandQueue);
        threadPool.submit(connectedCommandQueue);
        threadPool.submit(instanceCommandQueue);
    }

    public void closeConnections() {
        this.instanceCommandQueue.finish();
        this.connectedCommandQueue.finish();
        this.gateKeeperCommandQueue.finish();

        init();
    }

    public void connectToServer(final Channel channel) {
        gateKeeperCommandQueue.enqueue(new ServerCommandQueue.Command<Void>() {
            @Override
            public Void background() throws IOException {
                gateKeeper = new ARDBroker(channel, threadPool);
                return null;
            }

            @Override
            public void ui(Void result) {
            }
        });
    }

    public void connectWithPassCode(final PassCode passCode, final ConnectedEvent connectedEvent) {
        enqueueGatekeeperCommand(new CommandWithParam<ARDConnectionStatus.Status, ARDGatekeeper>() {
            @Override
            public ARDConnectionStatus.Status background(ARDGatekeeper gateKeeper) throws IOException {
                ARDConnectionStatus status = gateKeeper.connect(passCode);
                LOG.debug("ARDConnectionStatus: " + status.getStatus());

                if (status.getStatus() == ARDConnectionStatus.Status.CONNECTED) {
                    connected = status.getConnection();
                }

                return status.getStatus();
            }

            @Override
            public void ui(ARDConnectionStatus.Status result) {
                switch (result) {
                    case NOPASSCODE:
                        connectedEvent.noPassCode();
                        break;
                    case ALREADYCONNECTED:
                        connectedEvent.alreadyConnected();
                        break;
                    case CONNECTED:
                        connectedEvent.connected();
                        break;
                }
            }
        });
    }

    public void joinInstance(final int instanceID, final GeoCoordFine location, final JoinInstanceEvent joinInstanceEvent) {
        enqueueConnectedCommand(new ServerManager.CommandWithParam<ARDInstanceJoinStatus, ARDConnected>() {
            @Override
            public ARDInstanceJoinStatus background(ARDConnected connected) throws IOException {
                return connected.joinInstance(instanceID, location);
            }

            @Override
            public void ui(ARDInstanceJoinStatus status) throws IOException {
                switch (status.getStatus()) {
                    case NOINSTANCE:
                        joinInstanceEvent.noInstance();
                        break;
                    case ALREADYJOINED:
                        joinInstanceEvent.alreadyJoined();
                    case JOINED:
                        setInstance(status.getInstance());

                        joinInstanceEvent.joined();
                        break;
                }
            }
        });
    }

    private void setInstance(ARDInstance instance) throws IOException {
        this.instance = instance;
        InputStream input = this.instance.getMap().getImageInputStream();
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            this.mapModel = new MapModel(bitmap);

            instance.setSubscriber(new InstanceSubscriber(this.mapModel, instance.getMap().getTransform()));
        } finally {
            input.close();
        }
    }

    public <E> void enqueueGatekeeperCommand(final CommandWithParam<E, ARDGatekeeper> cmd) {
        gateKeeperCommandQueue.enqueue(new ServerCommandQueue.Command<E>() {
            @Override
            public E background() throws IOException {
                if (gateKeeper == null) {
                    throw new IOException("Not connected to gatekeeper");
                }
                return cmd.background(gateKeeper);
            }

            @Override
            public void ui(E result) throws Exception {
                cmd.ui(result);
            }
        });
    }

    public <E> void enqueueConnectedCommand(final CommandWithParam<E, ARDConnected> cmd) {
        connectedCommandQueue.enqueue(new ServerCommandQueue.Command<E>() {
            @Override
            public E background() throws IOException {
                if (connected == null) {
                    throw new IOException("Not connected to connected");
                }
                return cmd.background(connected);
            }

            @Override
            public void ui(E result) throws Exception {
                cmd.ui(result);
            }
        });
    }

    public MapModel getMapModel() {
        return mapModel;
    }
}
