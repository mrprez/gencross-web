var numberAssociation = new Array();
var specification = new Array();
var personnageWorkId;
var context = '/'+window.location.pathname.split( '/' )[1];



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


function setNewValue(number){
	displayWaitMask();
	var propertyAbsoluteName;
	var newValue;
	var serializedArray = $('#form_'+number).serializeArray();
	for(var i=0; i<serializedArray.length; i++){
		if(serializedArray[i].name=="newValue"){
			newValue = serializedArray[i].value;
		}else if(serializedArray[i].name=="propertyAbsoluteName"){
			propertyAbsoluteName = serializedArray[i].value;
		}
	}
	hideEditForm($('#form_'+number));
	
	editPersonnageAjaxAction.updateValue(personnageWorkId, propertyAbsoluteName, newValue, reloadChanges);
}


function reloadChanges(changes){
	for(var i=0; i<changes.propertyNames.length; i++){
		var propertyNum = numberAssociation[changes.propertyNames[i]];
		$('#li_'+propertyNum).empty();
		$('#li_'+propertyNum).append('<img src="'+context+'/img/wait.gif" class="waitImg" alt="Attente Serveur..." width=15 height=15/>');
	}
	if(changes.errorChanges){
		$('#errors').empty();
		$('#errors').append('<img src="'+context+'/img/wait.gif"/>" class="waitImg" alt="Attente Serveur..." width="35" height="35"/>');
	}
//	var updatedpointPoolTab = $(xml).find('updatedPointPool');
//	for(i=0; i<updatedpointPoolTab.length; i++){
//		var trId = 'pointPool_'+updatedpointPoolTab.eq(i).text();
//		$('#'+trId).empty();
//		$('#'+trId).append('<img src="<s:url value="/img/wait.gif"/>" class="waitImg" alt="Attente Serveur..." width="15" height="15"/>');
//	}
	if(changes.phaseFinished != null){
		$('#nextPhaseButton')[0].disabled = ! changes.phaseFinished;
	}
//	if($(xml).find('history').length>0){
//		var trText = '<tr id="waitLine">';
//		for(i=0; i<5; i++){
//			trText = trText+'<td><img src="<s:url value="/img/wait.gif"/>" class="waitImg" alt="Attente Serveur..." width="15" height="15"/></td>';
//		}
//		$('#historyTable > tbody').prepend('<tr id="waitLine"><td class="historyIndex"></td><td><img src="<s:url value="/img/wait.gif"/>" class="waitImg" alt="Attente Serveur..." width="15" height="15"/></td><td><img src="<s:url value="/img/wait.gif"/>" alt="Attente Serveur..." width="15" height="15"/></td><td><img src="<s:url value="/img/wait.gif"/>" alt="Attente Serveur..." width="15" height="15"/></td><td><img src="<s:url value="/img/wait.gif"/>" alt="Attente Serveur..." width="15" height="15"/></td><td><img src="<s:url value="/img/wait.gif"/>" alt="Attente Serveur..." width="15" height="15"/></td></tr>');
//	}
//	var changedHistoryTab = $(xml).find('changedHistory');
//	for(i=0; i<changedHistoryTab.length; i++){
//		var trId = 'historyItemLine_'+changedHistoryTab.eq(i).text();
//		$('#'+trId).empty();
//		$('#'+trId).append('<td class="historyIndex">'+changedHistoryTab.eq(i).text()+'</td><td><img src="<s:url value="/img/wait.gif"/>" class="waitImg" alt="Attente Serveur..." width="15" height="15"/></td><td><img src="<s:url value="/img/wait.gif"/>" alt="Attente Serveur..." width="15" height="15"/></td><td><img src="<s:url value="/img/wait.gif"/>" alt="Attente Serveur..." width="15" height="15"/></td><td><img src="<s:url value="/img/wait.gif"/>" alt="Attente Serveur..." width="15" height="15"/></td><td><img src="<s:url value="/img/wait.gif"/>" alt="Attente Serveur..." width="15" height="15"/></td>');
//	}
	hideWaitMask();
	
	for(i=0; i<changes.propertyNames.length; i++){
		propertyNum = numberAssociation[changes.propertyNames[i]];
		var data = new Object();
		data.personnageId = personnageWorkId;
		data.propertyAbsoluteName = changes.propertyNames[i];
		data.propertyNum = propertyNum;
		$('#li_'+propertyNum).load(context+'/Edit!getProperty #li_'+propertyNum+' > *',data, executeJavascript);
	}
//	if($(xml).find('errors').length>0){
//		var data = new Object();
//		data.personnageWorkId = personnageWorkId;
//		$('#errors').load('<s:url action="../jsp/EditAjax" method="getErrorList" includeParams="none"/> #errors > *',data);
//	}
//	for(i=0; i<updatedpointPoolTab.length; i++){
//		var trId = 'pointPool_'+updatedpointPoolTab.eq(i).text();
//		var data = new Object();
//		data.personnageWorkId = personnageWorkId;
//		data.pointPoolIndex = updatedpointPoolTab.eq(i).text();
//		$('#'+trId).load('<s:url action="../jsp/EditAjax" method="getPointPool" includeParams="none"/> #'+trId+' > *',data);
//	}
//	if($(xml).find('history').length>0){
//		var data = new Object();
//		data.personnageWorkId = personnageWorkId;
//		if($('#historyTable > tbody > tr.historyItemLine:first > td:eq(0)').size()==0){
//			data.historyLastIndex = '-1';
//			$.post('<s:url action="../jsp/EditAjax" method="getLastHistory" includeParams="none"/>', data, addHistory, 'html');
//		}else{
//			data.historyLastIndex = $('#historyTable > tbody > tr.historyItemLine:first > td:eq(0)').text();
//			$.post('<s:url action="../jsp/EditAjax" method="getLastHistory" includeParams="none"/>', data, addHistory, 'html');
//		}
//	}
//	var changedHistoryTab = $(xml).find('changedHistory');
//	for(i=0; i<changedHistoryTab.length; i++){
//		var trId = 'historyItemLine_'+changedHistoryTab.eq(i).text();
//		var data = new Object();
//		data.personnageWorkId = personnageWorkId;
//		data.historyIndex = changedHistoryTab.eq(i).text();
//		$('#'+trId).load('<s:url action="../jsp/EditAjax" method="getHistoryItem" includeParams="none"/> #'+trId+' > *',data);
//	}
//	
//	if($(xml).find('actionMessage').length>0){
//		alert($(xml).find('actionMessage').first().text());
//	}
	
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

