var usersList = [];
var selectedMapId;
$(function(){
	var messagable = createElemMessagable("#message");
	var mapSelection = $("#mapSelection");
	
	var currentUser;
	useInstanceController();
	$.ajax({
		type : "GET",
		url : ctx + "/api/account/current",
		contentType : "application/json; charset=utf-8",
		complete : ajaxMessageClosureOnError(messagable, function(json) {
			// XXX - race condition
			currentUser = {"username": json.username, "id": json.id};
		})
	});
	
	$.ajax({
		type : "GET",
		url : ctx + "/api/instance",
		contentType : "application/json; charset=utf-8",
		complete : ajaxMessageClosureOnError(messagable, function(json) {
			if (json.length > 0) {
				useInstanceInfo();
				var instance = json[0];
				var mapName = $("#inst-map-name");
				mapName.text(instance.name);
				var startTime = $("#inst-start-time");
				var date = new Date(instance.start_time).format("M d,Y h:i:s A");
				startTime.text(date);
				var instList = $("#inst-selected-users");
				instList.empty();
				for (var i=0; i < instance.ards.length; i++) {
					var $li = $("<li>");
					$li.text(instance.ards[i].name);
					instList.append($li);
				}
			}
		})
	});
	
    $("#stop-instance").click(function() {
    	$.ajax({
    		type : "DELETE",
    		url : ctx + "/api/instance",
    		contentType : "application/json; charset=utf-8",
    		complete : ajaxMessageClosureOnError(messagable, function(json) {
    			useInstanceController();
    		})
    	});
    })
    $("#instaB").click(function() {
    	var invited = $("inst-selected-users");
    	var invitedList = [];
    	for (var i = 0; i < usersList.length; i++) {
    		invitedList.push(usersList[i].id);
    	}
    	if (selectedMapId == undefined) {
			showErrorMessage(messagable,"A map is required");
		}else {
	    	var instance_data = {"map_id":selectedMapId,"accounts":invitedList};
	    	// TODO: load the instance
			// POST /api/instance/
			$.ajax({
				type : "POST",
				url : ctx + "/api/instance/",
				data : JSON.stringify(instance_data),
				contentType : "application/json; charset=utf-8",
				complete : ajaxMessageClosureOnError(messagable, function(json) {
					useInstanceInfo();
			    	
				})
			});
		}
    });

	loadMapsListAJAX(messagable, function(maps) {
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
		
	});
	
	$("#invite-to-inst").keyup(function(e){
	    if(e.keyCode == 13)
	    {
	    	instanceInvite();
	    }
	});
	
	$("#inst-invite").click(function() {
		instanceInvite();
	});
	
	function instanceInvite(){
		var username = $("#invite-to-inst").val();
		if (username === "") {
			showErrorMessage(messagable,"A username is required");
		}else {
		// GET /account/query/{username}
			$.ajax({
				type : "GET",
				url : ctx + "/api/account/query/" + encodeURIComponent(username),
				contentType : "application/json; charset=utf-8",
				complete : ajaxMessageClosureOnError(messagable, function(json) {
					if (json.id == currentUser.id) {
						showErrorMessage(messagable,"You can't invite yourself");
					} else {
						var isInList = false;
						for (var i = 0; i < usersList.length; i++) {
							if (json.id == usersList[i].id) {
								isInList = true;
								break;
							}
						}
						if (!isInList) {
							usersList.push({"username": json.username, "id": json.id});
							createList(usersList);
						}
					}
				})
			});
		}
	}
	
	function createList(usersList) {
		var selectedUsers = $("#inst-select-users");
		selectedUsers.empty();
		for (var i = 0; i < usersList.length; i++) {
			var $li = $("<li>");
			$li.text(usersList[i].username);
			var $delete = $("<span>", {"class":"del"});
			$li.append($delete);
			new function() {
				var id = usersList[i].id;
				$delete.click(function() {
					removeFromList(id,usersList);
				});
			}();
			selectedUsers.append($li);	
		}
	}
	
	function removeFromList(id, usersList) {
		for (var i = 0; i < usersList.length; i++) {
			if (id === usersList[i].id) {
				usersList.splice(i,1);
				createList(usersList);
			}
		}
	}
	
	var mapList = $("#map-list");
	    
    function addEntry(name, id) {
        /* Append a <entry> surrounded by an <a>
         * Example output: <a href="javascript:loadMap(1)"><entry>Map 1</entry></a> */
        mapList.append($('<a>', {href:'javascript:selectMap('+id+')'})
                        .append($('<entry>', {"id":"map-"+id}).text(name)));
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
    function useInstanceController() {
    	$("#info-modal").show();
    	$("#controller-modal").hide();
    	$("#stop-instance").attr("disabled", true);
    	$("#instaB").attr("disabled", false);
    }
    function useInstanceInfo() {
    	$("#info-modal").hide();
    	$("#controller-modal").show();
    	$("#instaB").attr("disabled", true);
    	$("#stop-instance").attr("disabled", false);
    }
});
function selectMap(id) {
	var elem = $("#map-"+id);
	if (selectedMapId != undefined) {
		var selectedElem = $("#map-"+selectedMapId);
		selectedElem.removeClass("selected");
	}
    elem.addClass("selected");
    selectedMapId = id;
}