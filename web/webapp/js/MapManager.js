/* JavaScript is not object-oriented, but can mimic OOP patterns
 * For more on OO JavaScript, take a gander:
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Introduction_to_Object-Oriented_JavaScript
 */

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
    
    // begin GET /map Make this into Ajax Request Jacky ahaha
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
    // end GET/map
}

function createMap() {
    var modal_backdrop = $("#modal-backdrop");
    var map_creation = $("#map-creation");
    modal_backdrop.fadeIn();
    map_creation.fadeIn();
}

$(function() {
    // When the map view resizes, the marker locations need to change
    $("#map-view").resize(function() {
        MapController.recalculateMarkerPositions();
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
    
    // Prompt the user if there are any unsaved changes
    $(window).on('beforeunload', MapController.unsavedMapPageUnloadEvent);
    
    // Load the map list
    loadMapsList();
});
