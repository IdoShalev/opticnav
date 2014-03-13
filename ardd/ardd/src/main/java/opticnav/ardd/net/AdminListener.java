package opticnav.ardd.net;

import java.io.IOException;
import java.util.logging.Logger;

import opticnav.ardd.ARDListsManager;
import opticnav.ardd.connections.AdminClientCommandHandler;
import opticnav.ardd.connections.ClientConnection;
import opticnav.ardd.protocol.chan.Channel;

public class AdminListener implements Runnable {
    private final class Spawner implements Listener.ConnectionSpawner {
        @Override
        public Runnable create(Channel channel) {
            return new ClientConnection(channel,
                    new AdminClientCommandHandler(ardListsManager));
        }
    }

    private ARDListsManager ardListsManager;
    private Listener listener;
    
    public AdminListener(int port, ARDListsManager ardListsManager)
            throws IOException {
        Logger logger = Logger.getLogger("AdminListener");
        this.ardListsManager = ardListsManager;
        this.listener = new Listener(port, new Spawner(), logger);
    }

    @Override
    public void run() {
        this.listener.run();
    }
}
