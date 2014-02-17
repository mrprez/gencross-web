<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>

<s:actionerror/>

<div id='calendar'></div>


<div id="newEvent">
	<s:form id="newEventForm" action="PlanGame!createNewGame" theme="simple">
		<s:hidden name="tableId"/>
		<div id="newHeader">Nouvelle partie</div>
		<div><span id="newGameTitleLabel">Titre</span><s:textfield cssClass="newGameFormField" name="title"/></div>
		<div>
			<table id="newGameDatesTable">
				<tr>
					<td class="newGameDateLabel">Début</td>
					<td>
						<s:textfield cssClass="newGameDate" name="startDay" size="10"/>
						<s:select name="startHour" id="startHour" list="{'0','1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23'}">
							<s:param name="value"><s:date name="value" format="HH" /></s:param>
						</s:select>
						:
						<s:select name="startMinutes" list="{'00','01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31','32','33','34','35','36','37','38','39','40','41','42','43','44','45','46','47','48','49','50','51','52','53','54','55','56','57','58','59'}">
							<s:param name="value"><s:date name="value" format="mm" /></s:param>
						</s:select>
					</td>
				</tr>
				<tr>
					<td>Fin</td>
					<td>
						<s:textfield cssClass="newGameDate" name="endDay" size="10"/>
						<s:select name="endHour" id="endHour" list="{'0','1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23'}">
							<s:param name="value"><s:date name="value" format="HH" /></s:param>
						</s:select>
						:
						<s:select name="endMinutes" list="{'00','01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31','32','33','34','35','36','37','38','39','40','41','42','43','44','45','46','47','48','49','50','51','52','53','54','55','56','57','58','59'}">
							<s:param name="value"><s:date name="value" format="mm" /></s:param>
						</s:select>
					</td>
				</tr>
			</table>
		</div>
		<div>
			<center>
				<input class="newGameFormButton" type="button" value="Valider" onclick="javascript:submitNewGameForm();"/>
				<input class="newGameFormButton" type="button" value="Annuler" onclick="javascript:hideNewGameForm();"/>
			</center>
		</div>
	</s:form>

</div>

	
<script type="text/javascript">

$(document).ready(function() {
	$('#calendar').fullCalendar({
		header: {
			left: 'prev,next today',
			center: 'title',
			right: 'month,agendaWeek,agendaDay'
		}, 
		defaultView: 'agendaWeek',
		firstDay: 1,
		editable: true,
		dayClick: function(date, allDay, jsEvent, view) { dayClick(date, allDay, jsEvent, view); },
		viewDisplay: function(view) {
			view.calendar.option;
		},
		timeFormat: "H(:mm)",
		axisFormat: "H'h'(:mm)",
		columnFormat: {
			month: "ddd",
		    week: "ddd d/M",
		    day: "dddd d/M"
		},
		titleFormat: {
		    month: "MMMM yyyy",
		    week: "d[ MMM][ yyyy]{ '&#8212;' d MMM yyyy}",
		    day: "dddd d MMM yyyy"
		},
		buttonText: {
			today:    "aujourd'hui",
		    month:    "mois",
		    week:     "semaine",
		    day:      "jour"
		},
		monthNames: ["Janvier","Février","Mars","Avril","Mai","Juin","Juillet","Aout","Septembre","Octobre","Novembre","Décembre"],
		monthNamesShort: ["Jan","Fév","Mars","Avr","Mai","Juin","Juil","Aout","Sept","Oct","Nov","Déc"],
		dayNames: ["Dimanche","Lundi","Mardi","Mercredi","Jeudi","Vendredi","Samedi"],
		dayNamesShort: ["Dim","Lun","Mar","Mer","Jeu","Ven","Sam"],
		weekNumberTitle: "S",
		startParam: "startTime",
		endParam: "endTime",
		events: {
	        url: url = host+"/PlanGame!loadEvents",
	        type: 'GET',
	        data: {
	        	 tableId: <s:property value="tableId"/>
	        },
	        error: function() {
	            alert('Erreur Client/Serveur');
	        }
	    },
	    eventDrop: function(event,dayDelta,minuteDelta,allDay){
	    	var url = "<s:url action="PlanGame" method="updateGame"/>";
	    	var data = {
	    		tableId: <s:property value="tableId"/>,
	    		startTime: event.start.getTime(),
	    		endTime: event.end.getTime(),
	    		dayDelta: dayDelta,
	    		minuteDelta: minuteDelta
	    	};
	    	$.post(url, data, refresh);
	    },
	    eventResize: function(event,dayDelta,minuteDelta){
	    	var url = "<s:url action="PlanGame" method="updateGame"/>";
	    	var data = {
	    		tableId: <s:property value="tableId"/>,
	    		startTime: event.start.getTime(),
	    		endTime: event.end.getTime()
	    	};
	    	$.post(url, data, refresh);
	    }
	});
});


function dayClick(date, allDay, jsEvent, view){
	$(".newGameDate").datepicker( "setDate", date );
	$("#startHour").val(date.getHours());
	$("#endHour").val(date.getHours() + 1);
	$("#newEvent").show();
	$("#newEvent").offset({ top: jsEvent.pageY, left: jsEvent.pageX });
	
	
}


function hideNewGameForm(){
	$("#newEvent").hide();
	$(".newGameFormField").val("");
}


function submitNewGameForm(){
	$.post($("#newEventForm").attr("action"), $("#newEventForm").serialize(), refresh);
	hideNewGameForm();
}


function refresh(){
	$('#calendar').fullCalendar( 'refetchEvents' );
}

$(".newGameDate").datepicker();

</script>

<s:property value="googleCalendarLink"/>


<s:include value="include/footer.jsp"/>