<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>

<s:form action="ChangeMail!changeMail">
	<tr><td>Adresse actuelle:</td><td><s:property value="#session.user.mail"/></td></tr>
	<s:textfield label="Nouvelle adresse mail" name="mail"/>
	<s:submit/>
</s:form>

<s:include value="include/footer.jsp"/>