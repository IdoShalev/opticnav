package opticnav.ardd.connections;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import opticnav.ardd.ARDListsManager;
import opticnav.ardd.protocol.chan.Channel;
import opticnav.ardd.protocol.chan.ChannelMultiplexer;

public class ARDChannelsManager implements Runnable {
    private ExecutorService threadPool;
    private Channel channel;
    private ChannelMultiplexer.Listener listener;
    private Channel gatekeeper;
    private ClientConnection gatekeeperConn;

    public ARDChannelsManager(Channel channel, ARDListsManager ardListsManager) {
        this.threadPool = Executors.newCachedThreadPool();
        this.channel = channel;
        
        ChannelMultiplexer mpxr = new ChannelMultiplexer(this.channel);
        this.gatekeeper = mpxr.createChannel(0);
        
        this.gatekeeperConn = new ClientConnection(this.gatekeeper,
                new ARDClientGatekeeperCommandHandler(ardListsManager));
        
        this.listener = mpxr.createListener();
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
