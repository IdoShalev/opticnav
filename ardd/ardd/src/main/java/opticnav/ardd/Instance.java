package opticnav.ardd;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import opticnav.ardd.TemporaryResourceUtil.TemporaryResource;

/**
 * The Instance class represents an _active_ instance (the amalgamation of a map and its static/dynamic markers)
 *
 */
public class Instance implements AutoCloseable {
    private final Date startTime;
    private final String name;
    private final boolean hasMapImage;
    private final TemporaryResource mapImage;
    private final Anchor[] mapAnchors;
    private final List<StaticMarker> staticMarkers;
    // TODO - make this a map
    private final List<ARDIdentifier> invitedARDs;

    public Instance(String name, TemporaryResource mapImage, Anchor[] mapAnchors,
            List<StaticMarker> staticMarkers, List<ARDIdentifier> invitedARDs) {
        if ((mapImage != null) != (mapAnchors != null)) {
            throw new IllegalArgumentException("mapImage and mapAnchors should both either be null or not-null");
        }
        
        this.startTime = new Date();
        this.name = name;
        this.hasMapImage = mapImage != null && mapAnchors != null;
        this.mapImage = mapImage;
        this.mapAnchors = mapAnchors;
        this.staticMarkers = staticMarkers;
        this.invitedARDs = Collections.unmodifiableList(invitedARDs);
    }
    
    @Override
    public void close() throws IOException {
        this.mapImage.delete();
    }
    
    public String getName() {
        return this.name;
    }
    
    public Date getStartTime() {
        // create a clone that can't mutate the original
        return (Date)this.startTime.clone();
    }
    
    public List<ARDIdentifier> getInvitedARDs() {
        return this.invitedARDs;
    }

    public boolean hasInvitedARD(int ardID) {
        for (ARDIdentifier i: invitedARDs) {
            if (ardID == i.getArdID()) {
                return true;
            }
        }
        return false;
    }

    /**
     * A location updater observable - used by an entity that is moving (such as an ARD).
     * Notifying this observable will in turn notify other ARDs of this event.
     */
    public interface LocationUpdate {
        public void updateLocation(int lng, int lat);
    }

    public static class ARDIdentifier {
        private final int ardID;
        private final String name;

        public ARDIdentifier(int ardID, String name) {
            this.ardID = ardID;
            this.name = name;
        }

        public int getArdID() { return ardID; }

        public String getName() { return name; }
    }

    public static class StaticMarker {
        private final String name;
        private final int lng;
        private final int lat;
    
        public StaticMarker(String name, int lng, int lat) {
            this.name = name;
            this.lng = lng;
            this.lat = lat;
        }
    
        public String getName() { return name; }
    
        public int getLng() { return lng; }
    
        public int getLat() { return lat; }
    }

    public static class Anchor {
        private final int lng;
        private final int lat;
        private final int localX;
        private final int localY;

        public Anchor(int lng, int lat, int localX, int localY) {
            this.lng = lng;
            this.lat = lat;
            this.localX = localX;
            this.localY = localY;
        }

        public int getLng() { return lng; }

        public int getLat() { return lat; }

        public int getLocalX() { return localX; }

        public int getLocalY() { return localY; }
    }
}
