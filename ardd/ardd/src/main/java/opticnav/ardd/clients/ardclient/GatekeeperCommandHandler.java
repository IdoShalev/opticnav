package opticnav.ardd.clients.ardclient;

import opticnav.ardd.clients.AnnotatedCommandHandler;
import opticnav.ardd.protocol.ConfCode;
import opticnav.ardd.protocol.PassCode;
import opticnav.ardd.protocol.Protocol;

import org.apache.commons.math3.util.Pair;

import opticnav.ardd.ARDConnection;
import opticnav.ardd.ARDListsManager;
import opticnav.ardd.BlockingValue;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;
import static opticnav.ardd.protocol.consts.ARDdARDProtocol.*;

public class GatekeeperCommandHandler extends AnnotatedCommandHandler {
    private ARDListsManager ardListsManager;
    private ARDChannelsManager ardChannelsManager;
    
    public GatekeeperCommandHandler(ARDListsManager ardListsManager,
                                             ARDChannelsManager ardChannelsManager) {
        super(GatekeeperCommandHandler.class);
        this.ardListsManager = ardListsManager;
        this.ardChannelsManager = ardChannelsManager;
    }
    
    @Command(Commands.REQCODES)
    public void reqCodes(PrimitiveReader in, PrimitiveWriter out) throws Exception {
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
    }
    
    @Command(Commands.CONNECT)
    public void connect(PrimitiveReader in, PrimitiveWriter out) throws Exception {
        byte[] passCodeBytes;
        passCodeBytes = in.readFixedBlob(Protocol.PASSCODE_BYTES);

        final PassCode passCode = new PassCode(passCodeBytes);

        if (this.ardListsManager.getPersistedList().containsPassCode(passCode)) {
            // passcode exists
            final ARDConnection connection;
            connection = this.ardListsManager.getConnectedList().createConnected(passCode);
            if (connection != null) {
                // no ongoing connection
                this.ardChannelsManager.startConnection(connection);
                // TODO - replace 0 with constant
                out.writeUInt8(0);
            } else {
                // there's an ongoing connection
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
