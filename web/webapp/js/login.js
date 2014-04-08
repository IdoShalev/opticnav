/**
 * Java Script for index.jsp handles login verification and the slideshow display
 * 
 * @author Jacky Huynh, Danny Spencer
 */
$(function() {
	var messagable = createElemMessagable("#message", "#message-loader");
	
	$("#login").submit(function(event) {
		object = new Object();

		var username = $("#username").val();
		var password = $("#password").val();
		
		object.username = username;
		object.password = password;
		
		//Request login verification
		$.ajax({
		 type: "POST",
		 url: ctx+"/api/account/login",
		 data: JSON.stringify(object),
		 contentType: "application/json; charset=utf-8",
		 complete: ajaxMessageClosureOnError(messagable, function() {
			 window.location.replace(ctx);
		 })
		});
		
		event.preventDefault();
	});
});
	
//http://snook.ca/archives/javascript/simplest-jquery-slideshow
$(document).ready(function() {
	$('#slideShow div:gt(0)').hide();
	setInterval(function(){
	  $('#slideShow :first-child').fadeOut()
	     .next('div').fadeIn()
	     .end().appendTo('#slideShow');}, 
	  5000);
});

