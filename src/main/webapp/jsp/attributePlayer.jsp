<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>

<div class="attributionFormContainer">
	<s:form action="AttributePlayer!attribute" cssClass="attributionForm">
		<p>
			Attribuer le personnage <span id="attributedPersonnageName"><s:property value="personnageWork.name"/></span> au Joueur:
			<s:select name="newPlayerName" label="Nouveau Joueur" list="userList" theme="simple" listKey="username" listValue="username" headerKey="_no_player_" headerValue="Aucun"/>
		</p>
		<s:if test="personnageWork.player!=null">
			<p>
				Le Joueur actuel est <span id="currentValue"><s:property value="personnageWork.player.username"/></span>.
			</p>
		</s:if>
		<s:hidden name="personnageId" value="%{personnageWork.id}"/>
		<s:submit cssClass="attributionButton" value="Valider"/>
	</s:form>
</div>

<s:include value="include/footer.jsp"/>