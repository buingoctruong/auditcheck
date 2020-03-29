var $loading = $('#loading');
var $makeIrrelevance = $('#dislike');
var $makeRelevance = $('#like');
var $tableResult = $('.search-list');
var $searchForm = $('.multiple');

$(document).on('change','.up', function(){
	var names = [];
	var length = $(this).get(0).files.length;
    for (var i = 0; i < $(this).get(0).files.length; ++i) {
       names.push($(this).get(0).files[i].name);
    }
    // $("input[name=file]").val(names);
    if(length>2){
      var fileName = names.join(', ');
      $(this).closest('.form-group').find('.form-control').attr("value",length+" files selected");
    }
    else{
      $(this).closest('.form-group').find('.form-control').attr("value",names);
    }
});

function send() {
	var file = document.getElementById('uploadFile').files[0];
	if (file) {
	    var reader = new FileReader();
	    reader.readAsText(file, "UTF-8");
	    reader.onload = function (event) {	    	
	    	var rows = this.result.split('\n');
	    	
	    	var header = rows[0].split(';');
	    	
	    	if (header.length === 2 && header[0] === 'category'
	    			&& header[1] === 'question') {
	    		if (rows.length > 1) {
	    			$loading.show();
	    			var listQuery = [];
		    		for(var i = 1; i < rows.length; i++){
		      	      var item = rows[i].split(';');
		      	      var query = item[0].concat('.',item[1]);
		      	      listQuery.push(query);
		      	    }
		    		watsonRequest(listQuery);
	    		} else {
	    			alert("Don't have any questions in the input file");
	    		}
	    	} else {
	    		alert("The input file is wrong, please check again!");
	    	}
	    };
	    reader.onerror = function (evt) {
	        console.log('====> ' + "error reading file");
	    }
	}
}

function watsonRequest(listQuery) {
	var corpusId = document.getElementById("corpus").value;
	
	var data = {
		"corpusId": corpusId,
		"queries": listQuery
	}
	
	$.ajax({
		type: "POST",
		url: "/queries",
		contentType: "application/json",
		data: JSON.stringify(data),
		success: function(data, textStatus, jqXHR) {
			$loading.hide();
			
			var message = '<table id="results" style="overflow: scroll;"><thead><tr class="header"><th>Query</th><th colspan="2">Result 1</th>\n'
					+ '<th colspan="2">Result 2</th><th colspan="2">Result 3</th><th colspan="2">Result 4</th>\n'
					+ '<th colspan="2">Result 5</th></tr></thead><tbody>\n';
			
			Object.keys(data).forEach(function(key) {
				message = message
						+ '<tr><td>' + key + '</td>\n';
				data[key].forEach(function(item) {
					if (item['confidence']) {
						var numChange = parseFloat(item['confidence']);
						numChange = numChange * 100;
						var confidence = numChange.toFixed(1);
					} else if (item['confidence'] === 0) {
						var confidence  = 0;
					} else {
						var confidence = "N/A";
					}
					
					message = message
						+ '<td><input type="checkbox" class="defaultCheckbox" id="' + item['dataId'] + '"></td>'
						+ '<td><h4>' + item['question'] + '</h4>\n'
						+ '<p>' + item['answer'] + '</p>\n'
						+ '<small>Confidence : ' + confidence + '%</small></td>\n';
				});
				message = message + '</tr>\n';
			});
			message = message
					+ '</tbody></table>\n';
			
			$searchForm.css({'min-height': 60 + 'vh'});
			$tableResult.html(message);
			$makeIrrelevance.show();
			$makeRelevance.show();
	    },
		error: function(jqXHR, textStatus, errorThrown){
        }
	});
}
