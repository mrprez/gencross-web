<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:include value="include/header.jsp"/>

	<div id="loginContent">
		<h3 class="centerTitle">Mot de passe oublié</h3>
		<s:if test="#session.user==null">
			<s:form action="ForgottenPassword">
				<s:actionerror />
				<s:textfield name="username" label="Login"/>
				<s:submit value="Envoyer" align="center"/>
			</s:form>
			<s:a action="Login"><img alt="<==" class="straddling" src="<s:url value="/img/left_arrow.png"/>" /> Retour</s:a>
			<s:a action="Subscription" id="subscribeLink">S'inscrire</s:a>
		</s:if>
		<s:else>
			<p class="connectedMessage">Vous êtes déjà connecté.</p>
			<p class="homeLink"><s:a action="List">Retour à Accueil</s:a></p>
		</s:else>
	</div>
	
<s:include value="include/footer.jsp"/>