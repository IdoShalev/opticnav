package opticnav.ardd.connections;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import opticnav.ardd.ARDListsManager;
import opticnav.ardd.protocol.PassCode;
import opticnav.ardd.protocol.Protocol;
import opticnav.ardd.protocol.chan.Channel;
import opticnav.ardd.protocol.chan.ChannelMultiplexer;

public class ARDChannelsManager implements Runnable {
    private ExecutorService threadPool;
    private Channel channel;
    private ChannelMultiplexer mpxr;
    private ChannelMultiplexer.Listener listener;

    private Channel gatekeeper;
    private ClientConnection gatekeeperConn;

    private Channel lobby;
    private ClientConnection lobbyConn;

    public ARDChannelsManager(Channel channel, ARDListsManager ardListsManager) {
        this.threadPool = Executors.newCachedThreadPool();
        this.channel = channel;
        
        this.mpxr = new ChannelMultiplexer(this.channel);
        this.gatekeeper = this.mpxr.createChannel(Protocol.ARDClient.CHANNEL_GATEKEEPER);
        
        this.gatekeeperConn = new ClientConnection(this.gatekeeper,
                new ARDClientGatekeeperCommandHandler(ardListsManager, this));
        
        this.listener = this.mpxr.createListener();
    }

    /**
     *
     * @param passCode
     * @return True if there's no existing lobby connection, false if there is one
     */
    public boolean startLobbyConnection(PassCode passCode) {
        if (this.lobby != null) {
            return false;
        } else {
            this.lobby = this.mpxr.createChannel(Protocol.ARDClient.CHANNEL_LOBBY);
            this.lobbyConn = new ClientConnection(this.lobby, new ARDClientLobbyConnectionHandler());
            return true;
        }
    }

    @Override
    public void run() {
        try {
            Thread gk = new Thread(this.gatekeeperConn);
            gk.start();
            Future<Void> listenerResult = this.threadPool.submit(listener);
            
            listenerResult.get();
            gk.join();
        } catch (Exception e) {
            // TODO - log this
            e.printStackTrace();
        }
    }
}
