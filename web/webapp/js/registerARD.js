$(function() {
	$("#RegisterARD").submit(function(event) {
		var code = $("#code").val();
		
		$.ajax({
			type: "POST",
			url: ctx+"/rest/ard",
			data: code,
			contentType: "text/plain",
			complete: ajaxMessageClosure($("#message"))
		});
		
		event.preventDefault();
	});
	
	function updateARD() {
        $.get("rest/ard",function(data) {
        	var id = data.id;
        	var name = data.name;
        	$('#ard_id').text(id);
        	$('#ard_name').text(name);
        }, "json");
	}
	
	updateARD();
});
