<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>


<script language="Javascript">
	personnageWorkId = <s:property value="personnageWork.id" escape="false" escapeJavaScript="true"/>;
</script>


<div id="personnageName">
	<span><s:property value="personnageWork.name"/></span>
	<s:if test="personnageWork.player!=null">
		<span id="playerName">Joueur: <s:property value="personnageWork.player.username"/></span>
	</s:if>
	<s:if test="personnageWork.gameMaster!=null">
		<span id="gamemasterName">MJ: <s:property value="personnageWork.gameMaster.username"/></span>
	</s:if>
	<s:if test="personnageWork.table!=null">
		<span id="tableName">Table: 
			<s:if test="isGameMaster">
				<s:a action="EditTable">
					<s:param name="id"><s:property value="personnageWork.table.id"/></s:param>
					<s:property value="personnageWork.table.name"/>
				</s:a>
			</s:if>
			<s:else>
				<s:property value="personnageWork.table.name"/>
			</s:else>
		</span>
	</s:if>
</div>

<s:push value="personnageWork.personnage">
	<table id="personnageMainTable">
		<tr id="personnageHeader">
			<td>
				<span id="phasePanel">
					Phase: <s:property value="phase"/>
				</span>
				<span class="actionIcon" id="helpFile">
					<s:if test="helpFileName!=null">
						<s:url id="helpFileUrl" action="HelpFile"><s:param name="personnageWorkId" value="personnageWork.id"/></s:url>
						<s:a href="%{helpFileUrl}" javascriptTooltip="Fichier d'aide"><img src="<s:url value="/img/helpFile.png"/>" alt="Fichier d'aide" width="20" height="20"/></s:a>
					</s:if>
					<s:else>
						<img src="<s:url value="/img/helpFileDisabled.png"/>" alt="Fichier d'aide" width="20" height="20"/>
					</s:else>
				</span>
				<span class="actionIcon">
					<s:a href="%{reloadUrl}"><img src="<s:url value="/img/refresh.png"/>" alt="Recharger la page" width="20" height="20" title="Recharger la page"/></s:a>
				</span>
				<s:if test="isGameMaster">
					<span class="actionIcon">
						<s:form action="Edit!validatePersonnage" theme="simple" method="get"><s:hidden name="personnageId"/><s:submit value="Valider"/></s:form>
					</span>
				</s:if>
				<span class="actionIcon">
					<s:form action="Edit!unvalidatePersonnage" theme="simple" method="get"><s:hidden name="personnageId"/><input type="button" value="Annuler" onclick="javascript:unvalidate(this)"/></s:form>
				</span>
				<span class="actionIcon">
					<s:form action="Export" theme="simple" method="get"><s:hidden name="personnageId"/><s:submit value="Export"/></s:form>
				</span>
				<span class="actionIcon">
					<s:form action="Background" theme="simple" method="get"><s:hidden name="personnageId"/><s:submit value="Background"/></s:form>
				</span>
				
			</td>
			<td class="historyColumn">
				<span id="lastValidationDateText">
					<s:if test="personnageWork.validationDate!=null">
						Dernière validation du MJ le <span id="lastValidationDate" title="<s:date name="personnageWork.validationDate" format="HH:mm:ss,SSS dd/MM/yyyy"/>"><s:date name="personnageWork.validationDate" format="dd/MM/yyyy à HH:mm:ss"/></span>
						<button id="seeNotValidateButton" onclick="javascript:manageHightlightHistory()">Voir le non-validé</button>
					</s:if>
					<s:else>
						Personnage jamais validé par le MJ
					</s:else>
				</span>
				<span class="actionIcon">
					<button onclick="javascript:hideHistory(this)">Masquer &gt;&gt;</button>
				</span>
			</td>
			<td id="displayButtons" rowspan="2">
				<button onclick="javascript:displayHistory(this)">&lt;&lt;<br/>H<br/>i<br/>s<br/>t<br/>o<br/>r<br/>i<br/>q<br/>u<br/>e<br/>&lt;&lt;</button>
			</td>
		</tr>
		<tr>
			<td id="treePanel">
				<div class="pointPool">
					<table class="pointPool">
						<s:iterator value="pointPools.values" status="itStatut">
							<s:if test="#itStatut.odd == true">
								<s:include value="include/pointPool.jsp">
									<s:param name="index" value="#itStatut.index"/>
								</s:include>
							</s:if>
						</s:iterator>
					</table>
					<table class="pointPool">
						<s:iterator value="pointPools.values" status="itStatut">
							<s:if test="#itStatut.even == true">
								<s:include value="include/pointPool.jsp">
									<s:param name="index" value="#itStatut.index"/>
								</s:include>
							</s:if>
						</s:iterator>
					</table>
				</div>
				
				<s:include value="include/errors.jsp"/>
				
				<s:set name="propertyNumPrefix" value="%{0}"/>
				<s:set name="propertyLastNum" value="%{0}"/>
				<ul>
					<s:set name="num" value="%{0}"/>
					<s:iterator value="properties" status="status" id="prop">
						<s:include value="include/editProperty.jsp">
							<s:param name="propertyNum">${num}</s:param>
						</s:include>
						<s:set name="num">${num+1}</s:set>
					</s:iterator>
				</ul>
				
				<s:form action="Edit!nextPhase">
					<s:hidden name="personnageId" value="%{personnageWork.id}"/>
					<s:if test="personnageWork.personnage.phaseFinished()">
						<s:submit id="nextPhaseButton" value="Phase suivante"/>
					</s:if>
					<s:else>
						<s:submit id="nextPhaseButton" value="Phase suivante" disabled="true"/>
					</s:else>
				</s:form>
				
				<s:if test="actionMessage!=null">
					<script language="Javascript">
						alert('<s:property value="actionMessage" escape="false"/>');
					</script> 
				</s:if>
			</td>
			
			<td id="historyPanel" class="historyColumn">
			
				<table id="historyTable">
					<thead>
						<tr>
							<th class="historyIndex">Index</th>
							<th>Propriété</th>
							<th>Ancien</th>
							<th>Nouveau</th>
							<th>Coût</th>
							<th>Date</th>
						</tr>
					</thead>
					<tbody>
						<s:iterator value="reversedIteratorHistory" status="stat">
							<s:include value="include/historyItem.jsp">
								<s:param name="historyItemIndex" value="%{(history.size()-#stat.count)}"/>
							</s:include>
						</s:iterator>
					</tbody>
				</table>
			
			</td>
			
		</tr>
	</table>
	
	<span class="personnageFooterElement">Identifiant du personnage = <s:property value="personnageWork.id"/></span>
	<span class="personnageFooterElement">Plugin = <s:property value="personnageWork.personnage.pluginDescriptor.name"/> - <s:property value="personnageWork.personnage.pluginDescriptor.version"/></span>
	
</s:push>



<s:include value="include/footer.jsp"/>

