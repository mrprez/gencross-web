<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>

<s:actionerror />
<s:form action="Create!create">
	<s:select name="selectedPersonnageTypeName" list="personnageTypeList" label="Selectionner un type de personnage" />
	<s:textfield label="Nom" name="personnageName"/>
	<s:radio onchange="javascript:showHidePlayerGmLists(this);" label="Votre rôle sur le personnage" name="role" list="roleList"/>
	<s:select id="gameMasterList" label="MJ du personnage" name="gameMasterName" list="userList" listKey="username" listValue="username" headerValue="Aucun" headerKey="%{noOneKey}"/>
	<s:select id="gameMasterLockedList" label="MJ du personnage" disabled="true" list="#{'':#session.user.username}"/>
	<s:select id="playerList" label="Joueur du personnage" name="playerName" list="userList" listKey="username" listValue="username" headerValue="Aucun" headerKey="%{noOneKey}"/>
	<s:select id="playerLockedList" label="Joueur du personnage" disabled="true" list="#{'':#session.user.username}"/>
	<s:submit value="Valider"/>
</s:form>

<script language="Javascript">
	var roleTab = document.getElementById('Create!create_roleJoueur').form.role;
	for(var i=0; i<roleTab.length; i++){
		if(roleTab[i].checked){
			showHidePlayerGmLists(roleTab[i]);
		}
	}
</script>

<div id="createFromFileButton">
	<s:a action="Upload">
		<img src="<s:url value="/img/upload.png"/>" class="straddling" /> Créer à partir d'un fichier GenCross <img src="<s:url value="/img/upload.png"/>" class="straddling" />
	</s:a>
</div>
				

<s:include value="include/footer.jsp"/>