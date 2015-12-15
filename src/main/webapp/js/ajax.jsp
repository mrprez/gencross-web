<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


$('#ajaxError').ajaxError(displayAjaxError);

function displayAjaxError(){
	displayWaitMask();
	$('#ajaxErrorDate').empty();
	var date = new Date();
	$('#ajaxErrorDate').append(''+date.getHours()+':'+date.getMinutes()+':'+date.getSeconds())
	$('#ajaxError').show();
}

function setNewValue(number){
	displayWaitMask();
	var serializedArray = $('#form_'+number).serializeArray();
	var postedData = new Object();
	postedData.personnageWorkId = personnageWorkId;
	for(i=0; i<serializedArray.length; i++){
		if(serializedArray[i].name=="newValue"){
			postedData.newValue = serializedArray[i].value;
		}else if(serializedArray[i].name=="propertyAbsoluteName"){
			postedData.propertyAbsoluteName = serializedArray[i].value;
		}
	}
	hideEditForm($('#form_'+number));
	$.post('<s:url action="../jsp/EditAjax" method="updateValue" includeParams="none"/>', postedData, reloadProperties, 'xml');
}

function addPropertyFromOption(number){
	displayWaitMask();
	var serializedArray = $('#addPropertyFromOptionForm_'+number).serializeArray();
	var postedData = new Object();
	postedData.personnageWorkId = personnageWorkId;
	for(i=0; i<serializedArray.length; i++){
		if(serializedArray[i].name=="motherProperty"){
			postedData.motherProperty = serializedArray[i].value;
		}else if(serializedArray[i].name=="optionProperty"){
			postedData.optionProperty = serializedArray[i].value;
		}
	}
	if(specification[number][postedData.optionProperty]!=null){
		postedData.specification = prompt('Specifiez:');
		if(postedData.specification==null){
			alert('Aucune spécification renseignée');
			$('#waitMask').hide();
			$('#waitImage').hide();
			return;
		}
	}
	$.post('<s:url action="../jsp/EditAjax" method="addPropertyFromOption" includeParams="none"/>', postedData, reloadProperties, 'xml');
}

function addFreeProperty(number){
	displayWaitMask();
	var serializedArray = $('#addFreePropertyForm_'+number).serializeArray();
	var postedData = new Object();
	postedData.personnageWorkId = personnageWorkId;
	for(i=0; i<serializedArray.length; i++){
		if(serializedArray[i].name=="motherProperty"){
			postedData.motherProperty = serializedArray[i].value;
		}else if(serializedArray[i].name=="newProperty"){
			postedData.newProperty = serializedArray[i].value;
		}
	}
	$.post('<s:url action="../jsp/EditAjax" method="addFreeProperty" includeParams="none"/>', postedData, reloadProperties, 'xml');
}

function removeProperty(number){
	displayWaitMask();
	var serializedArray = $('#removeForm_'+number).serializeArray();
	var postedData = new Object();
	postedData.personnageWorkId = personnageWorkId;
	for(i=0; i<serializedArray.length; i++){
		if(serializedArray[i].name=="propertyAbsoluteName"){
			postedData.propertyAbsoluteName = serializedArray[i].value;
		}
	}
	$.post('<s:url action="../jsp/EditAjax" method="removeProperty" includeParams="none"/>', postedData, reloadProperties, 'xml');
}
	



function addHistory(data){
	$('#waitLine').remove();
	$('#historyTable > tbody').prepend(data);
}





function modifyPointPool(validateButton){
	displayWaitMask();
	var form = getForm($(validateButton));
	
	var data = new Object();
	data.personnageWorkId = personnageWorkId;
	var inputTab = form.find("input");
	for(var i=0; i<inputTab.length; i++){
		data[inputTab[i].name] = inputTab[i].value;
	}
	if(data.pointPoolModification.match("^[-]?[0-9]+$")==null){
		alert("Vous devez rentrer un numérique");
		$('#waitMask').hide();
		$('#waitImage').hide();
	}else{
		$.post('<s:url action="../jsp/EditAjax" method="modifyPointPool" includeParams="none"/>', data, reloadProperties, 'xml');
	}
}

function modifyHistory(index){
	displayWaitMask();
	
	var serializedArray = $('#historyForm_'+index).serializeArray();
	var data = new Object();
	data.personnageWorkId = personnageWorkId;
	for(i=0; i<serializedArray.length; i++){
		data[serializedArray[i].name] = serializedArray[i].value;
	}
	data.historyIndex = index;
	$.post('<s:url action="../jsp/EditAjax" method="modifyHistory" includeParams="none"/>', data, reloadProperties, 'xml');
	
}

function unvalidate(button){
	var confirm = window.confirm('Etes vous sur de vouloir revenir à la dernière version validée par le MJ?');
	if(confirm){
		$(button).parent().submit();
	}
}

function uploadPersonnage(imageButton){
	$(imageButton).parent().children(".uploadFile")[0].click();
}




function poolPointFormKeyDown(event){
	if(event.keyCode==27){
		hidePointPoolForm(event.currentTarget);
	}
	if(event.keyCode==13){
		modifyPointPool(event.currentTarget);
	}
}

