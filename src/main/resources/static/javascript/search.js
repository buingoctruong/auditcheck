var $chatElement = $('.search-list');
var $loading = $('#loading');
var $dislike = $('#dislike');
var listDataId = [];

function send() {
	$loading.show();
	$chatElement.hide();
	var corpusId = document.getElementById("corpus").value;
	var query = document.getElementById("inputTextForm").value;

	var data = {
		"corpusId": corpusId,
		"query": query.trim()
	};
	
	$.ajax({
		type: "POST",
		url: "/search",
		contentType: "application/json",
		data: JSON.stringify(data),
		success: function(data, textStatus, jqXHR){
			$loading.hide();			
			var message = '';
			available = data;
			
			if (null == data || data.length == 0) {
				message = message
						+ '<h3>No Results</h3>\n';
			} else {
				message = message
						+ '<h3>Candidates Results</h3>\n'
						+ '<div class="list-group">\n';
						
				for (var i = 0; i < data.length; i++) {
					if (data[i].confidence) {
						var numChange = parseFloat(data[i].confidence);
						numChange = numChange * 100;
						var confidence = numChange.toFixed(1);
					} else if (data[i].confidence === 0) {
						var confidence  = 0;
					} else {
						var confidence = "N/A";
					}
					
					listDataId.push(data[i].dataId);
					
					message = message
							+ '<div class="list-group-item flex-column align-items-start">\n'
							+ '<div class="float-left"><div class="d-flex w-100 justify-content-between">\n'
							+ '<h5 class="mb-1">\n'
							+ data[i].question + '</h5></div><p class="mb-1">\n'
							+ data[i].answer + '</p><small>Confidence : \n'
							+ confidence + '%</small></div><div><button id="\n'
							+ data[i].dataId + '" type="button" class="btn btn-danger float-right" onclick="makeRelevance(\''
							+ query + '\',' + data[i].dataId + ')">Like</button></div></div>\n';
				}
				message = message + '</div>\n';
				message = message + '<div style="margin-top:10px; float:right"><button id="dislike" type="button" class="btn btn-danger" onclick="makeIrrelevance()">Dislike</button><div>\n';
			}
			$chatElement.html(message);
			$chatElement.show();
	    },
		error: function(jqXHR, textStatus, errorThrown){
        }
	});
}

function makeRelevance(question, dataId) {
	var corpusId = document.getElementById("corpus").value;
	
	var data = {
		"corpusId": corpusId,
		"category": "",
		"question": question,
		"dataId": dataId,
		"relevance": 10
	};
	
	$.ajax({
		type: "POST",
		url: "/feedback/relevance",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(data),
		success: function(data, textStatus, jqXHR){
			$('#dislike').prop('disabled', true);
			console.log('====> success');
	    },
		error: function(jqXHR, textStatus, errorThrown){
	    	console.log('====> fail');
        }
	});
}

function makeIrrelevance() {
	var corpusId = document.getElementById("corpus").value;
	var query = document.getElementById("inputTextForm").value;
	var data = {
			"question": query,
			"category": "",
			"listDataId": listDataId,
			"corpusId": corpusId,
			"relevance": 0
	};
	$.ajax({
		type: "POST",
		url: "/feedback/irrelevance",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(data),
		success: function(data, textStatus, jqXHR){
			listDataId = [];
			console.log('====> success');
	    },
		error: function(jqXHR, textStatus, errorThrown){
	    	console.log('====> fail');
        }
	});
}