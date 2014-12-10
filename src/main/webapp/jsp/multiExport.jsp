<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>

	<div id="tableTitle">
		<span><s:property value="table.name"/></span>
	</div>
	
	<s:actionerror/>
	
	<s:form enctype="multipart/form-data" method="post">
		<s:hidden name="tableId"/>
		<s:if test="pnjList.isEmpty()">
			<ul class="exportedList">
				<span class="checkboxlistLabel">
					<input type="checkbox" disabled="disabled">
					PNJ
				</span>
				<li>Aucun export possible</li>
			</ul>
		</s:if>
		<s:else>
			<s:checkboxlist theme="GcrTheme" label="PNJ" list="pnjList" name="exportedPnjList" cssClass="exportedList"/>
		</s:else>
		
		<s:if test="pjList.isEmpty()">
			<ul class="exportedList">
				<span class="checkboxlistLabel">
					<input type="checkbox" disabled="disabled">
					PJ
				</span>
				<li>Aucun export possible</li>
			</ul>
		</s:if>
		<s:else>
			<s:checkboxlist theme="GcrTheme" label="PJ" list="pjList" name="exportedPjList" cssClass="exportedList"/>
		</s:else>
		<div>
			<s:submit theme="simple" action="MultiExport!export" value="Export Web"/>
		</div>
		<div>
			<s:submit theme="simple" action="MultiExport!exportCsv" value="Export CSV"/>
		</div>
		<div>
			<s:select id="fileGeneratorName" theme="simple" name="fileGeneratorName" list="generatorList" listKey="value.simpleName" listValue="key" onchange="javascript:selectGenerator(this)"/>
			<s:iterator value="templateFiles">
				<span id="${key.simpleName}_templatesEl" class="templateFileList">
					<label for="${key.simpleName}_templates">Fichier template: </label>
					<s:select id="%{key.simpleName}_templates" name="" list="value" theme="simple" cssClass="selectTemplate" onchange="javascript:selectTemplateFile(this)"/>
				</span>
			</s:iterator>
			<s:submit theme="simple" action="MultiExport!exportZip" value="Export Zip"/>
		</div>
	</s:form>
	
	<script type="text/javascript">
		selectGenerator();
		$("[name='selectedTemplate']").children("[value='<s:property value="selectedTemplate"/>']").attr("selected", "true");
	</script>
	
	<div id="backToEditDiv">
		<s:a action="EditTable">
			<s:param name="id">
				<s:property value="table.id"/>
			</s:param>
			<img alt="<==" class="activeImg" src="<s:url value="/img/left_arrow.png"/>" /> Retour
		</s:a>
	</div>


<s:include value="include/footer.jsp"/>
