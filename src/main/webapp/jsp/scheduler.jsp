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
	<tr class="genCrossTable">
		<td class="genCrossTable">Envoie des personnages</td>
		<td class="genCrossTable">
			<s:if test="senderRunning">
				Running...
			</s:if>
			<s:else>
				<s:date name="senderLastExecutionDate" format="yyyy-MM-dd HH:mm:ss"/>
			</s:else>
		</td>
		<td class="genCrossTable">
			<s:if test="senderRunning">
				<s:url id="reloadUrl" action="JobProcessing"/>
				<s:a href="%{reloadUrl}"><img src="<s:url value="/img/refresh.png"/>" alt="Recharger la page" width="20" height="20" title="Recharger la page"/></s:a>
			</s:if>
			<s:else>
				<s:a action="JobProcessing" method="scheduleSender">Exécuter</s:a>
			</s:else>
		</td>
	</tr>
</table>
		


<s:include value="include/footer.jsp"/>