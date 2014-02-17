<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>

<h2>Une erreur est survenue</h2>
<hr/>
<h3>Message d'erreur</h3>
<p><s:actionerror/></p>
<hr/>
<s:a action="List">Retour à la page d'accueil</s:a>


<s:include value="include/footer.jsp"/>