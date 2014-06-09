<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<html>
	<head>
		<title>Gen-Cross</title>
		<meta http-equiv="X-UA-Compatible" content="IE=8"/>
		<script language="JavaScript" type="text/javascript" src="<s:url value="/js/jquery-1.10.1.js"/>" charset="utf-8"></script>
		<script language="JavaScript" type="text/javascript" src="<s:url value="/dhtmlxscheduler/dhtmlxscheduler.js"/>" charset="utf-8"></script>
		<script language="JavaScript" type="text/javascript" src="<s:url value="/dhtmlxscheduler/locale/locale_fr.js"/>" charset="utf-8"></script>
		<script language="JavaScript" type="text/javascript" src="<s:url value="/js/ajax.jsp"/>" charset="utf-8"></script>
		<script language="JavaScript" type="text/javascript" src="<s:url value="/js/tree.js"/>" charset="utf-8"></script>
		<script language="JavaScript" type="text/javascript" src="<s:url value="/js/script.js"/>" charset="utf-8"></script>
		<link href="<s:url value="/css/genCross.css"/>" rel="stylesheet" type="text/css"/>
		<link href="<s:url value="/dhtmlxscheduler/dhtmlxscheduler.css"/>" rel="stylesheet" type="text/css"/>
		<link rel="icon" type="image/jpeg" href="<s:url value="/img/icone_GenCross.jpg"/>" />
	</head>
	<body onload="$('#ajaxError').ajaxError(displayAjaxError);">
		<div id="header">
			<div id="headerLeft">
				<s:a action="List">
					<img src="<s:url value="/img/logo_GenCross.jpg"/>"/>
				</s:a>
				<s:include value="include/menu.jsp"/>
			</div>
			<div id="headerRight">
				<s:if test="#session.user!=null">
					<span id="username"><s:property value="#session.user.username"/></span>
					<br/>
					<s:a id="disconnect" action="Disconnect">Déconnexion</s:a>
				</s:if>
			</div>
			<div class="separator"></div>
		</div>
		
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
		
		<s:url id="reloadUrl" action="PlanGame">
			<s:param name="tableId">${table.id}</s:param>
		</s:url>
		
		<div id="waitMask">
			<img id="waitImage" class="waitImage" src="<s:url value="/img/wait.gif"/>" alt="Attente Serveur..." title="Attente Serveur..."/>
		</div>
		<div id="tooLongWaitMsg"><s:a href="%{reloadUrl}">Attente trop longue: cliquez ici pour recharger la page.</s:a></div>
		
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
				return true;
			}
		
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
			scheduler.attachEvent("onEventDeleted",function(id,ev){
				var data = { plannedGameId: id };
				var url = "<s:url value="/AjaxPlanGame!deletePlannedGame" includeParams="get"/>";
				sendPlanModification(url, data);
			});
		</script>
	<div>
<s:include value="include/footer.jsp"/>