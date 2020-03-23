var $chatElement = $('.search-list');
var $loading = $('#loading');
var $dislike = $('#dislike');
var listDataId = [];
var $searchForm = $('.s003');

function send() {
	var corpusId = document.getElementById("corpus").value;
	if (corpusId === '0') {
		alert("Please! Enter the environment!");
	} else {
		var query = document.getElementById("inputTextForm").value;
		query = trimmable(query);
		if (!query) {
			alert("Please! Enter the question!");
		} else {
			query = escapeHtml(query);
			$loading.show();
			$chatElement.hide();
			
			var data = {
				"corpusId": corpusId,
				"query": query
			};
			watsonRequest(data, query);
		}
	}
}

function watsonRequest(data, query) {
	$.ajax({
		type: "POST",
		url: "/query",
		contentType: "application/json",
		data: JSON.stringify(data),
		success: function(data, textStatus, jqXHR) {
			$loading.hide();			
			var message = '<section class="pt-2"><div class="container">\n'
					+ '<div class="row my-3"><div class="col-md-12">\n';
			
			available = data;
			
			if (null == data || data.length == 0) {
				message = message
						+ '<h4>No Results</h4></div></div></div></section>\n';
						
			} else {
				message = message
						+ '<h4>Candidates Results</h4></div></div>\n';
				
				
				listDataId = [];
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
							+ '<div class="row mb-3"><div class="col-md-12"><div class="card">\n'
							+ '<div class="card-body"><div class="row "><div class="col-md-11"><h4>\n'
							+ data[i].question + '</h4><p>\n'
							+ data[i].answer + '</p><small>Confidence : \n'
							+ confidence + '%</small></div><div class="col-md-1">\n'
							+ '<div class="sub-row"><button type="button" class="btn btn-danger" '
							+ 'onclick="makeRelevance(\'' + query + '\',' + data[i].dataId + ')" '
							+ 'style="position: absolute; bottom: 0;">Like</button>\n'
							+ '</div></div></div></div></div></div></div>\n';
				}
				message = message + '</div></section>\n';
				message = message
						+ '<br><div><div class="col-md-11">\n'
						+ '<button id="dislike" type="button" class="btn btn-danger" '
						+ 'style="float: right; margin-top: 10px; margin-bottom: 10px;" '
						+ 'onclick="makeIrrelevance()">Dislike</button></div></div>';
			}
			$searchForm.css({'min-height': 60 + 'vh'});
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
			console.log('====> success : ' + textStatus);
	    },
		error: function(jqXHR, textStatus, errorThrown){
	    	console.log('====> fail : ' + textStatus);
        }
	});
}

function makeIrrelevance() {
	var corpusId = document.getElementById("corpus").value;
	var query = document.getElementById("inputTextForm").value;
	query = trimmable(query);
	query = escapeHtml(query);
	
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
			console.log('====> success : ' + textStatus);
	    },
		error: function(jqXHR, textStatus, errorThrown){
	    	console.log('====> fail : ' + textStatus);
        }
	});
}