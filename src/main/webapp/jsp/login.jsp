<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:include value="include/header.jsp"/>

	<div id="loginContent">
		<h3 class="centerTitle">Authentification</h3>
		<s:if test="#session.user==null">
			<s:form action="Login">
				<s:actionerror />
				<s:textfield name="username" label="Login" id="usernameField"/>
				<s:password name="password" label="Password"/>
				<s:submit value="Login" align="center"/>
			</s:form>
			<s:a action="Subscription">S'inscrire</s:a><br/>
			<s:a action="ForgottenPassword">Mot de passe oublié</s:a><br/>
		</s:if>
		<s:else>
			<p class="connectedMessage">Vous êtes déjà connecté.</p>
			<p class="homeLink"><s:a action="List">Retour à Accueil</s:a></p>
		</s:else>
	</div>
	<script language="Javascript">
		document.getElementById("usernameField").focus();
	</script>
<s:include value="include/footer.jsp"/>