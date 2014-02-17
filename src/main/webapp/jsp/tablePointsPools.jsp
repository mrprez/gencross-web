<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>


<div id="tableTitle">
	<span><s:property value="table.name"/></span>
</div>

<s:actionerror/>

<table class="genCrossTable">
	<thead>
		<tr>
			<th class="genCrossTable">PJ</th>
			<th class="genCrossTable">Joueur</th>
			<th class="genCrossTable">Phase</th>
			<s:iterator value="pointPoolList">
				<th class="genCrossTable"><s:property/></th>
			</s:iterator>
			<th class="genCrossTable">Dernière modif</th>
			<th class="genCrossTable">Validation</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="pjList" var="pj">
			<tr class="highlightable">
				<td class="genCrossTable">
					<s:a action="Edit">
						<s:param name="personnageId">
							<s:property value="id"/>
						</s:param>
						<s:property value="name"/>
					</s:a>
				</td>
				<td class="genCrossTable"><s:property value="player.username"/></td>
				<td class="genCrossTable"><s:property value="personnage.phase"/></td>
				<s:iterator value="personnage.pointPools.values">
					<td class="genCrossTable"><s:property/></td>					
				</s:iterator>
				<td class="genCrossTable">
					<s:set name="lastHistoryDate" value="personnage.history[personnage.history.size-1].date"/>
					<s:date format="HH:mm" name="lastHistoryDate"/>&nbsp;<s:date format="dd/MM" name="lastHistoryDate"/>
				</td>
				<s:if test="validationDate==null">
					<s:set name="validateCssClass">unvalidated</s:set>
				</s:if>
				<s:elseif test="validationDate.before(personnage.history[personnage.history.size-1].date)">
					<s:set name="validateCssClass">unvalidated</s:set>
				</s:elseif>
				<s:else>
					<s:set name="validateCssClass" value=""/>
				</s:else>
				<td class="genCrossTable ${validateCssClass}">
					<s:date format="HH:mm" name="validationDate"/>&nbsp;<s:date format="dd/MM" name="validationDate"/>
				</td>
			</tr>
		</s:iterator>
		<tr class="highlightable">
			<td class="genCrossTable summaryTableLine" colspan="3">[Min, Max]</td>
			<s:iterator value="findMinMaxPjPoints().entrySet()">
				<td class="genCrossTable summaryTableLine">
					<s:if test="value[0]==value[1]">
						<s:property value="value[0]"/>
					</s:if>
					<s:else>
						[<s:property value="value[0]"/>..<s:property value="value[1]"/>]
					</s:else>
					<img class="editImg" src="<s:url value="/img/edit.png"/>" alt="Editer" onClick="javascript:displayPointPoolForm(this);"/>
					<s:form action="TablePointsPools!addPoints" cssClass="pointPoolForm" theme="simple">
						<s:hidden name="pointPoolName" value="%{key}"/>
						<s:hidden name="id" value="%{id}"/>
						<table>
							<tr>
								<td colspan="2"><s:textfield label="Nb de pts à ajouter:" class="pointPoolModification" name="pointPoolModification" size="5"/></td>
							</tr>
							<tr>
								<s:set name="imageUrl"><s:url value="/img/validate.png"/></s:set>
								<td><s:submit type="image" src="%{imageUrl}"/></td>
								<td><img class="foldImg" onclick="javascript:hidePointPoolForm(this)" src="<s:url value="/img/fold.png"/>"/></td>
							</tr>
						</table>
					</s:form>
				</td>
			</s:iterator>
		</tr>
	</tbody>
</table>

<div id="backToEditTableDiv">
	<s:a action="EditTable">
		<s:param name="id">
			<s:property value="table.id"/>
		</s:param>
		<img alt="<==" src="<s:url value="/img/left_arrow.png"/>"/> Retour
	</s:a>
</div>

<s:include value="include/footer.jsp"/>
	