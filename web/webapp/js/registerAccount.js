$(function() {
	$("#register_Account").submit(function(event) {
		object = new Object();

		var username = $("#username").val();
		var password = $("#password").val();
		
		object.username = username;
		object.password = password;

		$.ajax({
		 type: "POST",
		 url: "rest/account/register",
		 data: JSON.stringify(object),
		 contentType: "application/json; charset=utf-8",
		 complete: ajaxMessageClosure($("#message"))
		});
		
		event.preventDefault();
	});
});
	
