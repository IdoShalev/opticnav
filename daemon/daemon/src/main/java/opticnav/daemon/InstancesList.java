package opticnav.daemon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.map.MultiValueMap;

import opticnav.daemon.instance.Entity;
import opticnav.daemon.instance.EntitySubscriber;
import opticnav.daemon.instance.Instance;
import opticnav.daemon.instance.InstanceInfo;
import opticnav.daemon.protocol.InstanceDeploymentInfo;

/**
 * The InstancesList class contains a list of all active instances.
 * Once an instance is started by AdminClient, it is added here.
 * Once an instance is stopped by AdminClient, or if there is an error while running it, it is removed here.
 * 
 * @author Danny Spencer
 *
 */
public class InstancesList {
    /**
     * The callbacks in this class are called when either an instance is being joined to,
     * or an instance couldn't be joined.
     * 
     * @author Danny Spencer
     *
     */
    public interface JoinInstanceCallbacks {
        /**
         * The instance is being joined to. The implementation needs to provide a subscriber object so that marker
         * updates are sent to it.
         * 
         * @param instance The instance being joined to
         * @param entity Refers to an Entity object to be constructed shortly after this method is finished.
         * @return The new entity subscriber
         * @throws Exception
         */
        public EntitySubscriber joining(Instance instance, BlockingValue<Entity> entity) throws Exception;
        /**
         * Called when the instance to be joined couldn't be found.
         * 
         * @throws Exception
         */
        public void noInstanceFound() throws Exception;
    }

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
     * Attempt to join an ARD to an instance by adding an {@link Entry} representing the device to the instance.
     * 
     * @param instanceID The ID of the instance to join
     * @param ardID The ID of the device to add to the instance
     * @param callbacks The implementation of callbacks to call when joining succeeds or fails
     * @throws Exception XXX Any exception thrown by any method in JoinInstanceCallbacks. It's nasty, we know...
     */
    public synchronized void joinInstance(int instanceID, final int ardID,
            final JoinInstanceCallbacks callbacks)
            throws Exception {
        final Instance instance = instances.get(instanceID);
        if (instance == null) {
            callbacks.noInstanceFound();
        } else {
            final EntitySubscriber subscriber;
            final BlockingValue<Entity> entityFuture = new BlockingValue<>();
            subscriber = callbacks.joining(instance, entityFuture);
            final Entity entity = instance.createEntity(ardID, subscriber);
            entityFuture.set(entity);
        }
    }

    /**
     * Remove the instance identified by an instance ID
     * 
     * @param instanceID The ID number identifying the instance
     * @return True if the instance existed and was removed, false if instance didn't exist
     * @throws IOException Thrown if there was a problem stopping the instance
     */
    public synchronized boolean removeInstance(int instanceID) throws IOException {
        final Instance instance = instances.remove(instanceID);
        if (instance != null) {
            // XXX - removing instance ID from owner_instance
            for (Entry<Long, Object> entry: owner_instance.entrySet()) {
                @SuppressWarnings("unchecked")
                Collection<Integer> c = (Collection<Integer>)entry.getValue();
                
                c.remove(instanceID);
            }
            instance.close();
            return true;
        } else {
            return false;
        }
    }
}
