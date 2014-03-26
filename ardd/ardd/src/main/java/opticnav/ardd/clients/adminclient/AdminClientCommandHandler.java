package opticnav.ardd.clients.adminclient;

import opticnav.ardd.ARDListsManager;
import opticnav.ardd.clients.AnnotatedCommandHandler;
import opticnav.ardd.protocol.ConfCode;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;
import opticnav.ardd.protocol.Protocol;
import opticnav.ardd.protocol.Protocol.AdminClient.Commands;

public class AdminClientCommandHandler extends AnnotatedCommandHandler {
    private ARDListsManager ardListsManager;

    public AdminClientCommandHandler(ARDListsManager ardListsManager) {
        super(AdminClientCommandHandler.class);
        this.ardListsManager = ardListsManager;
    }
    
    @Command(Commands.REGARD)
    public void registerARD(PrimitiveReader in, PrimitiveWriter out) throws Exception {
        byte[] hexCode = in.readFixedBlob(Protocol.CONFCODE_BYTES);
        ConfCode confcode = new ConfCode(hexCode);
        
        int ard_id;
        ard_id = this.ardListsManager.persistPendingWithConfCode(confcode);

        out.writeUInt31(ard_id);
        out.flush();
    }
}
