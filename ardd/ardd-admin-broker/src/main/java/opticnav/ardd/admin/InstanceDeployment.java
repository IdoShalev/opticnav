package opticnav.ardd.admin;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import javax.activation.MimeType;

public class InstanceDeployment {
    private final String mapName;
    private final MimeType mapImageType;
    private final int mapImageSize;
    private final InputStream mapImageInput;
    private final List<InstanceDeployment.Anchor> mapAnchors;
    private final List<InstanceDeployment.Marker> mapMarkers;
    private final List<InstanceDeployment.ARDIdentifier> ardList;

    public InstanceDeployment(String mapName,
            MimeType mapImageType, int mapImageSize, InputStream mapImageInput,
            List<InstanceDeployment.Anchor> mapAnchors,
            List<InstanceDeployment.Marker> mapMarkers,
            List<InstanceDeployment.ARDIdentifier> ardList) {
        this.mapName       = mapName;
        this.mapImageType  = mapImageType;
        this.mapImageSize  = mapImageSize;
        this.mapImageInput = mapImageInput;
        this.mapAnchors    = Collections.unmodifiableList(mapAnchors);
        this.mapMarkers    = Collections.unmodifiableList(mapMarkers);
        this.ardList       = Collections.unmodifiableList(ardList);
    }
    
    public boolean hasMapImage() {
        return this.mapImageType != null && this.mapImageSize != 0 &&
               this.mapImageInput != null && this.mapAnchors != null &&
               this.mapAnchors.size() == 3;
    }

    public String getMapName() { return mapName; }

    public MimeType getMapImageType() { return mapImageType; }

    public int getMapImageSize() { return mapImageSize; }

    public InputStream getMapImageInput() { return mapImageInput; }

    public List<Anchor> getMapAnchors() { return mapAnchors; }

    public List<Marker> getMapMarkers() { return mapMarkers; }

    public List<ARDIdentifier> getArdList() { return ardList; }

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

    public static class Marker {
        private final String name;
        private final int lng;
        private final int lat;
    
        public Marker(String name, int lng, int lat) {
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
