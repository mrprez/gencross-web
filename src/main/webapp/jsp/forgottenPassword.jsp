<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:include value="include/header.jsp"/>

	<div id="loginContent">
		<h3>Mot de passe oublié</h3>
		<s:if test="#session.user==null">
			<s:form action="ForgottenPassword">
				<s:actionerror />
				<s:textfield name="username" label="Login"/>
				<s:submit value="Envoyer" align="center"/>
			</s:form>
			<s:a action="Subscription">S'inscrire</s:a>
		</s:if>
		<s:else>
			Vous êtes connecté.
		</s:else>
	</div>
	
<s:include value="include/footer.jsp"/>