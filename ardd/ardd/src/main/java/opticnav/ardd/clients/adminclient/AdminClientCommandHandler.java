package opticnav.ardd.clients.adminclient;

import java.io.IOException;

import opticnav.ardd.ARDListsManager;
import opticnav.ardd.clients.ClientCommandDispatcher;
import opticnav.ardd.protocol.ConfCode;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;
import opticnav.ardd.protocol.Protocol;

public class AdminClientCommandHandler implements ClientCommandDispatcher.CommandHandler {
    private ARDListsManager ardListsManager;

    public AdminClientCommandHandler(ARDListsManager ardListsManager) {
        this.ardListsManager = ardListsManager;
    }
    
    @Override
    public void command(int code, PrimitiveReader in, PrimitiveWriter out)
            throws IOException {
        if (code == Protocol.AdminClient.Commands.REGARD.getCode()) {
            byte[] hexCode = in.readFixedBlob(Protocol.CONFCODE_BYTES);
            ConfCode confcode = new ConfCode(hexCode);
            
            int ard_id;
            ard_id = this.ardListsManager.persistPendingWithConfCode(confcode);

            out.writeUInt31(ard_id);
            out.flush();
        }
    }
}
