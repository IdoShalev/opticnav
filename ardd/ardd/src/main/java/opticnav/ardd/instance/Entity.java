package opticnav.ardd.instance;

import java.util.HashSet;
import java.util.Set;

public class Entity {
    private final int markerID;
    private final EntitySubscriber subscriber;
    private final Set<EntitySubscriber> subscribers;
    private GeoCoordFine geoCoord;
    
    public Entity(int markerID, EntitySubscriber subscriber) {
        this.markerID = markerID;
        this.subscriber = subscriber;
        this.subscribers = new HashSet<>();
    }
    
    public void close() {
        for (EntitySubscriber s: subscribers) {
            s.removeMarker(markerID);
        }
    }
    
    public synchronized void move(GeoCoordFine geoCoord) {
        this.geoCoord = geoCoord;
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
