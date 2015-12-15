

function showHidePlayerGmLists(radioButton){
	var role = $(radioButton)[0].value;
	if(role=="Maître de jeux"){
		$('#gameMasterList').parent().parent().hide();
		$('#gameMasterLockedList').parent().parent().show();
		$('#playerList').parent().parent().show();
		$('#playerLockedList').parent().parent().hide();
	}
	if(role=="Joueur"){
		$('#gameMasterList').parent().parent().show();
		$('#gameMasterLockedList').parent().parent().hide();
		$('#playerList').parent().parent().hide();
		$('#playerLockedList').parent().parent().show();
	}
	if(role=="Les deux"){
		$('#gameMasterList').parent().parent().hide();
		$('#gameMasterLockedList').parent().parent().show();
		$('#playerList').parent().parent().hide();
		$('#playerLockedList').parent().parent().show();
	}
}