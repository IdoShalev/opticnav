package opticnav.ardd.instance;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import opticnav.ardd.protocol.InstanceDeploymentInfo.ARDIdentifier;
import opticnav.ardd.protocol.TemporaryResourceUtil.TemporaryResource;

/**
 * All properties about an instance, as determined on deployment. Instance.Info is immutable and doesn't change
 * for the lifetime of an instance.
 */
public class InstanceInfo {
    public final Date startTime;
    public final String name;
    public final boolean hasMapImage;
    public final TemporaryResource mapImage;
    public final InstanceInfo.Anchor[] mapAnchors;
    public final List<InstanceInfo.StaticMarker> staticMarkers;
    public final List<InstanceInfo.ARDIdentifier> invitedARDs;

    /**
     * @param startTime The start time. This is passed as a parameter so that the constructor remains deterministic.
     * @param name The name of the instance
     * @param mapImage The resource representing the map image. Can be null if no map image is needed.
     *                 If null, mapAnchors must also be null.
     * @param mapAnchors The list of map anchors. Can be null if no map image is needed.
     *                   If null, mapImage must also be null.
     * @param staticMarkers The list of static markers. If there are none, an empty list must be passed (not null).
     * @param invitedARDs The list of invited devices.
     */
    public InstanceInfo(Date startTime, String name, TemporaryResource mapImage, InstanceInfo.Anchor[] mapAnchors,
            List<InstanceInfo.StaticMarker> staticMarkers, List<InstanceInfo.ARDIdentifier> invitedARDs) throws IllegalArgumentException {
        if ((mapImage != null) != (mapAnchors != null)) {
            throw new IllegalArgumentException("mapImage and mapAnchors should both either be null or not-null");
        }
        
        this.startTime = startTime;
        this.name = name;
        this.hasMapImage = mapImage != null && mapAnchors != null;
        this.mapImage = mapImage;
        this.mapAnchors = mapAnchors;
        this.staticMarkers = staticMarkers;
        this.invitedARDs = Collections.unmodifiableList(invitedARDs);
    }

    public boolean hasInvitedARD(int ardID) {
        return getARD(ardID) != null;
    }

    public ARDIdentifier getARD(int ardID) {
        for (ARDIdentifier identifier: this.invitedARDs) {
            if (identifier.getArdID() == ardID) {
                return identifier;
            }
        }
        return null;
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
        
        @Override
        public String toString() {
            return String.format("ID: %d; Name: %s", ardID, name);
        }
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