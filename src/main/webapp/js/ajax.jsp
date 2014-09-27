<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

var numberAssociation = new Array();
var specification = new Array();
var personnageWorkId;

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
	

function reloadProperties(xml){
	var updatedPropertyTab = $(xml).find('updatedProperty');
	for(i=0; i<updatedPropertyTab.length; i++){
		var propertyNum = numberAssociation[updatedPropertyTab.eq(i).text()];
		$('#li_'+propertyNum).empty();
		$('#li_'+propertyNum).append('<img src="<s:url value="/img/wait.gif"/>" class="waitImg" alt="Attente Serveur..." width=15 height=15/>');
	}
	if($(xml).find('errors').length>0){
		$('#errors').empty();
		$('#errors').append('<img src="<s:url value="/img/wait.gif"/>" class="waitImg" alt="Attente Serveur..." width="35" height="35"/>');
	}
	var updatedpointPoolTab = $(xml).find('updatedPointPool');
	for(i=0; i<updatedpointPoolTab.length; i++){
		var trId = 'pointPool_'+updatedpointPoolTab.eq(i).text();
		$('#'+trId).empty();
		$('#'+trId).append('<img src="<s:url value="/img/wait.gif"/>" class="waitImg" alt="Attente Serveur..." width="15" height="15"/>');
	}
	if($(xml).find('phaseFinished').length>0){
		$('#nextPhaseButton')[0].disabled = ($(xml).find('phaseFinished').first().text()=="false");
	}
	if($(xml).find('history').length>0){
		var trText = '<tr id="waitLine">';
		for(i=0; i<5; i++){
			trText = trText+'<td><img src="<s:url value="/img/wait.gif"/>" class="waitImg" alt="Attente Serveur..." width="15" height="15"/></td>';
		}
		$('#historyTable > tbody').prepend('<tr id="waitLine"><td class="historyIndex"></td><td><img src="<s:url value="/img/wait.gif"/>" class="waitImg" alt="Attente Serveur..." width="15" height="15"/></td><td><img src="<s:url value="/img/wait.gif"/>" alt="Attente Serveur..." width="15" height="15"/></td><td><img src="<s:url value="/img/wait.gif"/>" alt="Attente Serveur..." width="15" height="15"/></td><td><img src="<s:url value="/img/wait.gif"/>" alt="Attente Serveur..." width="15" height="15"/></td><td><img src="<s:url value="/img/wait.gif"/>" alt="Attente Serveur..." width="15" height="15"/></td></tr>');
	}
	var changedHistoryTab = $(xml).find('changedHistory');
	for(i=0; i<changedHistoryTab.length; i++){
		var trId = 'historyItemLine_'+changedHistoryTab.eq(i).text();
		$('#'+trId).empty();
		$('#'+trId).append('<td class="historyIndex">'+changedHistoryTab.eq(i).text()+'</td><td><img src="<s:url value="/img/wait.gif"/>" class="waitImg" alt="Attente Serveur..." width="15" height="15"/></td><td><img src="<s:url value="/img/wait.gif"/>" alt="Attente Serveur..." width="15" height="15"/></td><td><img src="<s:url value="/img/wait.gif"/>" alt="Attente Serveur..." width="15" height="15"/></td><td><img src="<s:url value="/img/wait.gif"/>" alt="Attente Serveur..." width="15" height="15"/></td><td><img src="<s:url value="/img/wait.gif"/>" alt="Attente Serveur..." width="15" height="15"/></td>');
	}
	hideWaitMask();
	
	for(i=0; i<updatedPropertyTab.length; i++){
		var propertyNum = numberAssociation[updatedPropertyTab.eq(i).text()];
		var data = new Object();
		data.personnageWorkId = personnageWorkId;
		data.propertyAbsoluteName = updatedPropertyTab.eq(i).text();
		data.propertyNum = propertyNum;
		$('#li_'+propertyNum).load('<s:url action="../jsp/EditAjax" method="getProperty" includeParams="none"/> #li_'+propertyNum+' > *',data, executeJavascript);
		eval($('#li_'+propertyNum)[0]);
	}
	if($(xml).find('errors').length>0){
		var data = new Object();
		data.personnageWorkId = personnageWorkId;
		$('#errors').load('<s:url action="../jsp/EditAjax" method="getErrorList" includeParams="none"/> #errors > *',data);
	}
	for(i=0; i<updatedpointPoolTab.length; i++){
		var trId = 'pointPool_'+updatedpointPoolTab.eq(i).text();
		var data = new Object();
		data.personnageWorkId = personnageWorkId;
		data.pointPoolIndex = updatedpointPoolTab.eq(i).text();
		$('#'+trId).load('<s:url action="../jsp/EditAjax" method="getPointPool" includeParams="none"/> #'+trId+' > *',data);
	}
	if($(xml).find('history').length>0){
		var data = new Object();
		data.personnageWorkId = personnageWorkId;
		if($('#historyTable > tbody > tr.historyItemLine:first > td:eq(0)').size()==0){
			data.historyLastIndex = '-1';
			$.post('<s:url action="../jsp/EditAjax" method="getLastHistory" includeParams="none"/>', data, addHistory, 'html');
		}else{
			data.historyLastIndex = $('#historyTable > tbody > tr.historyItemLine:first > td:eq(0)').text();
			$.post('<s:url action="../jsp/EditAjax" method="getLastHistory" includeParams="none"/>', data, addHistory, 'html');
		}
	}
	var changedHistoryTab = $(xml).find('changedHistory');
	for(i=0; i<changedHistoryTab.length; i++){
		var trId = 'historyItemLine_'+changedHistoryTab.eq(i).text();
		var data = new Object();
		data.personnageWorkId = personnageWorkId;
		data.historyIndex = changedHistoryTab.eq(i).text();
		$('#'+trId).load('<s:url action="../jsp/EditAjax" method="getHistoryItem" includeParams="none"/> #'+trId+' > *',data);
	}
	
	if($(xml).find('actionMessage').length>0){
		alert($(xml).find('actionMessage').first().text());
	}
	
}

function addHistory(data){
	$('#waitLine').remove();
	$('#historyTable > tbody').prepend(data);
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
	
	var data = new Object();
	data.personnageWorkId = personnageWorkId;
	data.propertyAbsoluteName = propertyAbsoluteName;
	$.post('<s:url action="../jsp/EditAjax" method="expand" includeParams="none"/>', data);
}

function collapse(propertyAbsoluteName, propertyNum){
	var ulId = 'subProperties_'+propertyNum;
	$('#'+ulId).removeClass('expanded').addClass('expandable');
	var liId = 'li_'+propertyNum;
	var img = $('#'+liId+' > a.motherPropertyName > img.expandImg');
	var oldSrc = img.attr('src');
	var newSrc = oldSrc.replace('expanded','expandable');
	img.attr('src', newSrc);
	
	var data = new Object();
	data.personnageWorkId = personnageWorkId;
	data.propertyAbsoluteName = propertyAbsoluteName;
	$.post('<s:url action="../jsp/EditAjax" method="collapse" includeParams="none"/>', data);
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

function displayWaitMask(){
	$('#waitMask').show();
	$('#waitImage').show();
	waitMaskTimeout = setTimeout("displayReload()",60000);
}

function hideWaitMask(){
	clearTimeout(waitMaskTimeout);
	$('#waitMask').hide();
	$('#waitImage').hide();
	$('#tooLongWaitMsg').hide();
}

function displayReload(){
	$('#tooLongWaitMsg').show();
}

function editFormKeyDown(event){
	if(event.keyCode==27){
		hideEditFormFromChild(event.currentTarget);
	}
	if(event.keyCode==13){
		$(event.currentTarget).parent().children('button').click();
	}
	if(event.keyCode==37){
		var minusButton = $(event.currentTarget).parent().children('.minusButton');
		if( ! minusButton.get(0).disabled ){
			minusButton.click();
		}
	}
	if(event.keyCode==39){
		var plusButton = $(event.currentTarget).parent().children('.plusButton');
		if( ! plusButton.get(0).disabled ){
			plusButton.click();
		}
	}
}

function poolPointFormKeyDown(event){
	if(event.keyCode==27){
		hidePointPoolForm(event.currentTarget);
	}
	if(event.keyCode==13){
		modifyPointPool(event.currentTarget);
	}
}

