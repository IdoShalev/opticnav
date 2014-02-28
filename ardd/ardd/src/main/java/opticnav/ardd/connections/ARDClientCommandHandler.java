package opticnav.ardd.connections;

import java.io.IOException;

import opticnav.ardd.ARDListsManager;
import opticnav.ardd.PassConfCodes;
import opticnav.ardd.PassConfCodesWait;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;
import opticnav.ardd.protocol.Protocol.ARDClient.Commands;

public class ARDClientCommandHandler implements ClientConnection.CommandHandler {
    private ARDListsManager ardListsManager;
    
    public ARDClientCommandHandler(ARDListsManager ardListsManager) {
        this.ardListsManager = ardListsManager;
    }
    
    @Override
    public void command(int code, PrimitiveReader in, PrimitiveWriter out)
            throws IOException, InterruptedException {
        if (code == Commands.REQCODES.getCode()) {
            PassConfCodesWait codesWait;
            PassConfCodes codes;
            
            codesWait = ardListsManager.generatePassConfCodes();
            codes = codesWait.getPassConfCodes();
            
            out.writeFixedBlob(codes.getPasscode().getByteArray());
            out.writeFixedBlob(codes.getConfcode().getByteArray());
            out.flush();
            
            int result = codesWait.waitForResult();
            
            out.writeUInt8(result);
            out.flush();
        }
    }
}
