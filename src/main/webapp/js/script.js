
var context = '/'+window.location.pathname.split( '/' )[1];
var waitMaskTimeout;

$(document).ajaxError(displayException);

window.onerror = function(errorMessage, url, lineNumber) {
	displayException("\""+errorMessage+"\" dans "+url+" en ligne "+lineNumber);
};

dwr.engine.setErrorHandler(
	function(message, exception){ 
		displayException(exception.javaClassName+": "+message);
	}
);


function showConfirm(message, validCallback, cancelCallback, validText, cancelText){
	$('#obstructionMask').show();
	$('#modalDivText').html(message);
	if(validText==null){
		validText = 'Valider';
	}
	if(cancelText==null){
		cancelText = 'Annuler';
	}
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

function displayException(errorMessage){
	clearTimeout(waitMaskTimeout);
	var message = "UNE ERREUR EST SURVENUE A ";
	var date = new Date();
	message = message + padLeft(date.getHours(), 2, '0');
	message = message + ":" + padLeft(date.getMinutes(), 2, '0');
	message = message + ":" + padLeft(date.getSeconds(), 2, '0');
	message = message + "<br/>";
	message = message + errorMessage;
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

