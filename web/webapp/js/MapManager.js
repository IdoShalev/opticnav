/* JavaScript is not object-oriented, but can mimic OOP patterns
 * For more on OO JavaScript, take a gander:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Introduction_to_Object-Oriented_JavaScript
 */
//TODO Anchor Save

function loadMapsList() {
    var mapList = $("#map-list");
    
    function addEntry(name, id) {
        /* Append a <entry> surrounded by an <a>
         * Example output: <a href="javascript:loadMap(1)"><entry>Map 1</entry></a> */
        mapList.append($('<a>', {href:'javascript:MapController.loadMap('+id+')'})
                        .append($('<entry>', {}).text(name)));
    }
    
    mapList.hide();
    mapList.empty();
    
    $.ajax({
		 type: "GET",
		 url: ctx+"/api/map",
		 contentType: "application/json; charset=utf-8",
		 complete: ajaxMessageClosureOnError($("#MapMessage"), function(maps) {
			    for (var i = 0; i < maps.length; i++) {
			        var map = maps[i];
			        addEntry(map.name, map.id);
			    }
			    
			    mapList.fadeIn();
		 })
		});
}

function createMap() {
    var modal_backdrop = $("#modal-backdrop");
    var map_creation = $("#map-creation");
    modal_backdrop.fadeIn();
    map_creation.fadeIn();
}

$(function() {
    var modal_backdrop = $("#modal-backdrop");
    var map_creation = $("#map-creation");

    // When the map view resizes, the marker locations need to change
    $("#map-view").resize(function() {
        MapController.recalculatePositions();
    });
    
    // When the user clicks outside of a popup, it should disappear
    $("#marker-popup").focusout(function(e) {
        // setTimeout is an awful hack, but it's needed to get the active element
        setTimeout(function() {
            var p = $(document.activeElement).parents("#marker-popup").length >= 1;
            if (!p) {
                $("#marker-popup").hide();
            }
        }, 1);
    });
    $("#marker-popup").hide();
    $("#anchor-popup").hide();
    
    $("#anchor-save").click(function() {
        var popup = $("#anchor-popup");
        var anchor_elem = popup.data("anchor");
        var anchor = anchor_elem.data("anchor");
        
        var lng = $("#anchor-lng").val();
        var lat = $("#anchor-lat").val();
        
        anchor.gps = MapCoordHelper.gpsReprToNumbers({"lng": lng, "lat": lat});
        popup.hide();
    });
    
    $("#marker-save").click(function(){
    	var popup = $("#marker-popup");
        var marker_elem = popup.data("marker");
        var marker = marker_elem.data("marker");
        
        var marker_name = $("#marker-name").val();
        var lng = $("#marker-lng").val();
        var lat = $("#marker-lat").val();
        
        marker.info.name = marker_name;
        marker.gps = MapCoordHelper.gpsReprToNumbers({"lng": lng, "lat": lat});
        popup.hide();
    });
    
    $("#marker-delete").click(function(){
    	MapController.removeCurrentMarker();
    });
    
    $("#anchor-delete").click(function(){
    	MapController.removeCurrentAnchor();
    });

    //Leaves the Page, we forgot this, stuck forever
    $("#map-creation-cancel").click(function(){
    	modal_backdrop.fadeOut();
        map_creation.fadeOut();
    });
    
    $("#map-creation-create").click(function(){
    	var name= $('#map-creation-name').val();

        var fd = new FormData();
        var file = $("#map-creation-file").get(0).files[0];
        fd.append("file", file);
        
        $.ajax({
            type: "POST",
            url: ctx+"/api/map?name=" + encodeURIComponent(name),
            enctype: "multipart/form-data",
            data: fd,
		    cache: false,
		    contentType: false,
		    processData: false,
			complete: ajaxMessageClosureOnError($("#MapMessage"), function() {
				loadMapsList();
				modal_backdrop.fadeOut();
		        map_creation.fadeOut();
			})
        });
    });
    
    // Prompt the user if there are any unsaved changes
    $(window).on('beforeunload', MapController.unsavedMapPageUnloadEvent);
    
    // Load the map list
    loadMapsList();
});
