package opticnav.ardd.broker.ard;

import opticnav.ardd.ard.ARDConnectedException;
import opticnav.ardd.ard.ARDConnected;
import opticnav.ardd.ard.ARDInstance;
import opticnav.ardd.ard.ARDInstanceJoinStatus;
import opticnav.ardd.ard.InstanceInfo;
import opticnav.ardd.protocol.GeoCoordFine;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveUtil;
import opticnav.ardd.protocol.PrimitiveWriter;
import static opticnav.ardd.protocol.consts.ARDdARDProtocol.Connected.*;
import opticnav.ardd.protocol.chan.Channel;
import opticnav.ardd.protocol.chan.ChannelMultiplexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class ARDConnectedImpl implements ARDConnected {
    private final PrimitiveReader input;
    private final PrimitiveWriter output;
    private final ChannelMultiplexer mpxr;

    public ARDConnectedImpl(Channel connectedChannel, ChannelMultiplexer mpxr) {
        this.input  = PrimitiveUtil.reader(connectedChannel);
        this.output = PrimitiveUtil.writer(connectedChannel);
        this.mpxr = mpxr;
    }
    
    @Override
    public List<InstanceInfo> listInstances() throws ARDConnectedException {
        try {
            output.writeUInt8(Commands.LIST_INSTANCES);
            output.flush();

            int count = input.readUInt16();
            List<InstanceInfo> instanceList = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                String name = input.readString();
                int id      = input.readUInt16();
                InstanceInfo info = new InstanceInfo(name, id);
                instanceList.add(info);
            }
            
            return instanceList;
        } catch (IOException e) {
            throw new ARDConnectedException(e);
        }
    }

    @Override
    public ARDInstanceJoinStatus joinInstance(int instanceID, GeoCoordFine initialLocation) throws ARDConnectedException {
        try {
            output.writeUInt8(Commands.JOIN_INSTANCE);
            output.writeUInt31(instanceID);
            output.flush();
            
            final int response = input.readUInt8();
            // TODO - replace with constants
            if (response == 0) {
                // could not join - no instance found
                return new ARDInstanceJoinStatus(ARDInstanceJoinStatus.Status.NOINSTANCE);
            } else if (response == 1) {
                // joined
                final int instanceChannelID = input.readUInt8();
                final Channel channel = this.mpxr.createChannel(instanceChannelID);
                final ARDInstance instance;
                instance = new ARDInstanceImpl(channel);
                
                return new ARDInstanceJoinStatus(instance);
            } else {
                throw new IllegalStateException("Unexpected response code: " + response);
            }
            
        } catch (IOException e) {
            throw new ARDConnectedException(e);
        }
    }
}
