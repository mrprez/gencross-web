<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>

<s:form action="AttributePlayer!attribute" theme="simple">
	<s:if test="personnageWork.player!=null">
		<p>
			Le Joueur actuel est <span id="currentValue"><s:property value="personnageWork.player.username"/></span>.
		</p>
	</s:if>
	<p>
		Attribuer le personnage <span id="attributedPersonnageName"><s:property value="personnageWork.name"/></span> au Joueur:
		<s:select name="newPlayerName" label="Nouveau Joueur" list="userList" theme="simple" listKey="username" listValue="username" headerKey="_no_player_" headerValue="Aucun"/>
	</p>
	<s:hidden name="personnageId" value="%{personnageWork.id}"/>
	<div>
		<s:submit cssClass="attributionButton" value="Valider"/>
		<button type="button" onclick="Javascript:cancelAttribution();" id="cancelAttribution">Annuler</button>
	</div>
</s:form>