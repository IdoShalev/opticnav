$(function(){
	var errorElem = $("#message");
	var mapSelection = $("#mapSelection");
	
	loadMapsListAJAX(errorElem, function(maps) {
		for (var i = 0; i < maps.length; i++) {
			var map = maps[i];
			
			var optionElem = $("<option>", {"value": map.id});
			optionElem.text(map.name);
			
			mapSelection.append(optionElem);
		}
	});
	
	mapSelection.change(function() {
		var id = parseInt($(this).val());
		console.log("Changed to " + id);
		// TODO: load the instance
		// GET /api/instance/{id}
	});
	
	$("#inst-invite").click(function() {
		var username = $("#invite-to-inst").val();
		var selectedUsers = $("#inst-selected-users");
		// GET /account/query/{username}
		$.ajax({
			type : "GET",
			url : ctx + "/api/account/query/" + encodeURIComponent(username),
			contentType : "application/json; charset=utf-8",
			complete : ajaxMessageClosureOnError(errorElem, function(json) {
				var li = $("<li>");
				li.text(json.username);
				
				selectedUsers.append(li);
			})
		});
	});
	
var mapList = $("#map-list");
    
    function addEntry(name, id) {
        /* Append a <entry> surrounded by an <a>
         * Example output: <a href="javascript:loadMap(1)"><entry>Map 1</entry></a> */
        mapList.append($('<a>', {href:'javascript:MapController.loadMap('+id+')'})
                        .append($('<entry>', {}).text(name)));
    }
    
    mapList.hide();
    mapList.empty();

    loadMapsListAJAX($("#MapMessage"), function(maps) {
	    for (var i = 0; i < maps.length; i++) {
		    var map = maps[i];
		    addEntry(map.name, map.id);
	    }

		mapList.fadeIn();
    });
});