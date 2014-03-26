package opticnav.ardd.clients.ardclient;

import opticnav.ardd.clients.AnnotatedCommandHandler;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;
import static opticnav.ardd.protocol.Protocol.ARDClient.Connected.*;

public class ConnectedCommandHandler extends AnnotatedCommandHandler {
    public ConnectedCommandHandler() {
        super(ConnectedCommandHandler.class);
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
}
