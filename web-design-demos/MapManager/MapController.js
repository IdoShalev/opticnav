/*
 * The MapController class drives the view (HTML elements).
 * It handles all button clicks, marker clicks, updating the elements, etc.
 */
var MapController = function() {
    var currentMap = null;
    var unsavedMapMessage = "The current map has unsaved changes. Do you want to discard all map changes?";
    
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
        $("#delete").click(function(){
            currentMap.removeMarker(marker);
            popup.hide();
            that.generateMarkerElements();
        });
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
        var transform = currentMap.getMapTransform();
        var gps = transform.imageLocalToGPS(x, y);
        currentMap.addMarker("Untitled marker", gps);
        this.generateMarkerElements();
    }
    
    return {
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
                var view = $("#map-view");
                view.hide();
                // load the map...
                var that = this;
                new Map(id, function(map, image) {
                    image.id = "map-image";
                    currentMap = map;
                    $("#map-image").replaceWith(image);
                    $("#map-image").click(function(e) {
                        var img = $(this);
                        var x = (e.pageX - img.offset().left)*(this.naturalWidth/this.width);
                        var y = (e.pageY - img.offset().top)*(this.naturalHeight/this.height);
                        mapImageClick.call(that, x, y);
                    });
                    
                    that.generateMarkerElements();
                    
                    // show the map
                    view.fadeIn();
                });
            }
        },
        saveMap: function() {
            if (currentMap !== null) {
                currentMap.save();
            }
        },
        
        // Replaces all marker elements with new ones. This is called when a
        // marker is added or removed.
        generateMarkerElements: function() {
            var markers = currentMap.getMarkerList();
            
            var markersElem = $("#map-markers");
            markersElem.empty();

            var that = this;
            
            for (var i = 0; i < markers.length; i++) {
                var elem = $("<div>", {class: 'marker'});
                elem.click(function() {
                    showMarkerProperties.call(that, $(this), $(this).data("marker"));
                });
                elem.data("marker", markers[i]);
                markersElem.append(elem);
            }
            
            this.recalculateMarkerPositions();
        },

        // Markers have moved and the view need to be updated. This is called
        // when the view is resized or the user moves a marker.
        recalculateMarkerPositions: function() {
            function setMarkerPosition(elem, x, y) {
                elem.css({left: x, top: y});
            }
            
            var view = $("#map-view");
            var image_width  = currentMap.getImageWidth();
            var image_height = currentMap.getImageHeight();
            
            var transform = currentMap.getMapTransform();
            
            // marker positions
            $("#map-markers .marker").each(function(index, elem) {
                var marker = $(elem).data("marker");
                var pos = transform.gpsToImageLocal(marker.gps);
    
                var view_pos = imageLocalToViewLocal(pos, image_width, image_height, view);
                setMarkerPosition($(this), view_pos.x, view_pos.y);
            });
            
            this.recalculateMarkerPopup();
        },
        
        // Place the marker popup next to the marker it belongs to 
        recalculateMarkerPopup: function() {
            var view = $("#map-view");
            var popup = $("#marker-popup");
            var marker_elem = popup.data("marker");
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
        }
    };
}();
