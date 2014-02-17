<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

[
	<s:iterator value="plannedGamesList" status="plannedGameListStatus">
		{
			"id": "<s:property value="id"/>",
			"title": "<s:property value="title"/>",
			"start": "<s:date name="startTime" format="yyyy-MM-dd HH:mm:ss"/>",
			"end": "<s:date name="endTime" format="yyyy-MM-dd HH:mm:ss"/>",
			"allDay": false
		}
		<s:if test="!#plannedGameListStatus.last">,</s:if>
	</s:iterator>
]