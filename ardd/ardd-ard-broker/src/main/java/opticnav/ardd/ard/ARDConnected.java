package opticnav.ardd.ard;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public interface ARDConnected extends Closeable {
    /**
     * Queries the server for a list of instances the device can connect to.
     *
     * @return The entire list of instances available for the device
     * @throws ARDConnectedException
     */
    public List<InstanceInfo> listInstances() throws ARDConnectedException;
    public ARDInstanceJoinStatus joinInstance(int instanceID)
            throws ARDConnectedException;
    
    public void close() throws IOException;
}
