
$.getScript( context+'/dwr/interface/planGameAjaxAction.js' );



function getRequestParameter(name) {
	var query = window.location.search.substring(1);
	var pairs = query.split("&");
	for (var i = 0; i < pairs.length; i++) {
		var pair = pairs[i].split("=");
		if (pair[0] == name) {
			return pair[1];
		}
	}
	return (false);
}

function deletePlannedGame(id){
	if(id < Math.pow(2, 31)-1){
		planGameAjaxAction.deletePlannedGame(id);
	}
}

function reload(){
	scheduler.clearAll();
	scheduler.load(context+'/PlanGame!loadPlannedGames?tableId='+getRequestParameter('tableId'), 'json');
}

function initScheduler(){
	scheduler.config.fix_tab_position = false;
	scheduler.config.hour_size_px = 42;
	scheduler.init("scheduler",new Date(),"week");
	reload();
	scheduler.attachEvent(
			"onEventChanged",
			function(id,ev){ planGameAjaxAction.updatePlannedGame(id, ev.text, ev.start_date, ev.end_date); }
	);
	scheduler.attachEvent(
			"onEventAdded",
			function(id,ev){ planGameAjaxAction.createPlannedGame(getRequestParameter('tableId'), ev.text, ev.start_date, ev.end_date); }
	);
	scheduler.attachEvent(
			"onEventDeleted",
			function(id){ deletePlannedGame(id); }
	);
	$('#minical_text').datepicker({
			showOn: "button",
			buttonImageOnly: true,
			buttonImage: "img/calendar.png",
			onClose: function(dateText, inst){ scheduler.setCurrentView($("#minical_text").datepicker("getDate")); }
		}
	);
	$('#minical_text').datepicker("setDate", new Date());
	hideWaitMask();
}

function loadScheduler(){
	displayWaitMask();
	$.getScript( context+'/dhtmlxscheduler/dhtmlxscheduler.js', function( data, textStatus, jqxhr ){ loadSchedulerLocal(); } );
}

function loadSchedulerLocal(){
	$('<link rel="stylesheet" type="text/css" href="'+context+'/dhtmlxscheduler/dhtmlxscheduler.css" />').appendTo('head');
	$.getScript( context+'/dhtmlxscheduler/locale/locale_fr.js', function( data, textStatus, jqxhr ){ initScheduler(); } );
}

$( document ).ready(function() {loadScheduler();});


