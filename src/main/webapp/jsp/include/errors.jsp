<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div id="errors">
	<table class="errors">
		<s:iterator value="errors">
			<tr>
				<td><s:property/></td>
			</tr>
		</s:iterator>
	</table>
</div>