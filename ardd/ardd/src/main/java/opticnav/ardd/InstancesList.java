package opticnav.ardd;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.util.Pair;

import opticnav.ardd.persistence.Persistence;

public class InstancesList {
    private final Map<Integer, Instance> instances;
    private final Persistence persistence;
    
    public InstancesList(Persistence persistence) {
        this.instances = new HashMap<>();
        this.persistence = persistence;
    }
    
    public synchronized int addInstance(Instance instance) throws IOException {
        final int instanceID = this.persistence.nextInstanceID();
        instances.put(instanceID, instance);
        return instanceID;
    }
    
    public synchronized void getInstancesForARD(int ardID, Map<Integer, Instance> list) {
        // It could be more "efficient", sure.
        for (Map.Entry<Integer, Instance> entry: instances.entrySet()) {
            final int instanceID = entry.getKey();
            final Instance instance = entry.getValue();
            
            if (instance.hasInvitedARD(ardID)) {
                list.put(instanceID, instance);
            }
        }
    }
}
