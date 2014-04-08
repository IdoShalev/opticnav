/**
 * AJAX request for the Map Manager, handles fetching the resources for saved maps.
 * 
 * @author Danny Spencer
 */
var MapAJAX = function(id) {
	return {
		load : function(map, loader, messagable, onComplete) {
			$.ajax({
				type : "GET",
				url : ctx + "/api/map/" + id,
				contentType : "application/json; charset=utf-8",
				complete : ajaxMessageClosureOnError(messagable, function(json) {
					loader.loadMarkers(json.marker);
					loader.loadAnchors(json.anchor);					

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

function loadMapsListAJAX(messagable, mapsListCallback) {
	$.ajax({
		type : "GET",
		url : ctx + "/api/map",
		contentType : "application/json; charset=utf-8",
		complete : ajaxMessageClosureOnError(messagable, function(maps) {
			mapsListCallback(maps);
		})
	});
}

