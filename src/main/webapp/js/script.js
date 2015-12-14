
/** Generic function */

function showSubMenu(tdMenu){
	$(tdMenu).children('ul').show('fast');
}

function selectGenerator(){
	var selectList = $('#fileGeneratorName')[0];
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

function allSelection(checkBoxNode){
	var checked = $(checkBoxNode).prop('checked');
	$(checkBoxNode).parent().parent().find('li > :checkbox').prop('checked', checked);
}


/** listPersonnage */

function askMJPassword(form){
	var password = prompt("En tant que MJ, vous devez choisir un mot de passe pour ce fichier:");
	var passwordField = $(form).children('[name="password"]')[0];
	passwordField.value = password;
}


/** upload */

function hideDisplayPassword(checkbox){
	if(checkbox.checked){
		$('#mjPassword').parent().parent().show();
	}else{
		$('#mjPassword').parent().parent().hide();
	}
}


/** editTable */

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


/** createPersonnage */

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


/** params */

function displayEditParam(img, key){
	var idSuffix = key.replace(/\./g,"_");
	$('#paramForm_'+idSuffix).show();
	$(img).hide();
}


/** listTable */

function deleteTable(id){
	$('#waitMask').show();
	$('#deleteTableDiv').show();
	$('#tableId').val(id);
}

function cancelDeleteTable(){
	$('#waitMask').hide();
	$('#deleteTableDiv').hide();
}


/** editPersonnage */

function expandCollapse(propertyAbsoluteName, propertyNum){
	var ulId = 'subProperties_'+propertyNum;
	if($('#'+ulId).hasClass('expanded')){
		collapse(propertyAbsoluteName, propertyNum);
	}else if($('#'+ulId).hasClass('expandable')){
		expand(propertyAbsoluteName, propertyNum);
	}
}

function expand(propertyAbsoluteName, propertyNum){
	var ulId = 'subProperties_'+propertyNum;
	$('#'+ulId).removeClass('expandable').addClass('expanded');
	var liId = 'li_'+propertyNum;
	var img = $('#'+liId+' > a.motherPropertyName > img.expandImg');
	var oldSrc = img.attr('src');
	var newSrc = oldSrc.replace('expandable','expanded');
	img.attr('src', newSrc);
	
	editPersonnageAjaxAction.expand(personnageWorkId, propertyAbsoluteName);
}

function collapse(propertyAbsoluteName, propertyNum){
	var ulId = 'subProperties_'+propertyNum;
	$('#'+ulId).removeClass('expanded').addClass('expandable');
	var liId = 'li_'+propertyNum;
	var img = $('#'+liId+' > a.motherPropertyName > img.expandImg');
	var oldSrc = img.attr('src');
	var newSrc = oldSrc.replace('expanded','expandable');
	img.attr('src', newSrc);
	
	editPersonnageAjaxAction.collapse(personnageWorkId, propertyAbsoluteName);
}



