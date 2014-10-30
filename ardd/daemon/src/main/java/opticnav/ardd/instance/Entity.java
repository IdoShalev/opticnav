package opticnav.ardd.instance;

import java.io.Closeable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import opticnav.ardd.protocol.GeoCoordFine;

/**
 * An Entity (not an ARD) represents a physical object containing a name and a location in an instance.
 * Entities can be moved moved and notify subscribers of the entity of the event.
 * 
 * An Entity may represent an ARD or map marker (as it currently does), but not necessarily.
 * 
 * @author Danny Spencer
 *
 */
public class Entity implements AutoCloseable {
    private static final XLogger LOG = XLoggerFactory.getXLogger(Entity.class);
    
    private final int entityID;
    private final String name;
    private final EntitySubscriber subscriber;
    private final Set<EntitySubscriber> subscribers;
    private final Closeable removeEntity;
    private GeoCoordFine geoCoord;
    
    /**
     * Construct an entity that's static and doesn't move.
     * 
     * @param entityID A unique ID identifying the entity
     * @param name
     * @param geoCoord
     */
    public Entity(int entityID, String name, GeoCoordFine geoCoord) {
        this.entityID = entityID;
        this.name = name;
        this.geoCoord = geoCoord;
        this.subscriber = null;
        this.subscribers = new HashSet<>();
        this.removeEntity = null;
        
        LOG.debug("Created static entity (entity ID "+entityID+")");
    }
    
    public Entity(int entityID, String name, EntitySubscriber subscriber, Closeable removeEntity) {
        this.entityID = entityID;
        this.name = name;
        this.geoCoord = null;
        this.subscriber = subscriber;
        this.subscribers = new HashSet<>();
        this.removeEntity = removeEntity;
        LOG.debug("Created entity (entity ID "+entityID+")");
    }
    
    public void close() {
        LOG.debug("Closing entity (entity ID "+entityID+")...");
        for (EntitySubscriber s: subscribers) {
            s.removeMarker(entityID);
        }
        if (removeEntity != null) {
            IOUtils.closeQuietly(removeEntity);
            LOG.debug("Closed entity (entity ID "+entityID+")");
        }
    }

    public int getEntityID() {
        return this.entityID;
    }

    public String getName() {
        return this.name;
    }
    
    public synchronized GeoCoordFine getGeoCoord() {
        return this.geoCoord;
    }
    
    public synchronized void move(GeoCoordFine geoCoord) {
        LOG.trace("Move entity "+entityID+": " + geoCoord);
        if (this.geoCoord == null) {
            // new marker
            for (EntitySubscriber s: subscribers) {
                s.newMarker(entityID, this.name, geoCoord);
            }
        } else {
            // move marker
            for (EntitySubscriber s: subscribers) {
                s.moveMarker(entityID, geoCoord);
            }
        }
        this.geoCoord = geoCoord;
    }
    
    public synchronized void addSubscriber(EntitySubscriber subscriber) {
        if (subscriber == null) {
            throw new NullPointerException("Subscriber cannot be null");
        }
        this.subscribers.add(subscriber);
    }
    
    public synchronized void removeSubscriber(EntitySubscriber subscriber) {
        this.subscribers.remove(subscriber);
    }

    public EntitySubscriber getEntitySubscriber() {
        return this.subscriber;
    }
}
