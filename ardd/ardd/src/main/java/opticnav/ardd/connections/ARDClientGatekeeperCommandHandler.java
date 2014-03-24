package opticnav.ardd.connections;

import java.io.IOException;

import opticnav.ardd.protocol.PassCode;
import opticnav.ardd.protocol.Protocol;
import org.apache.commons.math3.util.Pair;

import opticnav.ardd.ARDListsManager;
import opticnav.ardd.BlockingValue;
import opticnav.ardd.PassConfCodes;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;
import static opticnav.ardd.protocol.Protocol.ARDClient.*;

public class ARDClientGatekeeperCommandHandler implements ClientConnection.CommandHandler {
    private ARDListsManager ardListsManager;
    private ARDChannelsManager ardChannelsManager;
    
    public ARDClientGatekeeperCommandHandler(ARDListsManager ardListsManager,
                                             ARDChannelsManager ardChannelsManager) {
        this.ardListsManager = ardListsManager;
        this.ardChannelsManager = ardChannelsManager;
    }
    
    @Override
    public void command(int code, PrimitiveReader in, PrimitiveWriter out)
            throws IOException, InterruptedException {
        if (code == Commands.REQCODES.CODE) {
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
            int response = ReqCodes.REGISTERED;
            out.writeUInt8(response);
            out.writeFixedBlob(codes.getPasscode().getByteArray());

            out.flush();
        } else if (code == Commands.CONNECT_TO_LOBBY.CODE) {
            byte[] passCodeBytes;
            passCodeBytes = in.readFixedBlob(Protocol.PASSCODE_BYTES);

            final PassCode passCode = new PassCode(passCodeBytes);

            if (this.ardListsManager.getPersistedList().containsPassCode(passCode)) {
                // passcode exists
                if (this.ardChannelsManager.startLobbyConnection(passCode)) {
                    // no ongoing lobby connection
                    // TODO - replace 0 with constant
                    out.writeUInt8(0);
                } else {
                    // there's an ongoing lobby connection
                    // TODO - replace 2 with constant
                    out.writeUInt8(2);
                }

            } else {
                // passcode does not exist
                // TODO - replace 1 with constant
                out.writeUInt8(1);
            }
            out.flush();
        }
    }
}
