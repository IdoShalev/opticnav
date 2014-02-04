function showErrorMessage(id, message) {
	var elem = $("#" + id);
	elem.text(message);
	elem.slideDown();
	elem.attr('class', 'errorMessage');
	elem.click(function(){
		elem.hide('slow');
	});
}

function showSuccessMessage(id, message) {
	var elem = $("#" + id);
	elem.text(message);
	elem.slideDown();
	elem.attr('class', 'successMessage');
	elem.click(function(){
		elem.hide('slow');
	});
}