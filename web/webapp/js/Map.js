/* 
 * The Map model class contains all data needed to store and show a map.
 * Whenever a new Map is needed, the old object is trashed by the controller
 * and a new one is constructed. This avoids fragile resetting of variables.
 * This contains absolutely NO view logic.
 */
var Map = function(id, complete, errorElem) {
    // Constructors, setting "private attributes and methods"
    var markers = [];
    var anchors = [];
    var mapTransform;
    var img;
    var dirty = false;
    
    // Equivalent to "Public methods"
    var pub = {
        getImageWidth: function() {
            return img.naturalWidth;
        },
        getImageHeight: function() {
            return img.naturalHeight;
        },
        // Saves the Map anchors and markers using the POST /map controller
        save: function() {
            dirty = false;
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

    
    $.ajax({
		type: "GET",
		url: ctx+"/api/map/"+id,
		contentType: "application/json; charset=utf-8",
		complete: ajaxMessageClosureOnError(errorElem, function(json) {
			json.imageResource;
			markers = json.marker;
			anchors = json.anchor;
			anchors = [{"gps":{"lng":-41072424,"lat":18383378},"local":{"x":463,"y":346}},{"gps":{"lng":-41071669,"lat":18383259},"local":{"x":714,"y":409}},{"gps":{"lng":-41071005,"lat":18383656},"local":{"x":937,"y":200}}];
			mapTransform = MapCoordHelper.getImageLocalGPSTransform(anchors);
			
			img = new Image();
			img.src = ctx+"/api/resource/"+json.imageResource;
			
			img.onload = function() {
			    // everything has completed loading
			    complete(pub, img);
			}
		})
	});
};
