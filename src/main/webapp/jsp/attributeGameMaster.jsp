<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>

<div class="attributionFormContainer">
	<s:form action="AttributeGameMaster!attribute">
		<p>
			Attribuer le personnage <span id="attributedPersonnageName"><s:property value="personnageWork.name"/></span> au Maître de Jeu: 
			<s:select name="newGameMasterName" list="userList" listKey="username" theme="simple" listValue="username" headerKey="_no_gm_" headerValue="Aucun"/>
		</p>
		<s:if test="personnageWork.gameMaster!=null">
			<p>
				Le Maître de Jeu actuel est <span id="currentValue"><s:property value="personnageWork.gameMaster.username"/></span>.
			</p>
		</s:if>
		<s:hidden name="personnageId" value="%{personnageWork.id}"/>
		<s:submit cssClass="attributionButton" value="Valider"/>
	</s:form>
</div>

<s:include value="include/footer.jsp"/>