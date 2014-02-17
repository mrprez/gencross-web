<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:include value="include/header.jsp"/>
	
	<div id="subscriptionDiv">
		<span id="subscriptionTitle">Inscription</span>
		<s:form action="Subscription!authentification" id="subscriptionForm">
			<s:actionerror />
			<s:textfield name="username" label="Login" size="50"/>
			<s:password name="password" label="Mot de passe" size="50"/>
			<s:password name="confirmPassword" label="Confirmation du mot de passe" size="50"/>
			<s:textfield name="mail" label="Adresse e-mail" size="50"/>
			<s:submit value="Envoyer" align="center"/>
		</s:form>
	</div>
	
<s:include value="include/footer.jsp"/>