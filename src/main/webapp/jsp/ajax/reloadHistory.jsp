<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:push value="personnageWork.personnage">
	<s:iterator value="newHistoryReversedIterator" status="stat">
		<s:include value="../include/history.jsp">
			<s:param name="index" value="%{(history.size()-#stat.count)}"/>
		</s:include>
	</s:iterator>
</s:push>
