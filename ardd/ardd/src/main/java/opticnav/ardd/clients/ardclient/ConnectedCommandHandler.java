package opticnav.ardd.clients.ardclient;

import java.io.IOException;

import org.apache.commons.math3.util.Pair;

import opticnav.ardd.ARDConnection;
import opticnav.ardd.InstancesList;
import opticnav.ardd.clients.AnnotatedCommandHandler;
import opticnav.ardd.instance.EntitySubscriber;
import opticnav.ardd.instance.Instance;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;
import static opticnav.ardd.protocol.consts.ARDdARDProtocol.Connected.*;

public class ConnectedCommandHandler extends AnnotatedCommandHandler {
    private final ARDChannelsManager ardChannelsManager;
    private final InstancesList instances;
    private final ARDConnection connection;
    
    public ConnectedCommandHandler(ARDChannelsManager ardChannelsManager, InstancesList instances,
                                   ARDConnection connection) {
        super(ConnectedCommandHandler.class);
        this.ardChannelsManager = ardChannelsManager;
        this.instances = instances;
        this.connection = connection;
    }
    
    @Command(Commands.LIST_INSTANCES)
    public void listInstances(PrimitiveReader in, PrimitiveWriter out) throws Exception {
        // TODO
        int count = 2;

        out.writeUInt16(count);

        for (int i = 0; i < count; i++) {
            // TODO - write instances
            out.writeString("Instance name");
            out.writeUInt16(i);
        }
        out.flush();
    }
    
    @Command(Commands.JOIN_INSTANCE)
    public void joinInstance(PrimitiveReader in, final PrimitiveWriter out) throws Exception {
        final int instanceID = in.readUInt31();
        
        instances.joinInstance(instanceID, connection.getARDID(), new InstancesList.JoinInstanceCallbacks() {
            @Override
            public EntitySubscriber joining(Instance instance) throws IOException {
                final Pair<Integer, EntitySubscriber> p = ardChannelsManager.startInstanceConnection(instance);
                final int instanceChannelID = p.getFirst();
                
                // result: joined
                out.writeUInt8(1);
                out.writeUInt8(instanceChannelID);
                out.flush();
                
                return p.getSecond();
            }
            
            @Override
            public void noInstanceFound() throws IOException {
                // result: could not join
                out.writeUInt8(0);
                out.flush();
            }
        });
    }
}
