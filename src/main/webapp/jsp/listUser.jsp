<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection role="manager" target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>

<table class="genCrossTable">
	<thead>
		<tr>
			<th class="genCrossTable">Login</th>
			<th class="genCrossTable">E-mail</th>
			<th class="genCrossTable">Action</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="userList">
			<tr class="genCrossTable">
				<s:form theme="simple">
					<s:hidden name="username"/>
					<td class="genCrossTable userName"><s:property value="username"/></td>
					<td class="genCrossTable userName"><s:property value="mail"/></td>
					<td class="genCrossTable">
						<s:set name="imgURL">${pageContext.request.contextPath}/img/remove.png</s:set>
						<s:submit action="ListUser!remove" type="image" src="%{imgURL}" tooltip="Supprimer"/>
					</td>
				</s:form>
			</tr>
		</s:iterator>
	</tbody>
</table>

<s:include value="include/footer.jsp"/>