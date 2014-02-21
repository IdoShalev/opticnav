package opticnav.ardd.connections;

import java.io.IOException;

import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;

public interface CommandRunnable {
    public void run(PrimitiveReader in, PrimitiveWriter out) throws IOException;
}
