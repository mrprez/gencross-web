<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>

<div class="accountManagementContainer">
	<ul>
		<li><s:a action="ChangePassword">Changer son mot de passe.</s:a></li>
		<li><s:a action="ChangeMail">Changer son adresse mail.</s:a></li>
	</ul>
</div>

<s:include value="include/footer.jsp"/>