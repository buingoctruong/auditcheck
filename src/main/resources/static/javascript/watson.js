var $loading = $('#loading');
var $tableStatus = $('#tableStatus');


function uploadData() {
	$loading.show();
	var corpusId = document.getElementById("corpus").value;
	var data = {
		"corpusId": corpusId
	};
	
	$.ajax({
		type: "POST",
		url: "/watson/collection/data",
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
		url: "/watson/collection/training",
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

function getStatus() {
	var corpusId = document.getElementById("corpus").value;
	
	$.ajax({
		type: "GET",
		url: "/watson/collection/status/" + corpusId,
		contentType: "application/json; charset=utf-8",
		success: function(data, textStatus, jqXHR){
			console.log("====> success : " + textStatus);
	    },
		error: function(jqXHR, textStatus, errorThrown){
	    	console.log("====> fail : " + textStatus);
        }
	});
	
	
}