<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection role="manager" target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>

<s:if test="migratedPersonnages!=null">
	<table class="genCrossTable">
		<tr class="genCrossTable">
			<th class="genCrossTable">ID</th>
			<th class="genCrossTable">Nom</th>
			<th class="genCrossTable">Ressemblance</th>
			<th class="genCrossTable">Valide</th>
		</tr>
		<s:iterator value="migratedPersonnages">
			<tr class="genCrossTable">
				<td class="genCrossTable"><s:property value="id"/></td>
				<td class="genCrossTable"><s:property value="name"/></td>
				<td class="genCrossTable"><s:property value="migrationResult[personnageData.id]*100"/>%</td>
				<td class="genCrossTable"><s:property value="migrationResult[validPersonnageData.id]*100"/>%</td>
			</tr>
		</s:iterator>
	</table>
</s:if>
<br/>
<table class="genCrossTable">
	<tr class="genCrossTable">
		<th class="genCrossTable">Plugin Personnage</th>
		<th class="genCrossTable">Version</th>
	</tr>
	<s:iterator value="availablePersonnageList">
		<tr class="genCrossTable">
			<td class="genCrossTable"><s:property value="class.simpleName"/></td>
			<td class="genCrossTable"><s:property value="version"/></td>
		</tr>
	</s:iterator>
</table>

<s:actionerror/>
<s:form cssClass="plugins" action="AddPlugin!upload" enctype="multipart/form-data" method="post">
	<s:file label="Charger un fichier plugin" name="zip"/>
	<s:submit/>

</s:form>





<s:include value="include/footer.jsp"/>