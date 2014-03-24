package opticnav.ardd.ard;

import java.util.List;

public interface ARDLobbyConnection {
    public void listInstances(List<InstanceInfo> instanceList) throws ARDConnectionException;
}
