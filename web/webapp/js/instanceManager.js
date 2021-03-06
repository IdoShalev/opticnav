/**
 * Handles the functionality in the Instance Manager.
 * 
 * @author Danny, Ido
 */
var usersList = [];
var selectedMapId;

var slided = false;

//Create a sliding effect when starting an instance or stopping one
var sectionsSlider = {
	init: function() {
		var wrapper = $("#instance-wrapper");
		wrapper.hide();
	},
	setWidths: function() {
		var containerWidth = $("#instanceContainer").width();
		
		var wrapper = $("#instance-wrapper");
		var children = $("#instance-wrapper > *");
		
		wrapper.css("width", children.length * containerWidth);
		
		children.each(function() {
			$(this).css("width", containerWidth);
		});
	},
	setIndex: function(index) {
		var wrapper = $("#instance-wrapper");
		var v = -index*100+"%";
		
		if (!slided) {
			wrapper.css("margin-left", v);
			wrapper.show();
		} else {
			wrapper.animate({"margin-left": v});
		}
		slided = true;
	}
}

$(function(){
	$(window).resize(function() {
		sectionsSlider.setWidths();
	});
	
	sectionsSlider.init();
	sectionsSlider.setWidths();
	
	var messagable = createElemMessagable("#generic-message");
	var inviteMessagable = createElemMessagable("#start-instance-message", "#invite-loader");
	var startInstanceMessagable = createElemMessagable("#start-instance-message", "#start-instance-loader");
	var stopInstanceMessagable = createElemMessagable("#instance-info-message", "#stop-instance-loader");
	var mapSelection = $("#mapSelection");
	//Retrieve and print the instance information
	function getCurrentInstances() {
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
				} else {
					useInstanceController();
				}
			})
		});
	}
	
	var currentUser;
	$.ajax({
		type : "GET",
		url : ctx + "/api/account/current",
		contentType : "application/json; charset=utf-8",
		complete : ajaxMessageClosureOnError(messagable, function(json) {
			// XXX - race condition
			currentUser = {"username": json.username, "id": json.id};
		})
	});
	
	getCurrentInstances();
	
    $("#stop-instance").click(function() {
    	$.ajax({
    		type : "DELETE",
    		url : ctx + "/api/instance",
    		contentType : "application/json; charset=utf-8",
    		complete : ajaxMessageClosureOnError(stopInstanceMessagable, function(json) {
    			useInstanceController();
    		})
    	});
    })
    //Start an instance if map is defined
    $("#instaB").click(function() {
    	var invited = $("inst-selected-users");
    	var invitedList = [];
    	for (var i = 0; i < usersList.length; i++) {
    		invitedList.push(usersList[i].id);
    	}
    	if (selectedMapId == undefined) {
			showErrorMessage(startInstanceMessagable, "A map must be selected");
		}else {
	    	var instance_data = {"map_id":selectedMapId,"accounts":invitedList};
	    	// TODO: load the instance
			// POST /api/instance/
			$.ajax({
				type : "POST",
				url : ctx + "/api/instance/",
				data : JSON.stringify(instance_data),
				contentType : "application/json; charset=utf-8",
				complete : ajaxMessageClosureOnError(startInstanceMessagable, function(json) {
					getCurrentInstances();
				})
			});
		}
    });
    //Load the list of maps to the website interface
	loadMapsListAJAX(startInstanceMessagable, function(maps) {
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
	    if(e.keyCode == 13) {
	    	instanceInvite();
	    }
	});
	
	$("#inst-invite").click(function() {
		instanceInvite();
	});
	
	//Gets a name from the user and, if valid, add the name of the account to a list
	function instanceInvite(){
		var username = $("#invite-to-inst").val();
		if (username === "") {
			showErrorMessage(inviteMessagable,"A username is required");
		}else {
		// GET /account/query/{username}
			$.ajax({
				type : "GET",
				url : ctx + "/api/account/query/" + encodeURIComponent(username),
				contentType : "application/json; charset=utf-8",
				complete : ajaxMessageClosureOnError(inviteMessagable, function(json) {
					if (json.id == currentUser.id) {
						showErrorMessage(startInstanceMessagable,"You can't invite yourself");
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
	//Create a list of users into the invited user list
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
	//Add an id to the delete button
	function removeFromList(id, usersList) {
		for (var i = 0; i < usersList.length; i++) {
			if (id === usersList[i].id) {
				usersList.splice(i,1);
				createList(usersList);
			}
		}
	}
	
	var mapList = $("#map-list");
	
    mapList.hide();
    mapList.empty();
    //Load the list of maps that the user might have
    loadMapsListAJAX($("#MapMessage"), function(maps) {
        function addEntry(name, id) {
            /*
			 * Append a <entry> surrounded by an <a> Example output: <a
			 * href="javascript:loadMap(1)"><entry>Map 1</entry></a>
			 */
        	var entry = $('<entry>', {"mapID": id});
        	entry.text(name);
        	entry.click(function() {
        		selectMap(id);
        	});
        	
            mapList.append(entry);
        }
	    for (var i = 0; i < maps.length; i++) {
		    var map = maps[i];
		    addEntry(map.name, map.id);
	    }

		mapList.fadeIn();
    });
    //Slides the screen to the instance controller to start an instance
    function useInstanceController() {
    	sectionsSlider.setIndex(0);
    	$("#stop-instance").attr("disabled", true);
    	$("#instaB").attr("disabled", false);
    }
    //Slides the screen to the information section when an instance is started
    function useInstanceInfo() {
    	sectionsSlider.setIndex(1);
    	$("#instaB").attr("disabled", true);
    	$("#stop-instance").attr("disabled", false);
    }
});
//Saves the current selected map's Id into a global variable
//This is used to make sure the instance starts with the selected map
function selectMap(id) {
	// Unselect all other entries
	$("entry").removeClass("selected");
	// Selct a specific entry
	var elem = $("entry[mapID='"+id+"']")
    elem.addClass("selected");
    selectedMapId = id;
}
