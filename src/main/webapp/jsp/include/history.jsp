<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<tr class="historyItemLine" id="historyItemLine_${param.index}" title="${absoluteName}">
	<td class="historyIndex">${param.index}</td>
	<td><s:property value="shortName"/></td>
	<td><s:property value="oldValue"/></td>
	<td><s:property value="newValue"/></td>
	<td>
		<s:property value="cost"/> <s:property value="pointPool"/>
		<s:if test="isGameMaster">
			<img class="editImg" src="<s:url value="/img/edit.png"/>" alt="Editer" onClick="javascript:displayHistoryEditForm(${param.index});"/>
			<s:set name="index">${param.index}</s:set>
			<s:form id="historyForm_%{index}" cssClass="historyEditForm" theme="simple" onsubmit="return false;">
				<s:textfield cssClass="historyFormCost" key="cost" size="1"/>
				<s:select name="pointPoolName" list="pointPools.values" listKey="name" listValue="name" value="%{pointPool}"/>
				<img class="activeImg historyFormImg" src="<s:url value="/img/validate.png"/>" alt="Valider" onClick="javascript:modifyHistory(${param.index});"/>
				<img class="activeImg historyFormImg" src="<s:url value="/img/fold.png"/>" alt="Fermer" onClick="javascript:hideHistoryEditForm(${param.index});"/>
			</s:form>
		</s:if>
	</td>
	<td title="<s:date name="date" format="HH:mm:ss,SSS dd/MM/yyyy"/>"><s:date name="date" format="HH:mm dd/MM"/></td>
</tr>
