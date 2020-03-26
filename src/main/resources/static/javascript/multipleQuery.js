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
	    		var list = [];
	    		for(var i = 1; i < rows.length; i++){
	      	      var item = rows[i].split(';');
	      	      var query = item[0].concat('.',item[1]);
	      	      list.push(query);
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