<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<tr id="pointPool_${param.index}">
	<td class="pointPool_name"><s:property value="name"/></td>
	<td class="pointPool_remaining"><s:property value="remaining"/>/<s:property value="total"/></td>
	<s:if test="isGameMaster">
		<td class="pointPool_modify">
			<img class="editImg" src="<s:url value="/img/edit.png"/>" alt="Editer" onClick="javascript:displayPointPoolForm(this);"/>
			<s:form cssClass="pointPoolForm" theme="simple" onsubmit="return false;">
				<s:hidden name="pointPoolName" value="%{name}"/>
				<table>
					<tr>
						<td colspan="2"><s:textfield label="Nb de pts à ajouter:" class="pointPoolModification" name="pointPoolModification" key="value" value="%{value}" size="5"/></td>
					</tr>
					<tr>
						<td><img class="foldImg" onclick="javascript:modifyPointPool(this)" src="<s:url value="/img/validate.png"/>"/></td>
						<td><img class="foldImg" onclick="javascript:hidePointPoolForm(this)" src="<s:url value="/img/fold.png"/>"/></td>
					</tr>
				</table>
			</s:form>
		</td>
	</s:if>
</tr>
