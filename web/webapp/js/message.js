function performOpWithMessage(url, obj, message_id, callback) {
	$.post(url, obj, function(data) {
		if (data.successful){
			showSuccessMessage(message_id, data.message);
		} else {
			showErrorMessage(message_id, data.message);
		}
		
		if (typeof(callback) !== 'undefined') {
			callback(data.successful);
		}
	}, "json").error(function() {
		showErrorMessage(message_id, "There was an internal server error");
	});
}

function showErrorMessage(id, message) {
	var elem = $("#" + id);
	elem.text(message);
	elem.fadeIn();
	elem.attr('class', 'errorMessage');
}

function showSuccessMessage(id, message) {
	var elem = $("#" + id);
	elem.text(message);
	elem.fadeIn();
	elem.attr('class', 'successMessage');
}