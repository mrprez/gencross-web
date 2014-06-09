<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test="exception.message!=null">
	{ "exception":"<s:property value="%{exception.message}"/>" }
</s:if>
<s:else>
	{ "exception":"<s:property value="exception"/>" }
</s:else>