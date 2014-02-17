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
	<s:iterator value="updatedPointPoolIndexes">
		<updatedPointPool><s:property escape="false"/></updatedPointPool>
	</s:iterator>
	<s:if test="actionMessage!=null">
		<actionMessage><s:property value="actionMessage" escape="false"/></actionMessage>
	</s:if>
	<s:if test="phaseFinished!=null">
		<phaseFinished><s:property escape="false" value="phaseFinished"/></phaseFinished>
	</s:if>
	<s:if test="reloadHistory">
		<history/>
	</s:if>
	<s:iterator value="changedHistory">
		<changedHistory><s:property/></changedHistory>
	</s:iterator>
</modifications>
