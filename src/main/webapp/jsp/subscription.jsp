<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:include value="include/header.jsp"/>
	
	<div id="subscriptionDiv">
		<h3 class="centerTitle">Inscription</h3>
		<s:form action="Subscription!authentification" id="subscriptionForm">
			<s:actionerror />
			<s:textfield name="username" label="Login" size="35"/>
			<s:password name="password" label="Mot de passe" size="35"/>
			<s:password name="confirmPassword" label="Confirmation du mot de passe" size="35"/>
			<s:textfield name="mail" label="Adresse e-mail" size="35"/>
			<s:submit value="Valider" align="center"/>
		</s:form>
		<s:a action="Login"><img alt="<==" class="straddling" src="<s:url value="/img/left_arrow.png"/>" /> Retour</s:a> 
	</div>
	
<s:include value="include/footer.jsp"/>