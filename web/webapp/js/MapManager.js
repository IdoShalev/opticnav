/**
 * Manages the UI elements of the map, Map list, buttons, popups.
 * 
 * @author Jacky Huynh, Danny Spencer
 */
function loadMapsList() {
	//var messagable = createElemMessagable("#MapMessage");
    var mapList = $("#map-list");
    
    function addEntry(name, id) {
    	var entry = $('<entry>', {"mapID":id});
    	entry.text(name);
    	entry.click(function() {
    		MapController.loadMap(id);
    	})
        mapList.append(entry);
    }
    
    mapList.hide();
    mapList.empty();

    loadMapsListAJAX(messagable, function(maps) {
	    for (var i = 0; i < maps.length; i++) {
		    var map = maps[i];
		    addEntry(map.name, map.id);
	    }

		mapList.fadeIn();
    });
}

function createMap() {
    var modal_backdrop = $("#modal-backdrop");
    var map_creation = $("#map-creation");
    modal_backdrop.fadeIn();
    map_creation.fadeIn();
}

function deleteMap() {
	MapController.delMap(function() {
		// map is deleted
		// regenerate the list
		loadMapsList();
	});
}

$(function() {
	var messagable = createElemMessagable("#MapMessage");
	$(function() {
		// hack, hack!

		var m = createElemMessagable("#MapMessage");
		
		messagable = {"showMessage": function(ok, message) {
			m.showMessage(ok, message);
			$('#map-message-container').css("margin-top","10px");
			setTimeout(function() {
				$('#map-message-container').css("margin-top","0px");
				m.clearMessage();
			}, 3000);
		}, "clearMessage": function() {
			m.clearMessage();
		}};
	});
    var modal_backdrop = $("#modal-backdrop");
    var map_creation = $("#map-creation");

    // When the map view resizes, the marker locations need to change
    $(window).resize(function() {
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
        var lng;
        var lat; 
        
        if($("#anchor-lng").val() != "" && $("#anchor-lat").val() != "") {
        	lng = $("#anchor-lng").val();
        	lat = $("#anchor-lat").val();
        }else{
        	//var messagable = createElemMessagable("#message");
        	
        	$("#modal-backdrop").fadeIn();
	        $("#alert-message").append("You can't save an anchor that doesn't have" +
	        		" both longitude and latitude values");
        	$("#alert-popup").fadeIn();
        }
        
        var anchor_elem = popup.data("anchor");
        var anchor = anchor_elem.data("anchor");
        
        anchor.gps = MapCoordHelper.gpsReprToNumbers({"lng": lng, "lat": lat});
        popup.hide();
        MapController.generateAnchors();
    });
    
    $("#anchor-cancel").click(function() {
    	var popup = $("#anchor-popup");
        popup.hide();
    });
    
    $("#marker-cancel").click(function(){
    	var popup = $("#marker-popup");
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
    
    $("#alert-ok").click(function(){
    	modal_backdrop.fadeOut();
    	$("#alert-message").text("");
    	$("#alert-popup").fadeOut();
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
			complete: ajaxMessageClosureOnError(messagable, function() {
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
