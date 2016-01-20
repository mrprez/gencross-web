
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
			function(id){ planGameAjaxAction.deletePlannedGame(id);}
	);
}

$( document ).ready(function() {initScheduler();});


