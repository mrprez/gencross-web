
function displayEditParam(img, key){
	var idSuffix = key.replace(/\./g,"_");
	$('#paramForm_'+idSuffix).show();
	$(img).hide();
}

