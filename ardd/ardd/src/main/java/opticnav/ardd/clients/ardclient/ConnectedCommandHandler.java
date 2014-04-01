package opticnav.ardd.clients.ardclient;

import org.apache.commons.math3.util.Pair;

import opticnav.ardd.ARDConnection;
import opticnav.ardd.Instance;
import opticnav.ardd.InstancesList;
import opticnav.ardd.clients.AnnotatedCommandHandler;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;
import opticnav.ardd.protocol.chan.Channel;
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
    public void joinInstance(PrimitiveReader in, PrimitiveWriter out) throws Exception {
        final int instanceID = in.readUInt31();
        final Instance instance = instances.joinInstance(connection, instanceID);
        final boolean joined = instance != null;
        
        out.writeUInt8(joined?1:0);
        if (joined) {
            final int channelID = this.ardChannelsManager.startInstanceConnection(instance);
            // write the channel ID
            out.writeUInt8(channelID);
        }
        out.flush();
    }
}
