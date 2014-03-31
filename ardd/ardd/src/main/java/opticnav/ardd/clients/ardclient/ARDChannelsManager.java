package opticnav.ardd.clients.ardclient;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import opticnav.ardd.ARDConnection;
import opticnav.ardd.ARDListsManager;
import opticnav.ardd.clients.ClientCommandDispatcher;
import opticnav.ardd.protocol.chan.Channel;
import opticnav.ardd.protocol.chan.ChannelMultiplexer;
import opticnav.ardd.protocol.consts.ARDdARDProtocol;

public class ARDChannelsManager implements Callable<Void> {
    private final ExecutorService threadPool;
    private final Channel channel;
    private final ChannelMultiplexer mpxr;
    private final ChannelMultiplexer.Listener listener;

    private final Channel gatekeeper;
    private final ClientCommandDispatcher gatekeeperConn;

    private ARDConnection connection;
    private Channel connectedChannel;
    private ClientCommandDispatcher connectedCommandDispatcher;
    
    public ARDChannelsManager(Channel channel, ExecutorService threadPool,
            ARDListsManager ardListsManager) {
        this.threadPool = threadPool;
        this.channel = channel;
        
        this.mpxr = new ChannelMultiplexer(this.channel);
        this.gatekeeper = this.mpxr.createChannel(ARDdARDProtocol.Channels.GATEKEEPER);
        
        this.gatekeeperConn = new ClientCommandDispatcher(this.gatekeeper,
                new GatekeeperCommandHandler(ardListsManager, this));
        
        this.listener = this.mpxr.createListener();
    }
    
    public void startConnection(ARDConnection connection) {
        this.connection = connection;
        this.connectedChannel = this.mpxr.createChannel(ARDdARDProtocol.Channels.CONNECTED);
        
        this.connectedCommandDispatcher =
                new ClientCommandDispatcher(this.connectedChannel, new ConnectedCommandHandler());
    }

    @Override
    public Void call() throws Exception {

        Future<Void> gk = this.threadPool.submit(this.gatekeeperConn);
        Future<Void> listenerResult = this.threadPool.submit(listener);
        
        try {        
            listenerResult.get();
            gk.get();
        } finally {
            // In all cases, any connected should be closed
            if (this.connection != null) {
                this.connection.close();
            }
        }
        
        return null;
    }
}
