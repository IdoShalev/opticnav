package opticnav.ardd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.MultiValueMap;

import opticnav.ardd.protocol.InstanceInfo;

public class InstancesList {
    /** One owner to many instances */
    private final MultiValueMap<Long, Integer> owner_instance;
    private final Map<Integer, Instance> instances;
    private int instanceIndex;
    
    public InstancesList() {
        this.owner_instance = new MultiValueMap<>();
        this.instances = new HashMap<>();
        this.instanceIndex = 1;
    }
    
    public synchronized int addInstance(long owner, Instance instance) throws IOException {
        final int instanceID = instanceIndex++;
        owner_instance.put(owner, instanceID);
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

    /**
     * Perform a lookup on all active instances belonging to an owner, and compose a list from those instances
     * 
     * @param owner An ID number identifying the owner.
     * @return A list of instances belonging to the owner, or null if no owner was found
     */
    public synchronized List<InstanceInfo> listInstancesByOwner(long owner) {
        final Collection<Integer> instanceIDs;
        instanceIDs = owner_instance.getCollection(owner);
        if (instanceIDs == null) {
            return null;
        } else {
            final List<InstanceInfo> list = new ArrayList<>(instanceIDs.size()); 
            for (int instanceID: instanceIDs) {
                final Instance inst = instances.get(instanceID);
                final long startTime = inst.getStartTime().getTime();
                final List<InstanceInfo.ARDIdentifier> invitedARDs;
                
                invitedARDs = new ArrayList<>(inst.getInvitedARDs().size());
                for (Instance.ARDIdentifier ard: inst.getInvitedARDs()) {
                    invitedARDs.add(new InstanceInfo.ARDIdentifier(ard.getArdID(), ard.getName()));
                }
                
                list.add(new InstanceInfo(instanceID, owner, inst.getName(), startTime, invitedARDs));
            }
            return list;
        }
    }
}
