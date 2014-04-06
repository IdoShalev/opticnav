function createElemMessagable(elem, elemLoader) {
	var jQueryElem;
	var jQueryElemLoader;
	
	if (elem.substring) {
		// a JavaScript hack to check if a type is a string
		// create a jQuery element out of it
		jQueryElem = $(elem);
		jQueryElemLoader = elemLoader ? $(elemLoader) : undefined;
	} else {
		// assume it's a jQuery element
		jQueryElem = elem;
		jQueryElemLoader = elemLoader;
	}
	
	function showMessage(ok, message) {
		if (jQueryElemLoader) {
			jQueryElemLoader.fadeOut();
		}
		jQueryElem.text(message);
		jQueryElem.fadeIn();
		if (ok) {
			jQueryElem.removeClass("message_error");
			jQueryElem.addClass("message_ok");
		} else {
			jQueryElem.removeClass("message_ok");
			jQueryElem.addClass("message_error");
		}
	}
	
	function clearMessage() {
		if (jQueryElemLoader) {
			jQueryElemLoader.fadeOut();
		}
		jQueryElem.fadeOut();
	}
	
	function loadingMessage() {
		if (jQueryElemLoader) {
			jQueryElemLoader.fadeIn();
		}
	}
	
	return {"showMessage": showMessage, "clearMessage": clearMessage, "loadingMessage": loadingMessage};
}

function createAlertMessagable() {
	function showMessage(ok, message) {
		alert(message);
	}
	
	return {"showMessage": showMessage};
}

function showMessage(messagable, ok, message) {
	messagable.showMessage(ok, message);
}

function showErrorMessage(messagable, message) {
	messagable.showMessage(false, message);
}

function showOkMessage(messagable, message) {
	messagable.showMessage(true, message);
}

function ajaxMessageClosure(messagable, callback) {
	// probably shouldn't do this here. what if the closure is not requested the same time as the AJAX request?
	// (i don't think it ever is, currently)
	if (messagable.loadingMessage) {
		messagable.loadingMessage();
	}
	
	return function(data) {
		var ok = data.status >= 200 && data.status <= 299;
		var json = data.responseJSON;
		
		var message;
		
		if (json != null && json.message !== undefined) {
			message = json.message;
		} else {
			ok = false;
			message = data.status + ": " + data.statusText;
		}
		messagable.showMessage(ok, message);
		if (callback != undefined) {
			callback(ok, message);
		}
	}
}

function ajaxMessageClosureRedirectOnSuccess(messagable, location, messageParam) {
	if (messagable.loadingMessage) {
		messagable.loadingMessage();
	}
	
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
				messagable.showMessage(ok, json.message);
			} else {
				messagable.showMessage(false, data.status + ": " + data.statusText);
			}
		}
	}
}

function ajaxMessageClosureOnError(messagable, success){
	if (messagable.loadingMessage) {
		messagable.loadingMessage();
	}
	
	return function(data) {
		var ok = data.status >= 200 && data.status <= 299;
		var json = data.responseJSON;
		
		if (ok) {
			if (messagable.clearMessage) {
				// if the messagable can be cleared, do so
				messagable.clearMessage();
			}
			success(json);
		} else {
			if (json != null && json.message !== undefined) {
				messagable.showMessage(ok, json.message);
			} else {
				messagable.showMessage(false, data.status + ": " + data.statusText);
			}
		}
	}
}
