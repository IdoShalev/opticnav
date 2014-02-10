package opticnav.ardd.connections;

import java.io.IOException;

import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;

public interface AdminClientCommandsImpl {
    public void registerARDWithConfCode(PrimitiveReader in, PrimitiveWriter out)
            throws IOException;
}
