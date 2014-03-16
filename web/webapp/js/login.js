$(function() {
	$("#login").submit(function(event) {
		object = new Object();

		var username = $("#username").val();
		var password = $("#password").val();
		
		object.username = username;
		object.password = password;

		$.ajax({
		 type: "POST",
		 url: ctx+"/api/account/login",
		 data: JSON.stringify(object),
		 contentType: "application/json; charset=utf-8",
		 complete: ajaxMessageClosureOnError($("#message"), function() {
			 window.location.replace(ctx);
		 })
		});
		
		event.preventDefault();
	});
});
	
