

function deleteTable(id){
	$('#waitMask').show();
	$('#deleteTableDiv').show();
	$('#tableId').val(id);
}

function cancelDeleteTable(){
	$('#waitMask').hide();
	$('#deleteTableDiv').hide();
}
