package opticnav.ardd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.collections4.map.MultiValueMap;

import opticnav.ardd.instance.Entity;
import opticnav.ardd.instance.EntitySubscriber;
import opticnav.ardd.instance.Instance;
import opticnav.ardd.instance.InstanceInfo;
import opticnav.ardd.protocol.GeoCoordFine;
import opticnav.ardd.protocol.InstanceDeploymentInfo;

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
    
    public synchronized Map<Integer, Instance> getInstancesForARD(int ardID) {
        // It could be more "efficient", sure.
        Map<Integer, Instance> list = new HashMap<>();
        for (Map.Entry<Integer, Instance> entry: instances.entrySet()) {
            final int instanceID = entry.getKey();
            final Instance instance = entry.getValue();
            
            if (instance.getInfo().hasInvitedARD(ardID)) {
                list.put(instanceID, instance);
            }
        }
        
        return list;
    }

    /**
     * Perform a lookup on all active instances belonging to an owner, and compose a list from those instances
     * 
     * @param owner An ID number identifying the owner.
     * @return A list of instances belonging to the owner, or null if no owner was found
     */
    public synchronized List<InstanceDeploymentInfo> listInstancesByOwner(long owner) {
        final Collection<Integer> instanceIDs;
        instanceIDs = owner_instance.getCollection(owner);
        if (instanceIDs == null) {
            return null;
        } else {
            final List<InstanceDeploymentInfo> list = new ArrayList<>(instanceIDs.size()); 
            for (int instanceID: instanceIDs) {
                final Instance inst = instances.get(instanceID);
                final InstanceInfo info = inst.getInfo();
                final long startTime = info.startTime.getTime();
                final List<InstanceDeploymentInfo.ARDIdentifier> invitedARDs;
                
                invitedARDs = new ArrayList<>(info.invitedARDs.size());
                for (InstanceInfo.ARDIdentifier ard: info.invitedARDs) {
                    invitedARDs.add(new InstanceDeploymentInfo.ARDIdentifier(ard.getArdID(), ard.getName()));
                }
                
                list.add(new InstanceDeploymentInfo(instanceID, owner, info.name, startTime, invitedARDs));
            }
            return list;
        }
    }

    /**
     * 
     * @param instanceID
     * @param ardID
     * @param callbacks
     * @throws Exception XXX Any exception thrown by any method in JoinInstanceCallbacks. It's nasty, we know...
     */
    public synchronized void joinInstance(int instanceID, final int ardID, final GeoCoordFine initialLocation,
            final JoinInstanceCallbacks callbacks)
            throws Exception {
        final Instance instance = instances.get(instanceID);
        if (instance == null) {
            callbacks.noInstanceFound();
        } else {
            final EntitySubscriber subscriber;
            final SettableFuture<Entity> entityFuture = new SettableFuture<>();
            subscriber = callbacks.joining(instance, entityFuture);
            final Entity entity = instance.createEntity(ardID, initialLocation, subscriber);
            entityFuture.set(entity);
        }
    }
    
    public interface JoinInstanceCallbacks {
        public EntitySubscriber joining(Instance instance, Future<Entity> entity) throws Exception;
        public void noInstanceFound() throws Exception;
    }
}
