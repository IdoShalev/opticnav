package opticnav.ardd.ard;

import java.util.List;

public interface ARDConnected {
    public List<InstanceInfo> listInstances() throws ARDConnectedException;
}
