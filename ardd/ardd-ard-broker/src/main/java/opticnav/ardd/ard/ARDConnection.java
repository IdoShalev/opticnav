package opticnav.ardd.ard;

import java.io.IOException;

public interface ARDConnection extends AutoCloseable {
    public void close() throws IOException;
}
