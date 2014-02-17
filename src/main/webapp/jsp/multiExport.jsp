<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>

	<div id="tableTitle">
		<span><s:property value="table.name"/></span>
	</div>
	
	<s:actionerror/>
	
	<s:form method="get">
		<s:hidden name="tableId"/>
		<s:if test="pnjList.isEmpty()">
			<ul class="exportedList">
				<span class="checkboxlistLabel">
					<input type="checkbox" disabled="disabled">
					PNJ
				</span>
				<li>Aucun export possible</li>
			</ul>
		</s:if>
		<s:else>
			<s:checkboxlist theme="GcrTheme" label="PNJ" list="pnjList" name="exportedPnjList" cssClass="exportedList"/>
		</s:else>
		
		<s:if test="pjList.isEmpty()">
			<ul class="exportedList">
				<span class="checkboxlistLabel">
					<input type="checkbox" disabled="disabled">
					PJ
				</span>
				<li>Aucun export possible</li>
			</ul>
		</s:if>
		<s:else>
			<s:checkboxlist theme="GcrTheme" label="PJ" list="pjList" name="exportedPjList" cssClass="exportedList"/>
		</s:else>
		<s:submit action="MultiExport!export" value="Export"/>
		<s:submit action="MultiExport!exportCsv" value="Export CSV"/>
	</s:form>
	
	<div id="backToEditTableDiv">
		<s:a action="EditTable">
			<s:param name="id">
				<s:property value="table.id"/>
			</s:param>
			<img alt="<==" class="activeImg" src="<s:url value="/img/left_arrow.png"/>" onClick="javascript:editComment()"/> Retour
		</s:a>
	</div>


<s:include value="include/footer.jsp"/>