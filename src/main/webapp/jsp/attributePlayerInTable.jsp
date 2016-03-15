<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>

<s:form action="AttributePlayerInTable!attribute" theme="simple">
	<p>
		Attribuer le personnage <span id="attributedPersonnageName"><s:property value="personnageWork.name"/></span> au Joueur:
		<s:select name="newPlayerName" label="Nouveau Joueur" list="userList" theme="simple" listKey="username" listValue="username"/>
	</p>
	<s:hidden name="personnageId" value="%{personnageWork.id}"/>
	<s:hidden name="tableId" value="%{tableId}"/>
	<div>
		<s:submit cssClass="attributionButton" value="Valider"/>
		<button type="button" onclick="Javascript:cancelAttribution();">Annuler</button>
	</div>
</s:form>