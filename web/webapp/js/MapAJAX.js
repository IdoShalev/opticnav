var MapAJAX = function(id) {
	return {
		load : function(map, loader, errorElem, onComplete) {
			$.ajax({
				type : "GET",
				url : ctx + "/api/map/" + id,
				contentType : "application/json; charset=utf-8",
				complete : ajaxMessageClosureOnError(errorElem, function(json) {
					loader.loadMarkers(json.marker);
					// loader.loadAnchors(json.anchor);
					var anchors = [ {
						"gps" : {
							"lng" : -41072424,
							"lat" : 18383378
						},
						"local" : {
							"x" : 463,
							"y" : 346
						}
					}, {
						"gps" : {
							"lng" : -41071669,
							"lat" : 18383259
						},
						"local" : {
							"x" : 714,
							"y" : 409
						}
					}, {
						"gps" : {
							"lng" : -41071005,
							"lat" : 18383656
						},
						"local" : {
							"x" : 937,
							"y" : 200
						}
					} ];
					loader.loadAnchors(anchors);

					img = new Image();
					img.src = ctx + "/api/resource/" + json.imageResource;

					img.onload = function() {
						// everything has completed loading
						loader.loadImage(img);
						onComplete(map, img);
					}
				})
			});
		},

		save : function(markers, anchors, onComplete) {
			// TODO - move this somewhere else
			var ajaxClosure = function(messageCallback) {
				return function(data) {
					var ok = data.status >= 200 && data.status <= 299;
					var json = data.responseJSON;

					messageCallback(ok, json);
				};
			};

			var object = {
				"markers" : markers,
				"anchors" : anchors
			};

			$.ajax({
				type : "PUT",
				url : ctx + "/api/map/" + id,
				data : JSON.stringify(object),
				contentType : "application/json; charset=utf-8",
				complete : ajaxClosure(function(ok, json) {
					onComplete(ok, json.message);
				})
			});
		}
	};
};

function loadMapsList(errorElem, mapsListCallback) {
	$.ajax({
		type : "GET",
		url : ctx + "/api/map",
		contentType : "application/json; charset=utf-8",
		complete : ajaxMessageClosureOnError(errorElem, function(maps) {
			mapsListCallback(maps);
		})
	});
}

loadMapsList($("#error-message-box"), function(maps) {
	// add each map to a list or something...
	for (var i = 0; i < maps.length; i++) {
		var map = maps[i];
		
	}
});
