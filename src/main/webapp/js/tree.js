
var commentDisplayer;
var commentPropertyAbsoluteName = null;
var waitMaskTimeout;


function displayEditForm(imgElement){
	$(imgElement).parent().children('.editForm').css('display', 'inline-block');
	$(imgElement).hide();
}

function hideEditFormFromImg(imgElement){
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
}

function hideAddPropertyForm(foldImage){
	$(foldImage).parent().hide();
}

function displayDelayComment(spanEl, event, absoluteName){
	if(commentPropertyAbsoluteName==null){
		var cursorPosition = getCursorPosition(event);
		commentDisplayer = setTimeout('displayComment(\''+spanEl.id+'\', '+cursorPosition.x+', '+cursorPosition.y+', \''+absoluteName+'\')', 3000);
	}
}

function stopDelayComment(){
	clearTimeout(commentDisplayer);
}

function displayComment(spanId, x, y, absoluteName){
	commentPropertyAbsoluteName = absoluteName;
	var commentId = spanId.replace('span_','comment_');
	var commentDisplayer = $('#commentDisplayer');
	var commentDisplayerContent = $('#commentContent > span');
	commentDisplayerContent.empty();
	commentDisplayerContent.append($('#'+commentId).text());
	commentDisplayer.show();
	commentDisplayer.offset({top: y, left: x });
}

function getCursorPosition(event) {
    event = event || window.event;
    var cursorPosition = {x:0, y:0};
    if (event.pageX || event.pageY) {
    	cursorPosition.x = event.pageX;
        cursorPosition.y = event.pageY;
    } else {
    	if(document.documentElement.scrollTop){
    	alert("document.documentElement.scrollTop: "+document.documentElement.scrollTop
    			+"\nevent.clientY: "+event.clientY
    			+"\nsomme=" + (event.clientY+document.documentElement.scrollTop));
    		cursorPosition.x = event.clientX + document.documentElement.scrollLeft;
    		cursorPosition.y = event.clientY + document.documentElement.scrollTop;
    	}else{
	    	alert("document.documentElement.scrollTop: "+document.documentElement.scrollTop
	    			+"\nevent.clientY: "+event.clientY
	    			+"\nsomme=" + (event.clientY+document.documentElement.scrollTop));
    		cursorPosition.x = event.clientX + document.body.scrollLeft;
    		cursorPosition.y = event.clientY + document.body.scrollTop;
    	}
	}
    return cursorPosition;
}

function closeComment(){
	$('#commentDisplayer').hide();
	$('#commentEditor').hide();
	commentPropertyAbsoluteName = null;
}

function editComment(){
	var commentEditor = $('#commentEditor');
	var commentEditorContent =  $('#commentEditor > textarea');
	var commentDisplayerContent = $('#commentContent > span');
	commentEditorContent.empty();
	commentEditorContent.val(commentDisplayerContent.text().trim());
	commentEditor.show();
	commentEditor.offset($('#commentDisplayer').offset());
	$('#commentDisplayer').hide();
}

function displayPointPoolForm(editImg){
	$(editImg).parent().children().css('display', 'inline');
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
		var lastValidationDateString = $('#lastValidationDate').text();
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
			$(historyTab[i]).css('background-color', null);
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


function getForm(element){
	var currentElement = element;
	while(currentElement[0].nodeName!="FORM"){
		currentElement = currentElement.parent();
	}
	return currentElement;
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



