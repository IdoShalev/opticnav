package opticnav.ardd.instance;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The Instance class represents an _active_ instance (the amalgamation of a map and its static/dynamic markers)
 *
 */
public class Instance implements AutoCloseable {
    private final InstanceInfo info;
    /** Map of entities by ARD id */
    private final Map<Integer, Entity> entities;
    private int nextMarkerID;
    
    public Instance(InstanceInfo info) {
        this.info = info;
        this.entities = new HashMap<>();
        this.nextMarkerID = 0;
    }
    
    @Override
    public void close() throws IOException {
        this.info.mapImage.delete();
    }
    
    public InstanceInfo getInfo() {
        return this.info;
    }
    
    public synchronized Entity createEntity(int ardID, EntitySubscriber subscriber) {
        final Entity entity;
        entity = new Entity(nextMarkerID++, subscriber);
        
        // Add all existing entries as subscribers to the new entry
        // Additionally, add the new subscriber too all existing entries
        for (Entity e: this.entities.values()) {
            entity.addSubscriber(e.getEntitySubscriber());
            e.addSubscriber(subscriber);
        }
        this.entities.put(ardID, entity);
        return entity;
    }
}
