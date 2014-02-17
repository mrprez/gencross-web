<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>

<s:actionerror/>
<table class="genCrossTable">
	<thead>
		<tr>
			<th class="genCrossTable">Nom</th>
			<th class="genCrossTable">Type</th>
			<th class="genCrossTable">MJ</th>
			<th class="genCrossTable">Actions</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="tableList">
			<tr class="genCrossTable">
				<td class="genCrossTable">
					<s:a action="EditTable">
						<s:param name="id">
							<s:property value="id"/>
						</s:param>
						<s:property value="name"/>
					</s:a>
				</td>
				<td class="genCrossTable"><s:property value="type"/></td>
				<td class="genCrossTable"><s:property value="gameMaster.username"/>
				<td class="genCrossTable"></td>
			</tr>
		</s:iterator>
		<s:form theme="simple" action="TableList!addTable">
			<tr class="genCrossTable">
				<td class="genCrossTable">
					<s:textfield name="tableName"/>
				</td class="genCrossTable">
				<td class="genCrossTable"><s:select name="tableType" list="tableTypeList"/></td>
				<td class="genCrossTable"><s:property value="#session.user.username"/></td>
				<td class="genCrossTable"><s:submit value="Créer"/></td>
			</tr>
		</s:form>
	</tbody>
</table>



<s:include value="include/footer.jsp"/>