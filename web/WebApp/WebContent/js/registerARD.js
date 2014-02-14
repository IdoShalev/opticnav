$(function() {
	$("#RegisterARD").submit(function(event) {
		var code = $("#code").val();
		
		var credentials = {code: code};
		
		performOpWithMessage("op/RegisterARD", credentials, "message",
			function(success) {
				if (success) {
					updateARDs();
				}
			});
		
		event.preventDefault();
	});
	
	function updateARDs() {
        $.post("op/ARDInfo",function(data) {
        	var id = data.id;
        	var name = data.name;
        	$('#ard_id').text(id);
        	$('#ard_name').text(name);
        }, "json");
	}
	
	updateARDs();
});
