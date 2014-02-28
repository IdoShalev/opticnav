package opticnav.ardd.connections;

import java.io.IOException;

import opticnav.ardd.protocol.HexCode;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;
import opticnav.ardd.protocol.Protocol.ARDClient.Commands;

public class ARDClientCommandHandler implements ClientConnection.CommandHandler {
    @Override
    public void command(int code, PrimitiveReader in, PrimitiveWriter out)
            throws IOException {
        if (code == Commands.REQCODES.getCode()) {
            HexCode passCode = new HexCode("0123456789ABCDEF0123456789ABCDEF");
            HexCode confCode = new HexCode("DEADBEEF");
            
            out.writeFixedBlob(passCode.getByteArray());
            out.writeFixedBlob(confCode.getByteArray());
            out.flush();
            
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            
            out.writeUInt8(40);
            out.flush();
        }
    }
}
