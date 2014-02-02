function showErrorMessage(id, message) {
	var elem = $("#" + id);
	elem.text(message);
	elem.slideDown();
}