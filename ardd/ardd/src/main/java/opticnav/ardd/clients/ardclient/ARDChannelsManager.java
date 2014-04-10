package opticnav.ardd.clients.ardclient;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.commons.math3.util.Pair;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import opticnav.ardd.ARDConnection;
import opticnav.ardd.ARDListsManager;
import opticnav.ardd.BlockingValue;
import opticnav.ardd.InstancesList;
import opticnav.ardd.clients.ClientCommandDispatcher;
import opticnav.ardd.instance.Entity;
import opticnav.ardd.instance.EntitySubscriber;
import opticnav.ardd.instance.Instance;
import opticnav.ardd.protocol.GeoCoordFine;
import opticnav.ardd.protocol.chan.Channel;
import opticnav.ardd.protocol.chan.ChannelMultiplexer;
import opticnav.ardd.protocol.consts.ARDdARDProtocol;

/**
 * For a given ARD connection, manages all channels of the connection. 
 * 
 * @author Danny Spencer
 *
 */
public class ARDChannelsManager implements Callable<Void> {
    private static final XLogger LOG = XLoggerFactory
            .getXLogger(ARDChannelsManager.class);
    
    private final ExecutorService threadPool;
    private final Channel channel;
    private final ChannelMultiplexer mpxr;
    private final ChannelMultiplexer.Listener listener;
    private final InstancesList instances;

    private final Channel gatekeeper;
    private final ClientCommandDispatcher gatekeeperConn;

    private ARDConnection connection;
    private Channel connectedChannel;
    private ClientCommandDispatcher connectedCommandDispatcher;

    // TODO - wtf?
    private ClientCommandDispatcher instanceCommandDispatcher;
    
    public ARDChannelsManager(Channel channel, ExecutorService threadPool,
            ARDListsManager ardListsManager) {
        this.threadPool = threadPool;
        this.channel = channel;
        
        this.mpxr = new ChannelMultiplexer(this.channel);
        this.gatekeeper = this.mpxr.createChannel(ARDdARDProtocol.Channels.GATEKEEPER);
        
        this.gatekeeperConn = new ClientCommandDispatcher(this.gatekeeper,
                new GatekeeperCommandHandler(ardListsManager, this));
        
        this.listener = this.mpxr.createListener();
        this.instances = ardListsManager.getInstancesList();
    }
    
    public void startConnection(ARDConnection connection) {
        this.connection = connection;
        this.connectedChannel = this.mpxr.createChannel(ARDdARDProtocol.Channels.CONNECTED);
        
        this.connectedCommandDispatcher =
                new ClientCommandDispatcher(this.connectedChannel,
                                            new ConnectedCommandHandler(this, this.instances, connection));
        
        // TODO - wait for this to finish on close
        threadPool.submit(this.connectedCommandDispatcher);
    }
    
    public Pair<Integer, EntitySubscriber> startInstanceConnection(Instance instance, BlockingValue<Entity> entity) {
        // TODO - proper channel ID
        final int channelID = 55;
        
        final Channel instanceChannel = this.mpxr.createChannel(channelID);
        final EntitySubscriber subscriber = new ARDInstanceWriter(instanceChannel.getOutputStream());
        
        InstanceCommandHandler instanceCommandHandler = new InstanceCommandHandler(connection, entity);
        this.instanceCommandDispatcher = new ClientCommandDispatcher(instanceChannel, instanceCommandHandler);
        
        this.threadPool.submit(instanceCommandDispatcher);
        
        return new Pair<Integer, EntitySubscriber>(channelID, subscriber);
    }

    @Override
    public Void call() {

        Future<Void> gk = this.threadPool.submit(this.gatekeeperConn);
        Future<Void> listenerResult = this.threadPool.submit(listener);
        
        try {        
            listenerResult.get();
            gk.get();
        } catch (Exception e) {
            LOG.catching(e);
        } finally {
            // In all cases, any connected should be closed
            if (this.connection != null) {
                this.connection.close();
            }
        }
        
        return null;
    }
}
