package opticnav.daemon.net;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import opticnav.daemon.ARDListsManager;
import opticnav.daemon.clients.ClientCommandDispatcher;
import opticnav.daemon.clients.adminclient.AdminClientCommandHandler;
import opticnav.daemon.protocol.chan.Channel;

public class AdminListener implements Runnable {
    private final class Spawner implements Listener.ConnectionSpawner {
        @Override
        public Callable<Void> create(Channel channel, ExecutorService threadPool) {
            return new ClientCommandDispatcher(channel,
                    new AdminClientCommandHandler(ardListsManager, threadPool));
        }
    }

    private ARDListsManager ardListsManager;
    private Listener listener;
    
    public AdminListener(int port, ARDListsManager ardListsManager)
            throws IOException {
        this.ardListsManager = ardListsManager;
        this.listener = new Listener(this.getClass(), port, new Spawner());
    }

    @Override
    public void run() {
        this.listener.call();
    }
}
