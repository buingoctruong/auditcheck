var $loading = $('#loading');

function uploadData() {
	$loading.show();
	var corpusId = document.getElementById("corpus").value;
	var data = {
		"corpusId": corpusId
	};
	
	$.ajax({
		type: "POST",
		url: "/watson/data",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(data),
		success: function(data, textStatus, jqXHR){
			$loading.hide();
			console.log("====> success : " + textStatus);
	    },
		error: function(jqXHR, textStatus, errorThrown){
	    	console.log("====> fail : " + textStatus);
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
		url: "/watson/training",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(data),
		success: function(data, textStatus, jqXHR){
			$loading.hide();
			console.log("====> success : " + textStatus);
	    },
		error: function(jqXHR, textStatus, errorThrown){
	    	console.log("====> fail : " + textStatus);
        }
	});
}