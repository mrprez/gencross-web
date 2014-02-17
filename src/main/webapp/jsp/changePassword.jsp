<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>

<s:form action="ChangePassword!changePassword">
	<s:actionerror />
	<s:password label="Ancien mot de passe" name="password"/>
	<s:password label="Nouveau mot de passe" name="newPassword"/>
	<s:password label="Confirmation du nouveau de passe" name="confirm"/>
	<s:submit/>
</s:form>

<s:include value="include/footer.jsp"/>