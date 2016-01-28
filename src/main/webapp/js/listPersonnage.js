
function askMJPassword(form){
	var password = prompt("En tant que MJ, vous devez choisir un mot de passe pour ce fichier:");
	var passwordField = $(form).children('[name="password"]')[0];
	passwordField.value = password;
}

function deletePersonnagePJ(imageForm){
	var validCallback = function(){
		$(imageForm).parent().submit();
	};
	showConfirm(
			'Voulez-vous supprimer ce personnage?\n Si ce personnage a un Maître de Jeux autre que vous, il ne sera pas supprimer pour celui-ci.',
			validCallback
	);
}

function deletePersonnageMJ(imageForm){
	var validCallback = function(){
		$(imageForm).parent().submit();
	};
	showConfirm(
			'Voulez-vous supprimer ce personnage?\n Si ce personnage a un Joueur autre que vous, il ne sera pas supprimer pour celui-ci.',
			validCallback
	);
}
