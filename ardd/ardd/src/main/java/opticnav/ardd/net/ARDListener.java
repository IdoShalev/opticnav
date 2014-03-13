package opticnav.ardd.net;

import java.io.IOException;
import java.util.logging.Logger;

import opticnav.ardd.ARDListsManager;
import opticnav.ardd.connections.ARDChannelsManager;
import opticnav.ardd.connections.ARDClientGatekeeperCommandHandler;
import opticnav.ardd.connections.ClientConnection;
import opticnav.ardd.protocol.chan.Channel;

public class ARDListener implements Runnable {
    private final class Spawner implements Listener.ConnectionSpawner {
        @Override
        public Runnable create(Channel channel) {
            return new ARDChannelsManager(channel, ardListsManager);
        }
    }

    private ARDListsManager ardListsManager;
    private Listener listener;
    
    public ARDListener(int port, ARDListsManager ardListsManager)
            throws IOException {
        Logger logger = Logger.getLogger("ARDListener");
        this.ardListsManager = ardListsManager;
        this.listener = new Listener(port, new Spawner(), logger);
    }

    @Override
    public void run() {
        this.listener.run();
    }
}
