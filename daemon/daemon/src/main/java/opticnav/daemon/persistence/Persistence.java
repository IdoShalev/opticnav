package opticnav.daemon.persistence;

import opticnav.daemon.protocol.PassCode;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public interface Persistence extends AutoCloseable {

    void persistARDList(Iterator<Map.Entry<Integer, PassCode>> list) throws IOException;

    void readARDList(Map<Integer, PassCode> list) throws IOException;

    int nextARDID() throws IOException;

    public void close() throws IOException;
}
