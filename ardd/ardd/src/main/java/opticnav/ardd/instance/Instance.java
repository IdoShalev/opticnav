package opticnav.ardd.instance;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import opticnav.ardd.protocol.GeoCoordFine;

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
        for (InstanceInfo.StaticMarker marker: info.staticMarkers) {
            entities.put(nextMarkerID, new Entity(nextMarkerID, marker.getName(), marker.getGeoCoord()));
            nextMarkerID++;
        }
    }
    
    @Override
    public void close() throws IOException {
        if (this.info.mapImage != null) {
            this.info.mapImage.delete();
        }
    }
    
    public InstanceInfo getInfo() {
        return this.info;
    }
    
    public synchronized Entity createEntity(final int ardID, EntitySubscriber subscriber) {
        final int markerID = nextMarkerID++;
        final String name = info.getARD(ardID).getName();
        
        final Entity entity;
        entity = new Entity(markerID, name, subscriber, new Closeable() {
            @Override
            public void close() throws IOException {
                removeEntity(ardID);
            }
        });
        
        // Add all existing entries as subscribers to the new entry
        // Broadcast all existing entries to the subscriber
        // Additionally, add the new subscriber to all existing entries
        for (Entity e: this.entities.values()) {
            if (e.getEntitySubscriber() != null) {
                entity.addSubscriber(e.getEntitySubscriber());
            }
            e.addSubscriber(subscriber);
            subscriber.newMarker(e.getEntityID(), e.getName(), e.getGeoCoord());
        }
            
        this.entities.put(ardID, entity);
        
        return entity;
    }
    
    public synchronized void removeEntity(int ardID) {
        final Entity entity = this.entities.remove(ardID);
        
        for (Entity e: this.entities.values()) {
            e.removeSubscriber(entity.getEntitySubscriber());
        }
    }

    public synchronized Map<Integer, Entity> getEntities() {
        return Collections.unmodifiableMap(this.entities);
    }
}
