package opticnav.ardd.connections;

import java.io.IOException;

import org.apache.commons.math3.util.Pair;

import opticnav.ardd.ARDListsManager;
import opticnav.ardd.BlockingValue;
import opticnav.ardd.PassConfCodes;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;
import opticnav.ardd.protocol.Protocol.ARDClient.Commands;

public class ARDClientGatekeeperCommandHandler implements ClientConnection.CommandHandler {
    private ARDListsManager ardListsManager;
    
    public ARDClientGatekeeperCommandHandler(ARDListsManager ardListsManager) {
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
            
            out.writeFixedBlob(codes.getConfcode().getByteArray());
            // TODO - proper cancellation channel
            out.writeUInt8(255);
            out.flush();
            
            // blocks until result (ARD registered or otherwise)
            // TODO - proper response and ARD id
            int ardID = codesWait.getSecond().get();
            int response = 0;
            out.writeUInt8(response);
            out.writeUInt31(ardID);
            out.writeFixedBlob(codes.getPasscode().getByteArray());

            out.flush();
        }
    }
}
