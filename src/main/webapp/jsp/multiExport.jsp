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
			<ul class="exportedList">
				<span class="checkboxlistLabel">
					<input type="checkbox" onchange="javascript:allSelection(this);">
					PNJ
				</span>
				<s:iterator value="pnjList">
					<li>
						<s:checkbox name="exportedPnjList" cssClass="exportedList" theme="simple" fieldValue="%{key}" value="%{exportedPnjList.contains(key)}">
							<s:param name="id">exportedPnjList-${key}</s:param>
						</s:checkbox>
						<label for="exportedPnjList-${key}" class="checkboxLabel">
							<s:property value="value"/>
						</label>
					</li>
				</s:iterator>
			</ul>
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
			<ul class="exportedList">
				<span class="checkboxlistLabel">
					<input type="checkbox" onchange="javascript:allSelection(this);">
					PJ
				</span>
				<s:iterator value="pjList">
					<li>
						<s:checkbox name="exportedPjList" cssClass="exportedList" theme="simple" fieldValue="%{key}" value="%{exportedPjList.contains(key)}">
							<s:param name="id">exportedPjList-${key}</s:param>
						</s:checkbox>
						<label for="exportedPjList-${key}" class="checkboxLabel">
							<s:property value="value"/>
						</label>
					</li>
				</s:iterator>
			</ul>
		</s:else>
		<div>
			<s:submit theme="simple" action="MultiExport!export" value="Export Web" id="export"/>
		</div>
		<div>
			<s:submit theme="simple" action="MultiExport!exportCsv" value="Export CSV" id="csvExport"/>
		</div>
		<div>
			<s:select id="fileGeneratorName" theme="simple" name="fileGeneratorName" list="generatorList" listKey="value.simpleName" listValue="key" onchange="javascript:selectGenerator(this)"/>
			<s:iterator value="templateFiles">
				<span id="${key.simpleName}_templatesEl" class="templateFileList">
					<label for="${key.simpleName}_templates">Fichier template: </label>
					<s:select id="%{key.simpleName}_templates" name="%{selectedTemplate}" value="" list="value" theme="simple" cssClass="selectTemplate" onchange="javascript:selectTemplateFile(this)"/>
				</span>
			</s:iterator>
			<s:file id="uploadTemplateFile" name="templateFile" theme="simple"/>
			<s:submit theme="simple" action="MultiExport!exportZip" value="Export Zip" id="zipExport"/>
		</div>
	</s:form>
	
	<div id="backToEditDiv">
		<s:a action="EditTable">
			<s:param name="id">
				<s:property value="table.id"/>
			</s:param>
			<img alt="<==" class="straddling" src="<s:url value="/img/left_arrow.png"/>" /> Retour
		</s:a>
	</div>


<s:include value="include/footer.jsp"/>
