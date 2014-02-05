$(function() {
	$("#register").submit(function(event) {
		var code = $("#code").val();
		
		var credentials = {code: wXXyZ};
		
		$.post("register", credentials, function(data) {
			if (data.successful) {
				window.location.href = "auth";
			} else {
				showErrorMessage("message", data.message);
			}
		}, "json");
		
		event.preventDefault();
	});
});