<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:include value="include/header.jsp"/>

	<div id="successContent">
		<span>Opération réalisée avec succès</span>
		<h3><s:property value="successMessage"/></h3>
		<s:set name="url">
			 <s:url action="%{successLink}"/>
		</s:set>
		<s:a href="%{url}"><s:property value="successLinkLabel"/></s:a>
	</div>
	
<s:include value="include/footer.jsp"/>