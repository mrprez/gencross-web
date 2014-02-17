<%@ page contentType="text/xml; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>

<modifications>
	<s:iterator value="updatedProperties">
		<updatedProperty><s:property escape="false"/></updatedProperty>
	</s:iterator>
	<s:if test="reloadErrors">
		<errors/>
	</s:if>
</modifications>
