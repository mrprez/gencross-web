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

<div id="scheduler_container">
	<div id="scheduler" class="dhx_cal_container" style="width:100%; height:600px;">
		<div class="dhx_cal_navline">
			<div class="dhx_cal_prev_button">&nbsp;</div>
			<div class="dhx_cal_next_button">&nbsp;</div>
			<div class="dhx_cal_today_button"></div>
			<span id="minical_span">
				<input type="text" id="minical_text"/>
			</span>
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
</div>



<s:include value="include/footer.jsp"/>