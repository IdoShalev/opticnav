package opticnav.ardd.connections;

import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;
import static opticnav.ardd.protocol.Protocol.ARDClient.Lobby.*;

import java.io.IOException;

public class ARDClientLobbyConnectionHandler implements ClientConnection.CommandHandler {
    @Override
    public void command(int code, PrimitiveReader in, PrimitiveWriter out)
            throws IOException, InterruptedException {
        if (code == Commands.LIST_INSTANCES.CODE) {
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
}
