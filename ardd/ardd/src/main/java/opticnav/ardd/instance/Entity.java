package opticnav.ardd.instance;

import java.io.Closeable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import opticnav.ardd.protocol.GeoCoordFine;

public class Entity implements AutoCloseable {
    private static final XLogger LOG = XLoggerFactory.getXLogger(Entity.class);
    
    private final int markerID;
    private final EntitySubscriber subscriber;
    private final Set<EntitySubscriber> subscribers;
    private final Closeable removeEntity;
    private GeoCoordFine geoCoord;
    
    public Entity(int markerID, EntitySubscriber subscriber, Closeable removeEntity) {
        this.markerID = markerID;
        this.subscriber = subscriber;
        this.subscribers = new HashSet<>();
        this.removeEntity = removeEntity;
        LOG.debug("Created entity (marker ID "+markerID+")");
    }
    
    public void close() {
        LOG.debug("Closing entity (marker ID "+markerID+")...");
        for (EntitySubscriber s: subscribers) {
            s.removeMarker(markerID);
        }
        IOUtils.closeQuietly(removeEntity);
        LOG.debug("Closed entity (marker ID "+markerID+")");
    }
    
    public synchronized void move(GeoCoordFine geoCoord) {
        this.geoCoord = geoCoord;
        LOG.trace("Move marker "+markerID+": " + geoCoord);
        for (EntitySubscriber s: subscribers) {
            s.moveMarker(markerID, geoCoord);
        }
    }
    
    public synchronized void addSubscriber(EntitySubscriber subscriber) {
        this.subscribers.add(subscriber);
    }
    
    public synchronized void removeSubscriber(EntitySubscriber subscriber) {
        this.subscribers.remove(subscriber);
    }

    public EntitySubscriber getEntitySubscriber() {
        return this.subscriber;
    }
}
