package opticnav.ardd.ard;

import java.util.List;

public interface ARDConnected {
    public void listInstances(List<InstanceInfo> instanceList) throws ARDConnectedException;
}
