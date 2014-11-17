<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>

<s:actionerror />

<s:property value="#session.personnageWork.name"/>

<s:form action="Export!export" enctype="multipart/form-data" method="post">
	<s:hidden name="personnageId"/>
	<s:select id="fileGeneratorName" label="Type d'export" name="fileGeneratorName" list="generatorList" listKey="value.simpleName" listValue="key" onchange="javascript:selectGenerator(this)"/>
	<s:iterator value="templateFiles">
		<tr id="${key.simpleName}_templatesEl" class="templateFileList">
			<td><label for="${key.simpleName}_templates">Fichier template: </label></td>
			<td><s:select id="%{key.simpleName}_templates" value="selectedTemplate" name="" list="value" theme="simple" cssClass="selectTemplate" onchange="javascript:selectTemplateFile(this)"/></td>
		</tr>
	</s:iterator>
	
	<s:file id="uploadTemplateFile" name="templateFile"/>
	<s:submit value="Valider"/>
</s:form>

<script type="text/javascript">
	selectGenerator();
</script>

<div id="backToEditDiv">
	<s:a action="Edit">
		<s:param name="personnageId">
			<s:property value="personnageId"/>
		</s:param>
		<img alt="<==" class="activeImg" src="<s:url value="/img/left_arrow.png"/>" /> Retour
	</s:a>
</div>

<s:include value="include/footer.jsp"/>
