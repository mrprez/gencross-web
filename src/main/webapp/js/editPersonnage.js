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
	var propertyAbsoluteName=null;
	var newValue=null;
	var serializedArray = $('#form_'+number).serializeArray();
	for(var i=0; i<serializedArray.length; i++){
		if(serializedArray[i].name=="newValue"){
			newValue = serializedArray[i].value;
		}else if(serializedArray[i].name=="propertyAbsoluteName"){
			propertyAbsoluteName = serializedArray[i].value;
		}
	}
	hideEditForm($('#form_'+number));
	if(newValue!=null && newValue!=null){
		editPersonnageAjaxAction.updateValue(personnageWorkId, propertyAbsoluteName, newValue, reloadChanges);
	}
}


function reloadChanges(changes){
	var i;
	for(i=0; i<changes.propertyNames.length; i++){
		var propertyNum = numberAssociation[changes.propertyNames[i]];
		$('#li_'+propertyNum).empty();
		$('#li_'+propertyNum).append('<img src="'+context+'/img/wait.gif" class="waitImg" alt="Attente Serveur..." width=15 height=15/>');
	}
	if(changes.errorChanges){
		$('#errors').empty();
		$('#errors').append('<img src="'+context+'/img/wait.gif" class="waitImg" alt="Attente Serveur..." width="35" height="35"/>');
	}
	for(i=0; i<changes.pointPoolNames.length; i++){
		$("tr[pointPool='"+changes.pointPoolNames[i]+"']").empty();
		$("tr[pointPool='"+changes.pointPoolNames[i]+"']").append('<img src="'+context+'/img/wait.gif" class="waitImg" alt="Attente Serveur..." width="15" height="15"/>');
	}
	if(changes.phaseFinished != null){
		$('#nextPhaseButton')[0].disabled = ! changes.phaseFinished;
	}
	if(changes.newHistoryLength > 0){
		var trText = '<tr id="waitLine">';
		for(i=0; i<5; i++){
			trText = trText+'<td><img src="'+context+'/img/wait.gif" class="waitImg" alt="Attente Serveur..." width="15" height="15"/></td>';
		}
		trText = trText+'</tr>';
		$('#historyTable > tbody').prepend(trText);
	}
	
	hideWaitMask();
	
	var data;
	for(i=0; i<changes.propertyNames.length; i++){
		propertyNum = numberAssociation[changes.propertyNames[i]];
		data = new Object();
		data.personnageId = personnageWorkId;
		data.propertyAbsoluteName = changes.propertyNames[i];
		data.propertyNum = propertyNum;
		$('#li_'+propertyNum).load(context+'/Edit!getProperty #li_'+propertyNum+' > *',data, executeJavascript);
	}
	if(changes.errorChanges){
		data = new Object();
		data.personnageId = personnageWorkId;
		$('#errors').load(context+'/Edit!getErrorList #errors > *',data);
	}
	for(i=0; i<changes.pointPoolNames.length; i++){
		data = new Object();
		data.personnageId = personnageWorkId;
		data.pointPoolName = changes.pointPoolNames[i];
		$("tr[pointPool='"+changes.pointPoolNames[i]+"']").load(context+"/Edit!getPointPool tr[pointPool='"+changes.pointPoolNames[i]+"'] > *",data);
	}
	if(changes.newHistoryLength > 0){
		data = new Object();
		data.personnageId = personnageWorkId;
		data.lastHistoryItemNumber = changes.newHistoryLength;
		$.post('<s:url action="../jsp/EditAjax" method="getLastHistory" includeParams="none"/>', data, addHistory, 'html');
	}
//	var changedHistoryTab = $(xml).find('changedHistory');
//	for(i=0; i<changedHistoryTab.length; i++){
//		var trId = 'historyItemLine_'+changedHistoryTab.eq(i).text();
//		var data = new Object();
//		data.personnageWorkId = personnageWorkId;
//		data.historyIndex = changedHistoryTab.eq(i).text();
//		$('#'+trId).load('<s:url action="../jsp/EditAjax" method="getHistoryItem" includeParams="none"/> #'+trId+' > *',data);
//	}
//	
//	if(changes.actionMessage!=null){
//		alert(changes.actionMessage);
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

