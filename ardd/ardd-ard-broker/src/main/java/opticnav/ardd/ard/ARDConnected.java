package opticnav.ardd.ard;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * An ARD broker connection in the Connected state.
 * 
 * @author Danny Spencer
 *
 */
public interface ARDConnected extends Closeable {
    /**
     * Queries the server for a list of instances the device can connect to.
     *
     * @return The entire list of instances available for the device
     * @throws ARDConnectedException
     */
    public List<InstanceInfo> listInstances() throws ARDConnectedException;
    /**
     * Attempt to join the instance specified by an ID.
     * 
     * @param instanceID The ID number identifying the instance
     * @return A status object determining success or failure
     * @throws ARDConnectedException
     */
    public ARDInstanceJoinStatus joinInstance(int instanceID)
            throws ARDConnectedException;
    
    public void close() throws IOException;
}
