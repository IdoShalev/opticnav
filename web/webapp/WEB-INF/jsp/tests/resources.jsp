<t:testPage>
<jsp:attribute name="script">
$(function(){
    $("#upload").click(function() {
        var fd = new FormData();
        var file = $("#file").get(0).files[0];
        fd.append("file", file);
        fd.append("name", "Test");
        
        $.ajax({
            type: "POST",
            url: ctx+"/api/resource",
            enctype: "multipart/form-data",
            data: fd,
		    cache: false,
		    contentType: false,
		    processData: false,
            complete: ajaxMessageClosure($("#result")),
            
            // Set callback for upload progress
	        xhr: function() {
	            var myXhr = $.ajaxSettings.xhr();
	            
	            if(myXhr.upload) {
	                myXhr.upload.addEventListener('progress', progressHandlingFunction, false);
	            }
	            return myXhr;
	        }
        });
    });
});

function progressHandlingFunction(e) {
    var per = Math.floor(e.loaded / e.total * 100);
    console.log(e.loaded + "/" + e.total + ": " + per + "%");
}
</jsp:attribute>

<jsp:attribute name="content">

<input type="file" id="file" />
<button id="upload">Upload</button>
<t:message name="result" />
<div id="image"></div>

</jsp:attribute>

</t:testPage>