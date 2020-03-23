var $loading = $('#loading');
var $tableStatus = $('#tableStatus');

setInterval(reloadWindow, 10000);

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
			getStatus();
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
			$loading.hide();
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
			var name = data["name"];
			var processing = data["processing"];
			var available = data["available"];
			var dateOfUpdate = '';
			var dataOfTraining = '';
			
			if (data["dateOfUpdate"]) {
				dateOfUpdate = convertMillisecondToDate(
						parseFloat(data["dateOfUpdate"]));
			}
			
			if (available & data["dataOfTraining"]) {
				dataOfTraining = convertMillisecondToDate(
						parseFloat(data["dataOfTraining"]));
			} else if (!available & processing) {
				dataOfTraining = "Training";
			}
			showStatus(name, dateOfUpdate, dataOfTraining);
	    },
		error: function(jqXHR, textStatus, errorThrown){
	    	console.log("====> fail : " + textStatus);
        }
	});
}

function showStatus(name, dateOfUpdate, dataOfTraining) {
	var message = '<thead><tr><th>Collection</th><th>Date Of Update</th>\n'
			+ '<th>Date Of Training</th></tr></thead><tbody><tr>\n'
			+ '<td><strong>' + name + '</strong></td>\n'
			+ '<td>' + dateOfUpdate + '</td>\n'
			+ '<td>' + dataOfTraining + '</td></tr></tbody>';
	
	$tableStatus.html(message);
	$tableStatus.show();
}

function convertMillisecondToDate(millisecond) {
	var myDate = new Date(millisecond);
	var result = myDate.getFullYear() + '/'
			+ ('0' + (myDate.getMonth()+1)).slice(-2)+ '/' 
			+ ('0' + myDate.getDate()).slice(-2) 
			+ ' ' + ('0' + (myDate.getHours())).slice(-2)
			+ ':' + ('0' + (myDate.getMinutes())).slice(-2) 
			+ ':' + ('0' + (myDate.getSeconds())).slice(-2);
	return result;
}

function reloadWindow() {
	var corpusId = document.getElementById("corpus").value;
	if (parseInt(corpusId)) {
		getStatus();
	}
}