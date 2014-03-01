package opticnav.ardd.connections;

import java.io.IOException;

import org.apache.commons.math3.util.Pair;

import opticnav.ardd.ARDListsManager;
import opticnav.ardd.BlockingValue;
import opticnav.ardd.PassConfCodes;
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
            Pair<PassConfCodes, BlockingValue<Integer>> codesWait;
            PassConfCodes codes;
            
            codesWait = this.ardListsManager.generatePassConfCodes();
            codes = codesWait.getFirst();
            
            out.writeFixedBlob(codes.getPasscode().getByteArray());
            out.writeFixedBlob(codes.getConfcode().getByteArray());
            out.flush();
            
            // blocks until result (ARD registered or otherwise)
            int result = codesWait.getSecond().get();
            
            out.writeUInt8(result);
            out.flush();
        }
    }
}
