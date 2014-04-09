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
    private final String name;
    private final EntitySubscriber subscriber;
    private final Set<EntitySubscriber> subscribers;
    private final Closeable removeEntity;
    private GeoCoordFine geoCoord;
    
    public Entity(int markerID, String name, GeoCoordFine geoCoord) {
        this.markerID = markerID;
        this.name = name;
        this.geoCoord = geoCoord;
        this.subscriber = null;
        this.subscribers = new HashSet<>();
        this.removeEntity = null;
        
        LOG.debug("Created static entity (marker ID "+markerID+")");
    }
    
    public Entity(int markerID, String name, GeoCoordFine geoCoord, EntitySubscriber subscriber, Closeable removeEntity) {
        this.markerID = markerID;
        this.name = name;
        this.geoCoord = geoCoord;
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
        if (removeEntity != null) {
            IOUtils.closeQuietly(removeEntity);
            LOG.debug("Closed entity (marker ID "+markerID+")");
        }
    }

    public int getMarkerID() {
        return this.markerID;
    }

    public String getName() {
        return this.name;
    }
    
    public synchronized GeoCoordFine getGeoCoord() {
        return this.geoCoord;
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
