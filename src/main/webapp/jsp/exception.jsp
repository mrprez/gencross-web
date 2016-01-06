<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>

<% response.setStatus(500); %>

<s:include value="/jsp/include/header.jsp"/>

<h2>Une erreur inattendue est survenue</h2>
<p>
    Merci de reporter cette erreur à votre administrateur.
</p>
<hr/>
<h3>Message d'erreur</h3>
<s:actionerror/>
<p>
	<s:if test="exception.message!=null">
		<s:property value="%{exception.message}"/>
	</s:if>
	<s:else>
		<s:property value="exception"/>
	</s:else>
</p>
<s:a action="List">Retour à la page d'accueil</s:a>
<hr/>
<h3>Détails</h3>

<s:push value="exception">
	<s:include value="/jsp/include/stackTrace.jsp"/>
</s:push>

<s:include value="include/footer.jsp"/>