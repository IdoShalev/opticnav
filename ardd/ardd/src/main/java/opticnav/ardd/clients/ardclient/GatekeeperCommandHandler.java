package opticnav.ardd.clients.ardclient;

import java.io.IOException;

import opticnav.ardd.clients.ClientCommandDispatcher;
import opticnav.ardd.protocol.ConfCode;
import opticnav.ardd.protocol.PassCode;
import opticnav.ardd.protocol.Protocol;

import org.apache.commons.math3.util.Pair;

import opticnav.ardd.ARDConnection;
import opticnav.ardd.ARDListsManager;
import opticnav.ardd.BlockingValue;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;
import static opticnav.ardd.protocol.Protocol.ARDClient.*;

class GatekeeperCommandHandler implements ClientCommandDispatcher.CommandHandler {
    private ARDListsManager ardListsManager;
    private ARDChannelsManager ardChannelsManager;
    
    public GatekeeperCommandHandler(ARDListsManager ardListsManager,
                                             ARDChannelsManager ardChannelsManager) {
        this.ardListsManager = ardListsManager;
        this.ardChannelsManager = ardChannelsManager;
    }
    
    @Override
    public void command(int code, PrimitiveReader in, PrimitiveWriter out)
            throws IOException, InterruptedException {
        if (code == Commands.REQCODES.CODE) {
            final Pair<Pair<PassCode, ConfCode>, BlockingValue<Integer>> codesWait;
            final PassCode passCode;
            final ConfCode confCode;
            
            codesWait = this.ardListsManager.generatePassConfCodes();
            passCode = codesWait.getFirst().getFirst();
            confCode = codesWait.getFirst().getSecond();
            
            out.writeFixedBlob(confCode.getByteArray());
            // TODO - proper cancellation channel
            out.writeUInt8(255);
            out.flush();
            
            // blocks until result (ARD registered or otherwise)

            // TODO - proper response and ARD id
            int ardID = codesWait.getSecond().get();
            int response = ReqCodes.REGISTERED;
            out.writeUInt8(response);
            out.writeFixedBlob(passCode.getByteArray());

            out.flush();
        } else if (code == Commands.CONNECT_TO_LOBBY.CODE) {
            byte[] passCodeBytes;
            passCodeBytes = in.readFixedBlob(Protocol.PASSCODE_BYTES);

            final PassCode passCode = new PassCode(passCodeBytes);

            if (this.ardListsManager.getPersistedList().containsPassCode(passCode)) {
                // passcode exists
                final ARDConnection connection;
                connection = this.ardListsManager.getConnectedList().createConnected(passCode);
                if (connection != null) {
                    // no ongoing connection
                    this.ardChannelsManager.startLobbyConnection(connection);
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
