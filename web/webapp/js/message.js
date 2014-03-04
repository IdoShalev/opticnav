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



function ajaxMessageClosureRedirectOnSuccess(elem, location, messageParam) {
	return function(data) {
		var ok = data.status >= 200 && data.status <= 299;
		var json = data.responseJSON;
		
		if (ok) {
			// This is an ugly hack, but it'll work for now
			var messageParam_ok = messageParam+"_ok";
			var ok_val = ok ? "1" : "0";
			window.location.href = ctx+location+"?"+messageParam_ok+"="+ok_val
					+"&"+messageParam+"="+json.message;
		} else {
			if (json != null && json.message !== undefined) {
				showMessage(elem, ok, json.message);
			} else {
				showErrorMessage(elem, data.status + ": " + data.statusText);
			}
		}
	}
}

function ajaxMessageClosureOnError(elem, success){
	return function(data) {
		var ok = data.status >= 200 && data.status <= 299;
		var json = data.responseJSON;
		
		if (ok) {
			success(json);
		} else {
			if (json != null && json.message !== undefined) {
				showMessage(elem, ok, json.message);
			} else {
				showErrorMessage(elem, data.status + ": " + data.statusText);
			}
		}
	}
}
