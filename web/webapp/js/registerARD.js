$(function() {
	$("#device-register").click(function() {
		var code = $("#code").val();
		
		$.ajax({
			type: "POST",
			url: ctx+"/api/ard",
			data: code,
			contentType: "text/plain",
			complete: ajaxMessageClosure($("#message"))
		});
	});
	
	$("#device-remove").click(function() {
		$.ajax({
			type: "DELETE",
			url: ctx+"/api/ard",
			contentType : "application/json; charset=utf-8",
			complete: ajaxMessageClosure($("#message"))
		});
	});
	
	function updateARD() {
		$.ajax({
			type: "GET",
			url: ctx+"/api/ard",
			contentType : "application/json; charset=utf-8",
			complete: ajaxMessageClosure($("#message"))
		});
	}
	
	updateARD();
});
