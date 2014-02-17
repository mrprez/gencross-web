<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<p>
	<s:if test="message!=null">
		<span class="message"><s:property value="class"/>:<s:property value="%{message}"/></span><br/>
	</s:if>
	<s:else>
		<span class="message"><s:property value="class"/>:<s:property/></span><br/>
	</s:else>
    <s:iterator value="stackTrace">
    	<s:property/><br/>
    </s:iterator>
    <s:if test="cause!=null">
    	<s:push value="cause">
    		<s:include value="stackTrace.jsp"/>
    	</s:push>
    </s:if>
</p>