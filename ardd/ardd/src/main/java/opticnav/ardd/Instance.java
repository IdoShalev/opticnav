package opticnav.ardd;

public class Instance {

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
