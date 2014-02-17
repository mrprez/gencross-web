<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>

<div class="attributionFormContainer">
	<s:form action="AttributePlayer!attribute" cssClass="attributionForm">
		<h2>Attribuer "<s:property value="personnageWork.name"/>" à un Joueur</h2>
		
		<s:if test="personnageWork.player!=null">
			<span class="currentOwner">Joueur actuel: <span class="currentOwnerUsername"><s:property value="personnageWork.player.username"/></span></span>
		</s:if>
		<s:select name="newPlayerName" label="Nouveau Joueur" list="userList" listKey="username" listValue="username" headerKey="_no_player_" headerValue="Aucun"/>
		<s:hidden name="personnageId" value="%{personnageWork.id}"/>
		<s:submit cssClass="attributionButton" value="Valider"/>
	</s:form>
</div>

<s:include value="include/footer.jsp"/>