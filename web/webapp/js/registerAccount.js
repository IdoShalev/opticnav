$(function() {
	$("#register_Account").submit(function(event) {
		object = new Object();

		var username = $("#username").val();
		var password = $("#password").val();
		
		object.username = username;
		object.password = password;

		$.ajax({
		 type: "POST",
		 url: ctx+"/api/account/register",
		 data: JSON.stringify(object),
		 contentType: "application/json; charset=utf-8",
		 complete: ajaxMessageClosureRedirectOnSuccess($("#message"), "/", "message")
		});
		
		event.preventDefault();
	});
});
	
