<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

[
	<s:iterator value="plannedGamesList" status="plannedGameListStatus">
		{
			id: <s:property value="id"/>,
			text: "<s:property value="title"/>",
			start_date: "<s:date name="startTime" format="MM/dd/yyyy HH:mm"/>",
			end_date: "<s:date name="endTime" format="MM/dd/yyyy HH:mm"/>"
		}
		<s:if test="!#plannedGameListStatus.last">,</s:if>
	</s:iterator>
]