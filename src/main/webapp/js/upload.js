
function hideDisplayPassword(checkbox){
	if(checkbox.checked){
		$('#mjPassword').parent().parent().show();
	}else{
		$('#mjPassword').parent().parent().hide();
	}
}
