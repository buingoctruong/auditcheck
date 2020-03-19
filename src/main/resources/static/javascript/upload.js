var $loading = $('#loading');

function uploadData() {
	$loading.show();
	var corpusId = document.getElementById("corpus").value;
	var data = {
		"corpusId": corpusId
	};
	
	$.ajax({
		type: "POST",
		url: "/study/upload",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(data),
		success: function(data, textStatus, jqXHR){
			$loading.hide();
	    },
		error: function(jqXHR, textStatus, errorThrown){
        }
	});
}

function trainingData() {
	$loading.show();
	var corpusId = document.getElementById("corpus").value;
	var data = {
		"corpusId": corpusId
	};
	
	$.ajax({
		type: "POST",
		url: "/study",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(data),
		success: function(data, textStatus, jqXHR){
			$loading.hide();
	    },
		error: function(jqXHR, textStatus, errorThrown){
        }
	});
}