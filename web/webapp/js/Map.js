/* 
 * The Map model class contains all data needed to store and show a map.
 * Whenever a new Map is needed, the old object is trashed by the controller
 * and a new one is constructed. This avoids fragile resetting of variables.
 * This contains absolutely NO view logic.
 */
var Map = function(mapPersistence, complete, errorElem) {
    // Constructors, setting "private attributes and methods"
    var markers = [];
    var anchors = [];
    var mapTransform;
    var img;
    var dirty = false;
    
    // Used in persistence loading
    var loader = {
    	loadMarkers: function(m) {
    		markers = m;
    	},
    	loadAnchors: function(a) {
    		anchors = a;
			mapTransform = MapCoordHelper.getImageLocalGPSTransform(anchors);
    	},
    	loadImage: function(i) {
    		img = i;
    	}
    };
    
    // Equivalent to "Public methods"
    var pub = {
        getImageWidth: function() {
            return img.naturalWidth;
        },
        getImageHeight: function() {
            return img.naturalHeight;
        },
        // Saves the Map anchors and markers using the PUT /map controller
        save: function(onComplete) {
        	mapPersistence.save(markers, anchors, function(ok, message) {
        		if (ok) {
        			dirty = false;
        		}
        		onComplete(ok, message);
        	});
        },
        // Returns true if a marker was added, false if not enough anchors are defined
        addMarker: function(name, gps) {
            if (anchors.length < 3) {
                return false;
            } else {
                var marker = {gps: gps, info: {name: name}};
                markers.push(marker);
                dirty = true;
                return true;
            }
        },
        removeMarker: function(marker) {
            var idx = markers.indexOf(marker);
            if (idx >= 0) {
                markers.splice(idx, 1);
            } else {
                throw new Error("Tried to remove marker, but it doesn't exist");
            }
            dirty = true;
        },
        addAnchor: function(gps, local) {
            anchors.push({gps: gps, local: local});
            mapTransform = MapCoordHelper.getImageLocalGPSTransform(anchors);
            dirty = true;
        },
        removeAnchor: function(anchor) {
            var idx = anchors.indexOf(anchor);
            if (idx >= 0) {
                anchors.splice(idx, 1);
                mapTransform = MapCoordHelper.getImageLocalGPSTransform(anchors);
            } else {
                throw new Error("Tried to remove anchor, but it doesn't exist");
            }
            dirty = true;
        },
        // Get an array of all the markers and return the array
        getMarkerList: function() {
            return markers;
        },
        // Get an array of all the anchors and return the array
        getAnchorList: function() {
            return anchors;
        },
        getMapTransform: function() {
            return mapTransform;
        },
        hasUnsavedChanges: function() {
            return dirty;
        }
    };
    
    mapPersistence.load(pub, loader, errorElem, complete);
};
