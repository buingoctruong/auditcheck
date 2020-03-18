var $chatElement = $('.search-list');
var $question = $('#question');
var $loading = $('#loading');

function send() {
	$loading.show();
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
			
			if (null == data || data.length == 0) {
				message = message
						+ '<h3>No Results</h3>';
			} else {
				message = message
						+ '<h3>Candidates Results</h3>\n'
						+ '<table class="table" id="myTable">\n'
						+ '<thead><tr><th width="30%">Question</th>\n'
						+ '<th width="10%">Category</th><th width="50%">Answer</th>\n'
						+ '<th width="10%">Confidence</th></tr></thead><tbody>\n';
				
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
					
					message = message
							+ '<tr><td>\n'
							+ data[i].question + '</td><td>\n'
							+ data[i].category + '</td><td>\n'
							+ data[i].answer + '</td><td>\n'
							+ confidence + '</td></tr>\n';
				}
				message = message
						+ '</tbody></table>\n';
			}
			$chatElement.html(message);
	    },
		error: function(jqXHR, textStatus, errorThrown){
        }
	});
}