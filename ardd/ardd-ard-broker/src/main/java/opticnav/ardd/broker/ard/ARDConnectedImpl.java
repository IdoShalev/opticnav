package opticnav.ardd.broker.ard;

import opticnav.ardd.ard.ARDConnectedException;
import opticnav.ardd.ard.ARDGatekeeperException;
import opticnav.ardd.ard.ARDConnected;
import opticnav.ardd.ard.InstanceInfo;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveUtil;
import opticnav.ardd.protocol.PrimitiveWriter;
import static opticnav.ardd.protocol.consts.ARDdARDProtocol.Connected.*;
import opticnav.ardd.protocol.chan.Channel;

import java.io.IOException;
import java.util.List;

class ARDConnectedImpl implements ARDConnected {
    private Channel connectedChannel;
    private PrimitiveReader input;
    private PrimitiveWriter output;

    public ARDConnectedImpl(Channel connectedChannel) {
        this.connectedChannel = connectedChannel;
        this.input  = PrimitiveUtil.reader(connectedChannel);
        this.output = PrimitiveUtil.writer(connectedChannel);
    }

    /**
     * Queries the server for a list of instances the device can connect to.
     * All available instances will be appended to instanceList.
     * instanceList is never cleared by this method.
     *
     * @param instanceList The list of InstanceInfo to be appended to
     * @throws ARDConnectedException
     */
    @Override
    public void listInstances(List<InstanceInfo> instanceList) throws ARDConnectedException {
        try {
            output.writeUInt8(Commands.LIST_INSTANCES);
            output.flush();

            int count = input.readUInt16();
            for (int i = 0; i < count; i++) {
                String name = input.readString();
                int id      = input.readUInt16();
                InstanceInfo info = new InstanceInfo(name, id);
                instanceList.add(info);
            }
        } catch (IOException e) {
            throw new ARDConnectedException(e);
        }
    }
}
