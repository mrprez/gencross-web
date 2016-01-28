var numberAssociation = new Array();
var specification = new Array();
var personnageWorkId;

$.getScript( context+'/dwr/interface/editPersonnageAjaxAction.js' );


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
	if(newValue!=null && propertyAbsoluteName!=null){
		var callbackMeta = {
			callback:reloadChanges,
			timeout:60000,
			errorHandler:displayException
		};
		editPersonnageAjaxAction.updateValue(personnageWorkId, propertyAbsoluteName, newValue, callbackMeta);
	}
}


function addPropertyFromOption(number){
	displayWaitMask();
	var motherProperty = null;
	var optionProperty = null;
	var serializedArray = $('#addPropertyFromOptionForm_'+number).serializeArray();
	for(var i=0; i<serializedArray.length; i++){
		if(serializedArray[i].name=="motherProperty"){
			motherProperty = serializedArray[i].value;
		}else if(serializedArray[i].name=="optionProperty"){
			optionProperty = serializedArray[i].value;
		}
	}
	
	var specificationValue = null;
	if(specification[number][optionProperty]!=null){
		specificationValue = prompt('Specifiez:');
		if(specificationValue==null || specificationValue==""){
			showAlertMessage('Aucune spécification renseignée');
			return;
		}
	}
	editPersonnageAjaxAction.addPropertyFromOption(personnageWorkId, motherProperty, optionProperty, specificationValue, reloadChanges);
}


function addFreeProperty(number){
	displayWaitMask();
	var serializedArray = $('#addFreePropertyForm_'+number).serializeArray();
	var motherProperty = null;
	var newProperty = null;
	for(var i=0; i<serializedArray.length; i++){
		if(serializedArray[i].name=="motherProperty"){
			motherProperty = serializedArray[i].value;
		}else if(serializedArray[i].name=="newProperty"){
			newProperty = serializedArray[i].value;
		}
	}
	editPersonnageAjaxAction.addFreeProperty(personnageWorkId, motherProperty, newProperty, reloadChanges);
}


function removeProperty(number){
	displayWaitMask();
	var serializedArray = $('#removeForm_'+number).serializeArray();
	var propertyAbsoluteName = null;
	for(var i=0; i<serializedArray.length; i++){
		if(serializedArray[i].name=="propertyAbsoluteName"){
			propertyAbsoluteName = serializedArray[i].value;
		}
	}
	editPersonnageAjaxAction.removeProperty(personnageWorkId, propertyAbsoluteName, reloadChanges);
}


function modifyPointPool(poolName){
	displayWaitMask();
	var addedValue = $("form[pointPoolName="+poolName+"]").find('input[type="text"][name="pointPoolModification"]')[0].value;
	
	if(addedValue.match("^[-]?[0-9]+$")==null){
		hideWaitMask();
		showAlertMessage("Vous devez rentrer un numérique");
	}else{
		editPersonnageAjaxAction.modifyPointPool(personnageWorkId, poolName, addedValue, reloadChanges);
	}
}


function modifyPointPoolKeyDown(poolName){
	if(event.keyCode==27){
		$("form[pointPoolName="+poolName+"]").hide();
	}
	if(event.keyCode==13){
		modifyPointPool(poolName);
	}
}


function modifyHistory(index){
	displayWaitMask();
	var serializedArray = $('#historyForm_'+index).serializeArray();
	var pointPoolName = null;
	var cost = null;
	for(var i=0; i<serializedArray.length; i++){
		if(serializedArray[i].name=="pointPoolName"){
			pointPoolName = serializedArray[i].value;
		}else if(serializedArray[i].name=="cost"){
			cost = serializedArray[i].value;
		}
	}
	editPersonnageAjaxAction.modifyHistory(personnageWorkId, index, pointPoolName, cost, reloadChanges);
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
	if(changes.newHistoryIndexes.length > 0){
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
	for(i=0; i<changes.newHistoryIndexes.length; i++){
		data = new Object();
		data.personnageId = personnageWorkId;
		data.historyItemIndex = changes.newHistoryIndexes[i];
		$.post(context+'/Edit!getHistoryItem', data, function(html) { $('#historyTable > tbody').prepend(html); }, 'html');
	}
	$('#waitLine').remove();
	if(changes.actionMessage!=null){
		showAlertMessage(changes.actionMessage);
	}
	
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


function displayEditForm(imgElement){
	$(imgElement).parent().children('.editForm').css('display', 'inline-block');
	$(imgElement).hide();
	$(imgElement).parent().children('.editForm').children('input[type=text]').focus();
	$(imgElement).parent().children('.editForm').children('select').focus();
	$(imgElement).parent().children('.editForm').children('.plusButton').focus();
}

function hideEditFormFromChild(imgElement){
	hideEditForm($(imgElement).parent());
}

function hideEditForm(formElement){
	formElement.hide();
	formElement.parent().children('.editImg').css('display', 'inline');
}

function minusValue(button, offset, min, max){
	var hiddenEl = $(button).parent().children('.hiddenNewValue').get(0);
	var value = parseFloat(hiddenEl.value);
	value = value - offset;
	hiddenEl.value = "" + value;
	displayedSpan = $(button).parent().children('.displayedNewValue');
	displayedSpan.empty();
	displayedSpan.append(hiddenEl.value);
	plusMinusButtonsEnability($(button).parent(), min, max);
}

function plusValue(button, offset, min, max){
	var hiddenEl = $(button).parent().children('.hiddenNewValue').get(0);
	var value = parseFloat(hiddenEl.value);
	value = value + offset;
	hiddenEl.value = "" + value;
	var displayedSpan = $(button).parent().children('.displayedNewValue');
	displayedSpan.empty();
	displayedSpan.append(hiddenEl.value);
	plusMinusButtonsEnability($(button).parent(), min, max);
}

function plusMinusButtonsEnability(formEl, min, max){
	var plusButton = $(formEl).children('.plusButton').get(0);
	var plusImg = $(plusButton).children('img').get(0);
	var minusButton = $(formEl).children('.minusButton').get(0);
	var minusImg = $(minusButton).children('img').get(0);
	var value = parseFloat($(formEl).children('.hiddenNewValue').get(0).value);
	if(!isNaN(min) && value<=min){
		if(!minusButton.disabled){
			minusButton.disabled=true;
			var index = minusImg.src.lastIndexOf('/');
			minusImg.src = minusImg.src.substring(0,index)+'/minusdisabled.png';
			plusButton.focus();
		}
	}else{
		if(minusButton.disabled){
			minusButton.disabled=false;
			var index = minusImg.src.lastIndexOf('/');
			minusImg.src = minusImg.src.substring(0,index)+'/minus.png';
		}
	}
	if(!isNaN(max) && value>=max){
		if(!plusButton.disabled){
			plusButton.disabled=true;
			var index = plusImg.src.lastIndexOf('/');
			plusImg.src = plusImg.src.substring(0,index)+'/plusdisabled.png';
			minusButton.focus();
		}
	}else{
		if(plusButton.disabled){
			plusButton.disabled=false;
			var index = plusImg.src.lastIndexOf('/');
			plusImg.src = plusImg.src.substring(0,index)+'/plus.png';
		}
	}
}

function displayAddPropertyForm(addpropertySpan){
	$(addpropertySpan).parent().children('.addPropertyForm').css('display', 'inline-block');
	$(addpropertySpan).parent().children('.addPropertyForm').children('select').focus();
	$(addpropertySpan).parent().children('.addPropertyForm').children('input[type=text]').focus();
}

function hideAddPropertyForm(foldImage){
	$(foldImage).parent().hide();
}

function displayPointPoolForm(editImg){
	$(editImg).parent().children().css('display', 'inline');
	$(editImg).parent().find('input[type=text]').focus();
}

function hidePointPoolForm(hideButton){
	$(hideButton).parents('.pointPoolForm').hide();
}

function displayHistoryEditForm(index){
	$('#historyForm_'+index).show();
}

function hideHistoryEditForm(index){
	$('#historyForm_'+index).hide();
}

function manageHightlightHistory(){
	if($('#seeNotValidateButton').text().indexOf("Voir")>=0){
		var lastValidationDateString = $('#lastValidationDate').attr("title");
		var lastValidationDate = convertStringToDate(lastValidationDateString);
		var historyTab = $('#historyTable > tbody > tr.historyItemLine');
		for(var i=0; i<historyTab.length; i++){
			var dateText = $(historyTab[i]).children()[5].title;
			var date = convertStringToDate(dateText);
			if(date.getTime()>lastValidationDate.getTime()){
				$(historyTab[i]).css('background-color', '#90AFC3');
			}
		}
		$('#seeNotValidateButton').empty();
		$('#seeNotValidateButton').append("Enlever la surbrillance");

	}else{
		var historyTab = $('#historyTable > tbody > tr.historyItemLine');
		for(var i=0; i<historyTab.length; i++){
			$(historyTab[i]).css('background-color', $(historyTab[i]).parent().css('background-color'));
		}
		$('#seeNotValidateButton').empty();
		$('#seeNotValidateButton').append("Voir le non-validé");
	}
	
}


function convertStringToDate(string){
	var heure = parseInt(string.substr(0,2));
	var minute = parseInt(string.substr(3,2));
	var seconde = parseInt(string.substr(6,2));
	var milliSecondes = parseInt(string.substr(9,3));
	var jour = parseInt(string.substr(13,2));
	var mois = parseInt(string.substr(16,2));
	var annee = parseInt(string.substr(19,4));
	var date = new Date(annee, mois, jour, heure, minute, seconde, milliSecondes);
	
	return date;
}

function displayHistory(button){
	$('#displayButtons').hide();
	$('.historyColumn').show();
	$('#content').animate({marginLeft: "-=80", marginRight: "-=70"});
}

function hideHistory(){
	$('.historyColumn').hide();
	$('#displayButtons').show();
	$('#content').animate({marginLeft: "+=80", marginRight: "+=70"});
}


function unvalidate(button){
	var validCallback = function(){
		$(button).parent().submit();
	};
	showConfirm("Etes vous sur de vouloir revenir à la dernière version validée par le MJ?", validCallback);
}


