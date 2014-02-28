function showMessage(elem, ok, message) {
	if (ok) {
		showOkMessage(elem, message);
	} else {
		showErrorMessage(elem, message);
	}
}

function showErrorMessage(elem, message) {
	elem.text(message);
	elem.fadeIn();
    elem.removeClass("message_ok");
    elem.addClass("message_error");
}

function showOkMessage(elem, message) {
	elem.text(message);
	elem.fadeIn();
    elem.removeClass("message_error");
    elem.addClass("message_ok");
}

function ajaxMessageClosure(elem) {
	return function(data) {
		var ok = data.status >= 200 && data.status <= 299;
		var json = data.responseJSON;
		
		if (json != null && json.message !== undefined) {
			showMessage(elem, ok, json.message);
		} else {
			showErrorMessage(elem, data.status + ": " + data.statusText);
		}
	}
}