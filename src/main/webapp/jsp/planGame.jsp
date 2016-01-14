<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>
		
<div id="planTableTitle">
	<span id="planningTableName"><s:property value="table.name"/></span>
	<s:form id="tableReturnForm" theme="simple" action="EditTable" method="get">
		<s:hidden name="id" value="%{table.id}"/>
		<s:submit type="button">
			<img alt="<==" class="activeImg" src="<s:url value="/img/left_arrow.png"/>"/>
			Retour
		</s:submit>
	</s:form>
</div>

<div id="planGameError">
	<span id="errors"></span>
	<div>
		<s:form action="PlanGame" theme="simple" method="get"><s:hidden name="tableId"/><s:submit value="Recharger"/></s:form>
	</div>
</div>


<s:actionerror/>

<div id="scheduler" class="dhx_cal_container" style="width:100%; height:600px;">
	<div class="dhx_cal_navline">
		<div class="dhx_cal_prev_button">&nbsp;</div>
		<div class="dhx_cal_next_button">&nbsp;</div>
		<div class="dhx_cal_today_button"></div>
		<div class="dhx_cal_date"></div>
		<div class="dhx_cal_tab" name="day_tab" style="right:204px;"></div>
		<div class="dhx_cal_tab" name="week_tab" style="right:140px;"></div>
		<div class="dhx_cal_tab" name="month_tab" style="right:76px;"></div>
	</div>
	<div id="dhx_cal_header" class="dhx_cal_header">
	</div>
	<div id="dhx_cal_data" class="dhx_cal_data">
	</div>		
</div>

<script type="text/javascript">
	function formatDate(date){
		var stringDate = padLeft(date.getFullYear(),'0',4);
		stringDate = stringDate+"/"+padLeft(date.getMonth()+1,'0',2);
		stringDate = stringDate+"/"+padLeft(date.getDate(),'0',2);
		stringDate = stringDate+" "+padLeft(date.getHours(),'0',2);
		stringDate = stringDate+":"+padLeft(date.getMinutes(),'0',2);
		return stringDate;
	}
	
	function padLeft(string,char,size){
		for(var i=0; i<size; i++){
			string = char+string;
		}
		return string.substr(string.length-size, size);
	}
	
	function reload(){
		scheduler.clearAll();
		scheduler.load("<s:url value="/AjaxPlanGame!loadPlannedGames" includeParams="get"/>", "json");
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

	var eventCancelled;
	scheduler.init("scheduler",new Date(),"month");
	reload();
	scheduler.attachEvent("onEventChanged",function(id,ev){
		var data = { title: ev.text, plannedGameId: id, startDate: formatDate(ev.start_date), endDate: formatDate(ev.end_date) };
		var url = "<s:url value="/AjaxPlanGame!updatePlannedGame" includeParams="get"/>";
		sendPlanModification(url, data);
	});
	scheduler.attachEvent("onEventAdded",function(id,ev){
		var data = { title: ev.text, startDate: formatDate(ev.start_date), endDate: formatDate(ev.end_date) };
		var url = "<s:url value="/AjaxPlanGame!createPlannedGame" includeParams="get"/>";
		sendPlanModification(url, data);
	});
	scheduler.attachEvent("onEventDeleted",function(id){
		var data = { plannedGameId: id };
		var url = "<s:url value="/AjaxPlanGame!deletePlannedGame" includeParams="get"/>";
		sendPlanModification(url, data);
	});
	scheduler.attachEvent("onEventCancel", function(id, flag) {
		eventCancelled=true;
	});
</script>

<s:include value="include/footer.jsp"/>