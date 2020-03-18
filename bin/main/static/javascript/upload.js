function uploadData() {
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
	        console.log("=====> " + data);
	    },
		error: function(jqXHR, textStatus, errorThrown){
        }
	});
}

function trainingData() {
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
	        console.log("=====> " + data);
	    },
		error: function(jqXHR, textStatus, errorThrown){
        }
	});
}