<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>

	<div id="tableTitle">
		<span><s:property value="table.name"/></span>
	</div>
	
	<div id="multiExportResultReturn">
		<s:a action="MultiExport">
			<s:param name="tableId" value="%{table.id}"/>
			<s:param name="exportedPjList" value="%{exportedPjList}"/>
			<s:param name="exportedPnjList" value="%{exportedPnjList}"/>
			<img alt="<==" src="<s:url value="/img/left_arrow.png"/>" class="straddling"/> Retour
		</s:a>
	</div>
	
	<table class="exportTable">
		<s:iterator value="export.getLines()">
			<tr class="exportTable">
				<th class="exportTable">
					<s:property value="title" />
				</th>
				<s:iterator value="values">
					<td class="exportTable">
						<s:property/>
					</td>
				</s:iterator>
			</tr>
		</s:iterator>
	</table>
	
<s:include value="include/footer.jsp"/>