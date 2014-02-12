package opticnav.ardd.connections;

import java.io.IOException;

import opticnav.ardd.protocol.HexCode;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;
import opticnav.ardd.protocol.Protocol;

public class AdminClientCommandHandler implements ClientConnection.CommandHandler {
    @Override
    public void command(int code, PrimitiveReader in, PrimitiveWriter out)
            throws IOException {
        if (code == Protocol.AdminClient.Commands.REGISTER.getCode()) {
            byte[] hexCode = in.readFixedBlob(Protocol.AdminClient.CONFCODE_BYTES);
            HexCode hc = new HexCode(hexCode);
            boolean match = hc.equals(new HexCode("AABBCCDD"));

            out.writeUInt31(match?44:0);
            out.flush();
        }
    }
}
