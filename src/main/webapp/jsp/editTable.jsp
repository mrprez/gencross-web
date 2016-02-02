<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>

<script language="JavaScript" type="text/javascript" src="<s:url value="/js/ckeditor/ckeditor.js"/>"></script>


<s:push value="table">
	<div id="tableTitle">
		<span><s:property value="name"/></span>
	</div>
	
	<s:actionerror/>
	
	<div id="pnjAndPjDiv">
		<div id="pnjDiv">
			<table id="pnjList" class="genCrossTable">
				<thead>
					<tr>
						<th class="genCrossTable">PNJ</th>
						<th class="genCrossTable">Actions</th>
					</tr>
				</thead>
				<tbody>
					<s:if test="pnjList.isEmpty()">
						<tr>
							<td class="genCrossTable" colspan="2">
								Pas de PNJ à cette table.
							</td>
						</tr>
					</s:if>
					<s:iterator value="pnjList">
						<tr>
							<td class="genCrossTable">
								<s:a action="Edit">
									<s:param name="personnageId">
										<s:property value="id"/>
									</s:param>
									<s:property value="name"/>
								</s:a>
							</td>
							<td class="genCrossTable">
								<s:a action="AttributePlayerInTable" title="Attribuer un PJ">
									<s:param name="personnageId">
										<s:property value="id"/>
									</s:param>
									<s:param name="tableId">
										<s:property value="table.id"/>
									</s:param>
									<img src="<s:url value="/img/attributePlayer.png"/>" alt="Attribuer à un joueur"/>
								</s:a>
								<s:a action="EditTable!unbindPersonnage" title="Exclure de la table">
									<s:param name="personnageId">
										<s:property value="id"/>
									</s:param>
									<s:param name="id">
										<s:property value="table.id"/>
									</s:param>
									<img src="<s:url value="/img/unlink.png"/>" alt="Exclure de la table"/>
								</s:a>
							</td>
						</tr>
					</s:iterator>
					<s:form theme="simple" action="EditTable!addPersonnage">
						<tr>
							<s:hidden name="id"/>
							<td class="genCrossTable"><s:textfield name="personnageName"/></td>
							<td class="genCrossTable"><s:submit value="Créer"/></td>
						</tr>
					</s:form>
				</tbody>
			</table>
		</div>
		
		<div id="pjDiv">
			<table id="pjList" class="genCrossTable">
				<thead>
					<tr>
						<th class="genCrossTable">PJ</th>
						<th class="genCrossTable">Joueur</th>
						<th class="genCrossTable">Actions</th>
					</tr>
				</thead>
				<tbody>
					<s:if test="pjList.isEmpty()">
						<tr>
							<td class="genCrossTable" colspan="3">
								Pas de PJ à cette table.
							</td>
						</tr>
					</s:if>
					<s:iterator value="pjList">
						<tr>
							<td class="genCrossTable">
								<s:a action="Edit">
									<s:param name="personnageId">
										<s:property value="id"/>
									</s:param>
									<s:property value="name"/>
								</s:a>
							</td>
							<td class="genCrossTable"><s:property value="player.username"/></td>
							<td class="genCrossTable">
								<s:form theme="simple" action="EditTable!transformInPNJ" id="transformInPnjForm" title="Transformer en PNJ">
									<s:hidden name="personnageId" value="%{id}"/>
									<s:hidden name="id" value="%{table.id}"/>
									<s:set name="imageUrl"><s:url value="/img/becomePNJ.png"/></s:set>
									<s:submit type="image" src="%{imageUrl}" onclick="this.form.submit();"/>
								</s:form>
								<s:a action="EditTable!unbindPersonnage" title="Exclure de la table">
									<s:param name="personnageId">
										<s:property value="id"/>
									</s:param>
									<s:param name="id">
										<s:property value="table.id"/>
									</s:param>
									<img src="<s:url value="/img/unlink.png"/>" alt="Exclure de la table"/>
								</s:a>
							</td>
						</tr>
					</s:iterator>
					<s:if test="!pjList.isEmpty()">
						<tr>
							<td colspan="3">
								<s:a action="TablePointsPools">
									<s:param name="id">
										<s:property value="table.id"/>
									</s:param>
									Gérer les pools de points
								</s:a>
							</td>
						</tr>
					</s:if>
				</tbody>
			</table>
		</div>
	</div>
	
	<div id="editTableActionsDiv">
		<s:if test="!addablePersonnage.isEmpty()">
			<s:form cssClass="tableButton buttonGroup" theme="simple" action="EditTable!bindPersonnage" id="bindPersonnageForm">
				<s:hidden name="id"/>
				Rattacher un personnage:
				<s:select name="personnageId" list="addablePersonnage" listKey="id" listValue="name"/>
				<s:set name="imageUrl"><s:url value="/img/link.png"/></s:set>
				<s:submit type="button"><img class="imgButton" src="${imageUrl}"/> Rattacher</s:submit>
			</s:form>
		</s:if>
		<s:form cssClass="tableButton buttonGroup" theme="simple" action="MultiExport" method="get"><s:hidden name="tableId" value="%{id}"/><s:submit value="Export Multiple"/></s:form>
		<s:form cssClass="tableButton buttonGroup" theme="simple" action="PlanGame" method="get"><s:hidden name="tableId" value="%{id}"/><s:submit value="Planning"/></s:form>
	</div>

	<s:if test="loadedMessageNumber!=null">
		<ul id="loadedMessagesMessage">
			<s:if test="loadedMessageNumber==0">
				<li>Aucun message chargé pour cette table.</li>
			</s:if>
			<s:elseif test="loadedMessageNumber==1">
				<li><s:property value="loadedMessageNumber" /> message chargé pour cette table.</li>
			</s:elseif>
			<s:else>
				<li><s:property value="loadedMessageNumber" /> messages chargés pour cette table.</li>
			</s:else>
		</ul>
	</s:if>
	
	<div id="messageListDiv">
		<table id="messageList" class="genCrossTable">
			<tr>
				<th class="genCrossTable">Messages</th>
			</tr>
			<s:set name="tableId" value="%{id}"/>
			<s:iterator value="messages">
				<tr>
					<td class="genCrossTable">
						<div>
							<span class="messageAuthor" onClick="javascript:showHideMessage(this)">
								<img class="expandImg" src="<s:url value="/img/expandable.jpg" includeParams="none"/>" alt="Déplier/Replier" width="10" height="10"/>
								<s:property value="author.username"/>
							</span>
							<s:form cssClass="removeMessageForm" action="EditTable!removeMessage" method="post" id="removeMessageForm_%{id}" theme="simple">
								<s:hidden name="messageId" value="%{id}"/>
								<s:hidden name="id" value="%{tableId}"/>
								<img src="<s:url value="/img/remove.png"/>" class="editImg" alt="Supprimer" onclick="javascript:deleteMessage(this);" id="removeMessageImg_${id}"/>
							</s:form>
						</div>
						<div class="messageDate"><s:date name="date" format="dd/MM/yyyy HH:mm:ss"/></div>
						<div class="messageContent">
							<span class="tableMessageContent"><s:property value="data" escape="false"/></span>
						</div>
					</td>
				</tr>
			</s:iterator>
			<tr>
				<td>
					<s:form theme="simple" id="addMessageForm" action="EditTable!newMessage" cssClass="tableButton">
						<s:hidden name="id" value="%{id}"/>
						<s:textarea id="message" name="message" cols="60" rows="8"/>
						<script type="text/javascript">
							var editor = CKEDITOR.replace('message', {
								toolbar : 'Basic',
							});
						</script>
						<s:submit name="addMessage" value="Ajouter"/>
						<s:submit name="sendMessage" value="Envoyer au Joueurs"/>
					</s:form>
					<s:form theme="simple" action="EditTable!refreshMessages" cssClass="tableButton">
						<s:hidden name="id" value="%{id}"/>
						<s:submit value="Récupérer les messages récemment envoyés"/>
					</s:form>
		
				</td>
			</tr>
		</table>
	</div>

	<div class="tableFooterElement">Identifiant de la table = <s:property value="table.id"/></div>
</s:push>



<s:include value="include/footer.jsp"/>

