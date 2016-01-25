

function deleteTable(id){
	$('#obstructionMask').show();
	$('#deleteTableDiv').show();
	$('#tableId').val(id);
}

function cancelDeleteTable(){
	$('#obstructionMask').hide();
	$('#deleteTableDiv').hide();
}
