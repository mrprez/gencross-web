
var context = '/'+window.location.pathname.split( '/' )[1];
var waitMaskTimeout;

$(document).ajaxError(displayAjaxError);


function showConfirm(message, validText, cancelText, validCallback, cancelCallback){
	$('#obstructionMask').show();
	$('#modalDivText').html(message);
	$('#modalDivButton');
	$('#modalDivButton').html("<button id='modalDivValidButton'>"+validText+"</button>"
							+ "<button id='modalDivCancelButton'>"+cancelText+"</button>");
	$('#modalDivButton > button').off("click");
	if(validCallback!=null){
		$('#modalDivValidButton').on("click", validCallback);
	}
	if(cancelCallback!=null){
		$('#modalDivCancelButton').on("click", cancelCallback);
	}
	$('#modalDivButton > button').on("click", function(){
		$('#modalDiv').hide();
		$('#obstructionMask').hide();
	});
	$('#modalDiv').show();
}

function showAlertMessage(message, actionText, callback){
	$('#obstructionMask').show();
	$('#modalDivText').html(message);
	$('#modalDivButton');
	if(actionText==null){
		actionText="OK";
	}
	$('#modalDivButton').html("<button>"+actionText+"</button>");
	$('#modalDivButton > button').off("click");
	if(callback!=null){
		$('#modalDivButton > button').on("click", callback);
	}
	$('#modalDivButton > button').on("click", function(){
		$('#modalDiv').hide();
		$('#obstructionMask').hide();
	});
	$('#modalDiv').show();
}

function displayAjaxError(errorMessage){
	clearTimeout(waitMaskTimeout);
	var message = "UNE ERREUR EST SURVENUE A ";
	var date = new Date();
	message = message + padLeft(date.getHours(), 2, '0');
	message = message + ":" + padLeft(date.getMinutes(), 2, '0');
	message = message + ":" + padLeft(date.getSeconds(), 2, '0');
	showAlertMessage(message, "Recharger la page", function(){window.location.reload(true);});
}

function padLeft(str, length, char){
	str = new String(str);
	while(str.length<length){
		str = char + str;
	}
	return str;
}

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

