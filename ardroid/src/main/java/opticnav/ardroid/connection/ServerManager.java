package opticnav.ardroid.connection;

import android.os.Handler;
import opticnav.ardd.ard.ARDConnected;
import opticnav.ardd.ard.ARDConnectionStatus;
import opticnav.ardd.ard.ARDGatekeeper;
import opticnav.ardd.broker.ard.ARDBroker;
import opticnav.ardd.protocol.PassCode;
import opticnav.ardd.protocol.chan.Channel;
import org.apache.commons.io.IOUtils;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ServerManager {
    private static final XLogger LOG = XLoggerFactory.getXLogger(ServerManager.class);

    public interface CommandWithParam<E, P> {
        public E background(P param) throws IOException;
        public void ui(E result);
    }

    public interface ConnectedEvent {
        public void noPassCode();
        public void alreadyConnected();
        public void connected();
    }

    public interface DisconnectEvent {
        public void gateKeeperDisconnect();
        public void connectedDisconnect();
        public void instanceDisconnect();
    }

    private final ExecutorService threadPool;
    private final ServerCommandQueue gateKeeperCommandQueue;
    private final ServerCommandQueue connectedCommandQueue;
    private ARDGatekeeper gateKeeper;
    private ARDConnected connected;

    public ServerManager(final Handler handler, final DisconnectEvent disconnectEvent) {
        this.threadPool = Executors.newCachedThreadPool();
        this.gateKeeper = null;
        this.connected  = null;
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

        threadPool.submit(gateKeeperCommandQueue);
        threadPool.submit(connectedCommandQueue);
    }

    public void closeConnections() {
        this.connectedCommandQueue.finish();
        this.gateKeeperCommandQueue.finish();
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
                    case NOPASSCODE: connectedEvent.noPassCode(); break;
                    case ALREADYCONNECTED: connectedEvent.alreadyConnected(); break;
                    case CONNECTED: connectedEvent.connected(); break;
                }
            }
        });
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
            public void ui(E result) {
                cmd.ui(result);
            }
        });
    }

    public <E> void enqueueConnectedCommand(final CommandWithParam<E, ARDConnected> cmd) {
        connectedCommandQueue.enqueue(new ServerCommandQueue.Command<E>() {
            @Override
            public E background() throws IOException {
                return cmd.background(connected);
            }

            @Override
            public void ui(E result) {
                cmd.ui(result);
            }
        });
    }
}
