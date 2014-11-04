<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>

<s:actionerror/>
<table class="genCrossTable">
	<tr class="genCrossTable">
		<th class="genCrossTable">Nom du Job</th>
		<th class="genCrossTable">Dernière exécution</th>
		<th class="genCrossTable">Action</th>
	</tr>
	<s:iterator value="jobList">
		<tr class="genCrossTable">
			<td class="genCrossTable"><s:property value="description"/></td>
			<td class="genCrossTable">
				<s:date name="jobLastDates.get(key)" format="MM/dd/yyyy HH:mm"/>
			</td>
			<td class="genCrossTable">
				<s:if test="jobRunningMap.get(key)">
					<s:url id="reloadUrl" action="JobProcessing"/>
					<s:a href="%{reloadUrl}"><img src="<s:url value="/img/refresh.png"/>" alt="Recharger la page" width="20" height="20" title="Recharger la page"/></s:a>
				</s:if>
				<s:else>
					<s:url var="scheduleSenderUrl" action="JobProcessing" method="scheduleSender">
						<s:param name="jobName" value="key.name"/>
					</s:url>
					<s:a href="%{scheduleSenderUrl}" method="scheduleSender">Exécuter</s:a>
				</s:else>
			</td>
		</tr>
	</s:iterator>
</table>
		


<s:include value="include/footer.jsp"/>