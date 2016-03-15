<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>

<s:form action="AttributeGameMaster!attribute" theme="simple">
	<s:if test="personnageWork.gameMaster!=null">
		<p>
			Le Maître de Jeu actuel est <span id="currentValue"><s:property value="personnageWork.gameMaster.username"/></span>.
		</p>
	</s:if>
	<p>
		Attribuer le personnage <span id="attributedPersonnageName"><s:property value="personnageWork.name"/></span> au Maître de Jeu: 
		<s:select name="newGameMasterName" list="userList" listKey="username" theme="simple" listValue="username" headerKey="_no_gm_" headerValue="Aucun"/>
	</p>
	<s:hidden name="personnageId" value="%{personnageWork.id}"/>
	<div>
		<s:submit cssClass="attributionButton" value="Valider"/>
		<button type="button" onclick="Javascript:cancelAttribution();">Annuler</button>
	</div>
</s:form>
