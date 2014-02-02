$(function() {
	$("#login").submit(function(event) {
		var user = $("#user").val();
		var pass = $("#pass").val();
		
		var credentials = {user: user, pass: pass};
		
		$.post("Login", credentials, function(data) {
			if (data.successful) {
				window.location.href = "auth";
			} else {
				showErrorMessage("message", data.message);
			}
		}, "json");
		
		event.preventDefault();
	});
});