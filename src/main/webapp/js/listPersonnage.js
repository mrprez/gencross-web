
function askMJPassword(form){
	var password = prompt("En tant que MJ, vous devez choisir un mot de passe pour ce fichier:");
	var passwordField = $(form).children('[name="password"]')[0];
	passwordField.value = password;
}
