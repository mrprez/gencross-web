function formatDate(date){
	var stringDate = padLeft(date.getFullYear(),4, '0');
	stringDate = stringDate+"/"+padLeft(date.getMonth()+1,2,'0');
	stringDate = stringDate+"/"+padLeft(date.getDate(),2,'0');
	stringDate = stringDate+" "+padLeft(date.getHours(),2,'0');
	stringDate = stringDate+":"+padLeft(date.getMinutes(),2,'0');
	return stringDate;
}


function reload(){
	scheduler.clearAll();
	scheduler.load(context+'/AjaxPlanGame!loadPlannedGames', 'json');
}

function cancelGamePlanned(){
	hideWaitMask();
	$('#planGameError').hide();
}

function sendPlanModification(url, data){
	if( ! eventCancelled ){
		displayWaitMask();
		$.ajax({
			url: url,
			data: data
		}).success(function( data ) {
			var result=jQuery.parseJSON(data);
			if(typeof result.exception == 'undefined'){
				reload();
				hideWaitMask();
			} else {
				clearTimeout(waitMaskTimeout);
				var htmlString="<div class=\"ajaxErrorTitle\">Une erreur s'est produite:</div>";
				htmlString=htmlString+"<p>"+result.exception+"</p>";
				$('#errors').html(htmlString);
				$('#planGameError').show();
			}
		});
		eventCancelled=false;
	}
	return true;
}

function initScheduler(){
	scheduler.config.fix_tab_position = false;
	scheduler.init("scheduler",new Date(),"week");
	reload();
	scheduler.attachEvent("onEventChanged",function(id,ev){
		var data = { title: ev.text, plannedGameId: id, startDate: formatDate(ev.start_date), endDate: formatDate(ev.end_date) };
		var url = context+'/AjaxPlanGame!updatePlannedGame';
		sendPlanModification(url, data);
	});
	scheduler.attachEvent("onEventAdded",function(id,ev){
		var data = { title: ev.text, startDate: formatDate(ev.start_date), endDate: formatDate(ev.end_date) };
		var url = context+'/AjaxPlanGame!createPlannedGame';
		sendPlanModification(url, data);
	});
	scheduler.attachEvent("onEventDeleted",function(id){
		var data = { plannedGameId: id };
		var url = context+'/AjaxPlanGame!deletePlannedGame';
		sendPlanModification(url, data);
	});
	scheduler.attachEvent("onEventCancel", function(id, flag) {
		eventCancelled=true;
	});
}

$( document ).ready(function() {initScheduler();});

var eventCancelled;

