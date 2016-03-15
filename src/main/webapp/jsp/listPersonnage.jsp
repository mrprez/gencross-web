<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>



<table class="personnageListContainer">
	<tr class="listePersonnageTitre">
		<td class="listePersonnageTitre">Vos personnages en tant que joueur:</td>
		<td class="listePersonnageTitre">Vos personnages en tant que MJ:</td>
	</tr>
	<tr>
		<td class="personnageListContainer">
			<table class="genCrossTable">
				<thead>
					<tr>
						<th class="genCrossTable">
							<s:a action="List!sort">
								<s:param name="playerPersonnageSort">name</s:param>
								Nom
								<s:if test="playerPersonnageComparator.name == 'name'">
									<img src="<s:url value="/img/%{playerSortDir}Sorted.png"/>" alt="${playerSortDir}"/>
								</s:if>
							</s:a>
						</th>
						<th class="genCrossTable">
							<s:a action="List!sort">
								<s:param name="playerPersonnageSort">type</s:param>
								Type
								<s:if test="playerPersonnageComparator.name == 'type'">
									<img src="<s:url value="/img/%{playerSortDir}Sorted.png"/>" alt="${playerSortDir}"/>
								</s:if>
							</s:a>
						</th>
						<th class="genCrossTable">
							<s:a action="List!sort">
								<s:param name="playerPersonnageSort">GM</s:param>
								MJ
								<s:if test="playerPersonnageComparator.name == 'GM'">
									<img src="<s:url value="/img/%{playerSortDir}Sorted.png"/>" alt="${playerSortDir}"/>
								</s:if>
							</s:a>
						</th>
						<th class="genCrossTable">
							<s:a action="List!sort">
								<s:param name="playerPersonnageSort">table</s:param>
								Table
								<s:if test="playerPersonnageComparator.name == 'table'">
									<img src="<s:url value="/img/%{playerSortDir}Sorted.png"/>" alt="${playerSortDir}"/>
								</s:if>
							</s:a>
						</th>
						<th class="genCrossTable">Actions</th>
					</tr>
				</thead>
				<tbody>
					<s:iterator value="playerPersonnageList">
						<tr class="genCrossTable">
							<td class="genCrossTable" title="ID=<s:property value="id"/>">
								<s:a action="Edit">
									<s:param name="personnageId">
										<s:property value="id"/>
									</s:param>
									<s:property value="name"/>
								</s:a>
							</td>
							<td class="genCrossTable">
								<s:property value="pluginName"/>
							</td>
							<td class="genCrossTable">
								<s:property value="gameMaster.username"/>
							</td>
							<td class="genCrossTable">
								<s:property value="table.name"/>
							</td>
							<td class="genCrossTable">
								<s:a action="Edit" title="Editer">
									<s:param name="personnageId">
										<s:property value="id"/>
									</s:param>
									<img src="<s:url value="/img/edit.png"/>" alt="Fermer"/>
								</s:a>
								<img class="editImg" src="<s:url value="/img/attributeGM.png"/>" alt="Attribuer un MJ" onclick="Javascript:attributeGameMaster(<s:property value="id"/>);"/>
								<s:a action="List" method="downloadAsPlayer" title="Télécharger au format gcr">
									<s:param name="personnageId">
										<s:property value="id"/>
									</s:param>
									<img src="<s:url value="/img/download.png"/>" alt="Télécharger"/>
								</s:a>
								<s:a action="Upload" title="Charger au format gcr">
									<s:param name="personnageId">
										<s:property value="id"/>
									</s:param>
									<img src="<s:url value="/img/upload.png"/>" alt="Charger" onclick="Javascript:uploadPersonnage(this)"/>
								</s:a>
								<s:form cssClass="imgForm" action="List!deletePersonnage" method="post" theme="simple">
									<s:hidden name="personnageId" value="%{id}"/>
									<img src="<s:url value="/img/remove.png"/>" class="editImg" alt="Supprimer" onclick="javascript:deletePersonnagePJ(this);" />
								</s:form>
							</td>
						</tr>
					</s:iterator>
					<s:if test="playerPersonnageList.isEmpty()">
						<tr class="genCrossTable">
							<td class="genCrossTable" colspan="5">
								Aucun personnage
							</td>
						</tr>
					</s:if>
				</tbody>
			</table>
		</td>
		
		
		
		<td class="personnageListContainer">
			<table class="genCrossTable">
				<thead>
					<tr>
						<th class="genCrossTable">
							<s:a action="List!sort">
								<s:param name="gameMasterPersonnageSort">name</s:param>
								Nom
								<s:if test="GMPersonnageComparator.name == 'name'">
									<img src="<s:url value="/img/%{gameMasterSortDir}Sorted.png"/>" alt="${gameMasterSortDir}"/>
								</s:if>
							</s:a>
						</th>
						<th class="genCrossTable">
							<s:a action="List!sort">
								<s:param name="gameMasterPersonnageSort">type</s:param>
								Type
								<s:if test="GMPersonnageComparator.name == 'type'">
									<img src="<s:url value="/img/%{gameMasterSortDir}Sorted.png"/>" alt="${gameMasterSortDir}"/>
								</s:if>
							</s:a>
						</th>
						<th class="genCrossTable">
							<s:a action="List!sort">
								<s:param name="gameMasterPersonnageSort">player</s:param>
								Joueur
								<s:if test="GMPersonnageComparator.name == 'player'">
									<img src="<s:url value="/img/%{gameMasterSortDir}Sorted.png"/>" alt="${gameMasterSortDir}"/>
								</s:if>
							</s:a>
						</th>
						<th class="genCrossTable">
							<s:a action="List!sort">
								<s:param name="gameMasterPersonnageSort">table</s:param>
								Table
								<s:if test="GMPersonnageComparator.name == 'table'">
									<img src="<s:url value="/img/%{gameMasterSortDir}Sorted.png"/>" alt="${gameMasterSortDir}"/>
								</s:if>
							</s:a>
						</th>
						<th class="genCrossTable">Actions</th>
					</tr>
				</thead>
				<tbody>
					<s:iterator value="gameMasterPersonnageList">
						<tr class="genCrossTable">
							<td class="genCrossTable" title="ID=<s:property value="id"/>">
								<s:a action="Edit">
									<s:param name="personnageId">
										<s:property value="id"/>
									</s:param>
									<s:property value="name"/>
								</s:a>
							</td>
							<td class="genCrossTable">
								<s:property value="pluginName"/>
							</td>
							<td class="genCrossTable">
								<s:property value="player.username"/>
							</td>
							<td class="genCrossTable" title="ID=<s:property value="table!=null?table.id:\"NA\""/>">
								<s:if test="table.id!=null">
									<s:a action="EditTable">
										<s:param name="id">
											<s:property value="table.id"/>
										</s:param>
										<s:property value="table.name"/>
									</s:a>
								</s:if>
							</td>
							<td class="genCrossTable">
								<s:a action="Edit" title="Editer">
									<s:param name="personnageId">
										<s:property value="id"/>
									</s:param>
									<img src="<s:url value="/img/edit.png"/>" alt="Fermer"/>
								</s:a>
								<img class="editImg" src="<s:url value="/img/attributePlayer.png"/>" alt="Attribuer un Joueur" onclick="Javascript:attributePlayer(<s:property value="id"/>);"/>
								<s:form onsubmit="Javascript:askMJPassword(this)" cssClass="imgForm" action="List!downloadAsGameMaster" method="post" theme="simple">
									<s:hidden name="personnageId" value="%{id}"/>
									<s:hidden name="password"/>
									<s:set name="imageUrl">
										<s:url value="/img/download.png"/>
									</s:set>
									<s:submit type="image" value="Télécharger" src="%{imageUrl}" title="Télécharger au format gcr"/>
								</s:form>
								<s:a action="Upload" title="Charger au format gcr">
									<s:param name="personnageId"><s:property value="id"/></s:param>
									<img src="<s:url value="/img/upload.png"/>" alt="Charger" onclick="Javascript:uploadPersonnage(this)"/>
								</s:a>
								<s:form cssClass="imgForm" action="List!deletePersonnage" method="post" theme="simple">
									<s:hidden name="personnageId" value="%{id}"/>
									<img src="<s:url value="/img/remove.png"/>" class="editImg" alt="Supprimer" onclick="javascript:deletePersonnageMJ(this);" />
								</s:form>
							</td>
						</tr>
					</s:iterator>
					<s:if test="gameMasterPersonnageList.isEmpty()">
						<tr class="genCrossTable">
							<td class="genCrossTable" colspan="5">
								Aucun personnage
							</td>
						</tr>
					</s:if>
				</tbody>
			</table>

		</td>
	</tr>
</table>


<s:include value="include/footer.jsp"/>
