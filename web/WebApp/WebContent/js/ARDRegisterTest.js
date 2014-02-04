$(function() {
	$("#RegisterARD").submit(function(event) {
		var code = $("#code").val();
		
		var credentials = {code: code};
		
		$.post("op/RegisterARD", credentials,function(data) {
			if (data.successful){
				showSuccessMessage("message", data.message);
				updateARDS;
			}
				showErrorMessage("message", data.message);
		}, "json");
		
		event.preventDefault();
	});
	function updateARDs() {
        $.post("op/ARDInfo",function(data) {
        	var id = data.id;
        	var name = data.name;
        	$('p').html(id+" "+name);
        }, "json");
	}
	$("#ARDInfo").ready(updateARDs);
});