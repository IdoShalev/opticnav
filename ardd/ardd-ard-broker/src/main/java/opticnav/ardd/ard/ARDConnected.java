package opticnav.ardd.ard;

import java.util.List;

import opticnav.ardd.protocol.GeoCoordFine;

public interface ARDConnected {
    /**
     * Queries the server for a list of instances the device can connect to.
     *
     * @return The entire list of instances available for the device
     * @throws ARDConnectedException
     */
    public List<InstanceInfo> listInstances() throws ARDConnectedException;
    public ARDInstanceJoinStatus joinInstance(int instanceID, GeoCoordFine initialLocation) throws ARDConnectedException;
}
