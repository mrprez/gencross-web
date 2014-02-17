<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>


<table id="appenderTable" class="genCrossTable">
	<tr class="genCrossTable">
		<th class="genCrossTable">Appender</th>
		<th class="genCrossTable">Type</th>
		<th class="genCrossTable">Fichier</th>
	</tr>
	<s:iterator value="appenders">
		<tr class="genCrossTable">
			<td class="genCrossTable"><s:property value="name"/></td>
			<td class="genCrossTable"><s:property value="class.simpleName"/></td>
			<td class="genCrossTable"><s:if test="%{top instanceof org.apache.log4j.FileAppender}">
				<s:a action="Logger" method="download">
					<s:param name="appenderName"><s:property value="name"/></s:param>
					<img src="<s:url value="/img/file.png"/>"/>
				</s:a>
			</s:if></td>
		</tr>

	</s:iterator>
</table>


<table class="genCrossTable">
	<tr class="genCrossTable">
		<th class="genCrossTable">Logger</th>
		<th class="genCrossTable">Level</th>
		<th class="genCrossTable">Appenders</th>
	</tr>
	<s:iterator value="loggers">
		<tr class="genCrossTable">
			<td class="genCrossTable"><s:property value="name"/></td>
			<td class="genCrossTable">
				<s:form action="Logger!changeLevel" theme="simple">
					<s:hidden name="loggerName" value="%{name}"/>
					<s:select name="levelName" list="levels" value="level"/>
					<s:set name="imageUrl"><s:url value="/img/validate.png"/></s:set>
					<s:submit type="image" src="%{imageUrl}" onclick="this.form.submit();"/>
				</s:form>
			</td>
			<td class="genCrossTable">
				&nbsp;<s:iterator value="allAppenders"><s:property value="name"/>&nbsp;</s:iterator>
			</td>
		</tr>

	</s:iterator>
</table>


<s:include value="include/footer.jsp"/>