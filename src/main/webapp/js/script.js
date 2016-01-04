
var context = '/'+window.location.pathname.split( '/' )[1];
var waitMaskTimeout;



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

function displayWaitMask(){
	$('#obstructionMask').show();
	$('#obstructionMask').append("<img class='waitImage' src='"+context+"/img/wait.gif' alt='Attente Serveur...' title='Attente Serveur...'/>");
	waitMaskTimeout = setTimeout("displayReload()",60000);
}

function hideWaitMask(){
	clearTimeout(waitMaskTimeout);
	$('#tooLongWaitMsg').remove();
	$('#obstructionMask').hide();
	$('#obstructionMask').empty();
}

function displayReload(){
	$('#obstructionMask').parent().append('<div id="tooLongWaitMsg"><a href="'+window.location.href+'">Attente trop longue: cliquez ici pour recharger la page.</a></div>');
}

function executeJavascript(responseText, textStatus, XMLHttpRequest){
	var position = 0;
	var start = 0;
	var end = 0;
	while(start>=0){
		start = responseText.indexOf('<script language="Javascript">', position);
		position = start;
		end = responseText.indexOf('</script>', position);
		position = end;
		if(start>=0){
			eval(responseText.substring(start+30, end));
		}
	}	
}

