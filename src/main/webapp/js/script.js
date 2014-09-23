
var host = window.location.href;
host = host.substring(0, host.lastIndexOf("/"));

function selectGenerator(selectList){
	$('.templateFileList').hide();
	$('.selectTemplate').attr("name", "");
	var index = selectList.selectedIndex;
	var generator = selectList.options[index].value;
	$('#'+generator+'_templatesEl').show();
	$('#'+generator+'_templates').attr("name", "selectedTemplate");
	selectTemplateFile($('#'+generator+'_templates')[0]);
}

function selectTemplateFile(selectList){
	$('#uploadTemplateFile').hide();
	if(selectList!=null){
		var index = selectList.selectedIndex;
		if(index==selectList.options.length-1){
			$('#uploadTemplateFile').show();
		}
	}
}

function askMJPassword(form){
	var password = prompt("En tant que MJ, vous devez choisir un mot de passe pour ce fichier:");
	var passwordField = $(form).children('[name="password"]')[0];
	passwordField.value = password;
}

function hideDisplayPassword(checkbox){
	if(checkbox.checked){
		$('#mjPassword').parent().parent().show();
	}else{
		$('#mjPassword').parent().parent().hide();
	}
}

function showSubMenu(tdMenu){
	$(tdMenu).children('ul').show('fast');
}

function showHideMessage(authorSpan){
	var messageContent = $(authorSpan).parent().parent().children('.messageContent');
	var image = $(authorSpan).children('img')[0];
	var imgSrc = image.src;
	if(imgSrc.indexOf("img/expandable.jpg")>=0){
		messageContent.show();
		image.src = imgSrc.substring(0, imgSrc.lastIndexOf("expandable.jpg"))+"expanded.jpg";
	}else{
		messageContent.hide();
		image.src = imgSrc.substring(0, imgSrc.lastIndexOf("expanded.jpg"))+"expandable.jpg";
	}
	
}

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

function allSelection(checkBoxNode){
	var checked = $(checkBoxNode).prop('checked');
	$(checkBoxNode).parent().parent().find('li > :checkbox').prop('checked', checked);
}

function displayEditParam(img, key){
	var idSuffix = key.replace(/\./g,"_");
	$('#paramForm_'+idSuffix).show();
	$(img).hide();
}



