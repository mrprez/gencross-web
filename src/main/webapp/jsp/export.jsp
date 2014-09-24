<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>

<s:property value="#session.personnageWork.name"/>

<s:form action="Export!export" enctype="multipart/form-data" method="post">
	<s:hidden name="personnageId"/>
	<s:select label="Type d'export" name="fileGeneratorName" list="generatorList" listKey="value.simpleName" listValue="key" onchange="javascript:selectGenerator(this)"/>
	<s:iterator value="templateFiles">
		<tr id="${key.simpleName}_templatesEl" class="templateFileList">
			<td><label for="${key.simpleName}_templates">Fichier template: </label></td>
			<td><s:select id="%{key.simpleName}_templates" name="" list="value" theme="simple" cssClass="selectTemplate" onchange="javascript:selectTemplateFile(this)"/></td>
		</tr>
	</s:iterator>
	
	<s:file id="uploadTemplateFile" name="templateFile"/>
	<s:submit value="Valider"/>
</s:form>

<s:include value="include/footer.jsp"/>
