/*
 * The MapController class drives the view (HTML elements).
 * It handles all button clicks, marker clicks, updating the elements, etc.
 */
// TODO - this is broken

var MapController = function() {
    var currentMap = null;
    var anchorMode = false;
    var unsavedMapMessage = "The current map has unsaved changes. Do you want to discard all map changes?";
    
    var ANCHORMODE_OFF = "Anchor placement mode: OFF";
    var ANCHORMODE_ON  = "Anchor placement mode: ON";
    
    function getCurrentMarker() {
        var popup = $("#marker-popup");
        var markerElement = popup.data("marker");
        var marker = markerElement.data("marker");
        return marker;
    }
    
    function getCurrentAnchor() {
        var popup = $("#anchor-popup");
        var anchorElement = popup.data("anchor");
        var anchor = anchorElement.data("anchor");
        return anchor;
    }
    
    function showMarkerProperties(elem, marker) {
        var popup = $("#marker-popup");
        popup.data("marker", elem);
        MapController.recalculateMarkerPopup();
        popup.fadeIn();
        $("#marker-name").val(marker.info.name);
        
        var gps_repr = MapCoordHelper.gpsNumbersToRepr(marker.gps);
        var that = this;
        
        $("#marker-lat").val(gps_repr.lat);
        $("#marker-lng").val(gps_repr.lng);
        $("#marker-name").focus();
    }
    
    function showAnchorProperties(elem, marker) {
        var popup = $("#anchor-popup");
        popup.data("anchor", elem);
        MapController.recalculateAnchorPopup();
        popup.fadeIn();
        
        var gps_repr;
        if (marker.gps === null) {
        	// No GPS coordinate yet
        	gps_repr = {"lng": "", "lat": ""};
        } else {
        	gps_repr = MapCoordHelper.gpsNumbersToRepr(marker.gps);
    	}
        var that = this;
        
        $("#anchor-lat").val(gps_repr.lat);
        $("#anchor-lng").val(gps_repr.lng);
        $("#anchor-lat").focus();
    }
    
    function imageLocalToViewLocal(coord, image_width, image_height, view) {
        var view_aspect = view.width()/view.height();
        var image_aspect = image_width/image_height;
        var scale;
        var offset;
        
        if (view_aspect > image_aspect) {
            // space on left and right
            scale = view.height()/image_height;
        } else {
            // space on top and bottom
            scale = view.width()/image_width;
        }
        
        // The image can only be zoomed out, not in
        if (scale > 1) {
            scale = 1;
        }
        
        var w = image_width*scale;
        var h = image_height*scale;
        var x = (view.width() - w)/2;
        var y = (view.height() - h)/2;
        offset = {x: x>0?x:0, y: y>0?y:0};
        
        return {x: coord.x*scale + offset.x, y: coord.y*scale + offset.y};
    }
    
    function mapImageClick(x, y) {
    	if (anchorMode) {
    		// place anchor (no GPS coordinate)
    		currentMap.addAnchor(null, {"x": x, "y": y});
    		generateAnchorElements.call(this);
    	} else {
    		// place marker
	        var transform = currentMap.getMapTransform();
	        
	        /* Broken, don't know if we'll use JACKY TODO
	        if(transform == null){
		        $("#modal_backdrop").fadeIn();
		        $("#alert-message").append("You need three anchors to place markers");
	        	$("#alert-popup").fadeIn();
	        	
	        }*/
	        
            var gps = transform.imageLocalToGPS(x, y);
	        currentMap.addMarker("Untitled marker", gps);
	        generateMarkerElements.call(this);
	        
    	}
    }
    
    function setAnchorMode(mode) {
    	anchorMode = mode;
    	
    	var markers = $("#map-markers");
    	var anchors = $("#map-anchors");
    	if (anchorMode) {
    		generateAnchorElements.call(this);
    		markers.hide();
    		anchors.show();
    	} else {
    		generateMarkerElements.call(this);
    		markers.show();
    		anchors.hide();
    	}

        $("#marker-popup").hide();
        $("#anchor-popup").hide();
    	
    	$("#placement-mode").text(anchorMode?ANCHORMODE_ON:ANCHORMODE_OFF);
    }
    
    // Replaces all marker elements with new ones. This is called when a
    // marker is added or removed.
    function generateMarkerElements() {
    	var list = currentMap.getMarkerList();
    	var className = "marker";
    	var elems = $("#map-markers");

        elems.empty();
        
        var that = this;
    	
        for (var i = 0; i < list.length; i++) {
            var elem = $("<div>", {"class": className});
            elem.click(function() {
            	var markerElem = $(this);
            	var marker = markerElem.data("marker");
            	showMarkerProperties.call(that, markerElem, marker);
            });
            elem.data("marker", list[i]);
            elems.append(elem);
        }
        
        this.recalculateMarkerPositions();
    }
    
    function generateAnchorElements() {
    	var list = currentMap.getAnchorList();
    	var elems = $("#map-anchors");
    	
        elems.empty();
        
        var that = this;
    	
        for (var i = 0; i < list.length; i++) {
        	var hasGPS = list[i].gps !== null;
        	var className = hasGPS ? "anchor" : "anchor-invalid";
        	
            var elem = $("<div>", {"class": className});
            elem.click(function() {
            	var anchorElem = $(this);
            	var anchor = anchorElem.data("anchor");
            	showAnchorProperties.call(that, anchorElem, anchor);
            });
            elem.data("anchor", list[i]);
            elems.append(elem);
        }
        
        this.recalculateAnchorPositions();
    }
    
    return {
    	removeCurrentMarker: function() {
            currentMap.removeMarker(getCurrentMarker());
            var popup = $("#marker-popup");
            popup.hide();
            generateMarkerElements.call(MapController);
        },
        
        removeCurrentAnchor: function() {
            currentMap.removeAnchor(getCurrentAnchor());
            var popup = $("#anchor-popup");
            popup.hide();
            generateAnchorElements.call(MapController);
        },
    	
        // When the page closes, run this function first.
        // This will prevent the page from closing if there are unsaved changes
        unsavedMapPageUnloadEvent: function() {
            if (currentMap !== null && currentMap.hasUnsavedChanges()) {
                return unsavedMapMessage;
            } else {
                return undefined;
            }
        },
        
        // Attempt to close the map. Returns true if saved, false if not
        tryCloseMap: function() {
            if (currentMap !== null && currentMap.hasUnsavedChanges()) {
                // stuff
                return confirm(unsavedMapMessage);
            }
            return true;
        },
        
        loadMap: function(id) {
            if (this.tryCloseMap()) {
            	var persistence = new MapAJAX(id);
                var view = $("#map-view");
                view.hide();
                // load the map...
                var that = this;
                new Map(persistence, function(map, image) {
                    image.id = "map-image";
                    currentMap = map;
                    setAnchorMode.call(that, false);
                    $("#map-image").replaceWith(image);
                    $("#map-image").click(function(e) {
                        var img = $(this);
                        var x = (e.pageX - img.offset().left)*(this.naturalWidth/this.width);
                        var y = (e.pageY - img.offset().top)*(this.naturalHeight/this.height);
                        mapImageClick.call(that, x, y);
                    });
                    
                    generateMarkerElements.call(that);
                    
                    // show the map
                    view.fadeIn();
                }, $("#MapMessage"));
            }
        },
        saveMap: function() {
            if (currentMap !== null) {
                currentMap.save(function(ok, message) {
                	showMessage($("#MapMessage"), ok, message);
                });
            }
        },
        
        toggleAnchorMode: function() {
            if (currentMap !== null) {
                setAnchorMode.call(this, !anchorMode);
            }
        },

        // Markers have moved and the view need to be updated. This is called
        // when the view is resized or the user moves a marker.
        recalculateMarkerPositions: function() {
        	var elements = $("#map-markers .marker");
        	var dataname = "marker";

            function setPosition(elem, x, y) {
                elem.css({"left": x-elem.width()/2, "top": y-elem.height()});
            }
            
            var view = $("#map-view");
            
            var image_width  = currentMap.getImageWidth();
            var image_height = currentMap.getImageHeight();
            
            var transform = currentMap.getMapTransform();
            
            elements.each(function(index, elem) {
                var marker = $(elem).data(dataname);
                var pos = transform.gpsToImageLocal(marker.gps);
    
                var view_pos = imageLocalToViewLocal(pos, image_width, image_height, view);
                setPosition($(this), view_pos.x, view_pos.y);
            });
            
            this.recalculateMarkerPopup();
        },
        
        recalculateAnchorPositions: function() {
        	var elements = $("#map-anchors div");

            function setPosition(elem, x, y) {
                elem.css({"left": x-elem.width()/2, "top": y-elem.height()});
            }
            
            var view = $("#map-view");
            
            var image_width  = currentMap.getImageWidth();
            var image_height = currentMap.getImageHeight();
            
            elements.each(function(index, elem) {
                var anchor = $(elem).data("anchor");
                var pos = anchor.local;
    
                var view_pos = imageLocalToViewLocal(pos, image_width, image_height, view);
                setPosition($(this), view_pos.x, view_pos.y);
            });
            
            this.recalculateAnchorPopup();
        },

        recalculatePositions: function() {
        	if (anchorMode) {
            	this.recalculateAnchorPositions();
        	} else {
            	this.recalculateMarkerPositions();
        	}
        },

        recalculatePopup: function(popup, dataname) {
            var view = $("#map-view");
            
            var marker_elem = popup.data(dataname);
            if (marker_elem !== undefined) {
                // popup is associated with a marker
                var marker_pos = marker_elem.position();
                var pos = marker_elem.position();
                pos.left += marker_elem.width();
                pos.top += marker_elem.height();
                
                // If the popup goes outside of the view, keep it inside
                if (pos.left+popup.outerWidth() >= view.width()) {
                    pos.left = view.width() - popup.outerWidth();
                }
                if (pos.top+popup.outerHeight() >= view.height()) {
                    pos.top = view.height() - popup.outerHeight();
                }
                popup.css(pos);
            }
        },
        
        // Place the marker popup next to the marker it belongs to 
        recalculateMarkerPopup: function() {
        	this.recalculatePopup($("#marker-popup"), "marker");
        },
        
        // Place the marker popup next to the marker it belongs to 
        recalculateAnchorPopup: function() {
        	this.recalculatePopup($("#anchor-popup"), "anchor");
        },
        
        //Allows generation of anchors from different JS pages
        generateAnchors: function() {
        	generateAnchorElements.call(MapController);
        }
    };
}();