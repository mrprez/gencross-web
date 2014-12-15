<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>

<div id="waitMask"></div>

<s:actionerror/>
<table class="genCrossTable">
	<thead>
		<tr>
			<th class="genCrossTable">Nom</th>
			<th class="genCrossTable">Type</th>
			<th class="genCrossTable">MJ</th>
			<th class="genCrossTable">Actions</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="tableList">
			<tr class="genCrossTable">
				<td class="genCrossTable">
					<s:a action="EditTable">
						<s:param name="id">
							<s:property value="id"/>
						</s:param>
						<s:property value="name"/>
					</s:a>
				</td>
				<td class="genCrossTable"><s:property value="type"/></td>
				<td class="genCrossTable"><s:property value="gameMaster.username"/>
				<td class="genCrossTable">
					<img onClick="javascript:deleteTable(${id})" class="editImg" src="<s:url value="/img/remove.png"/>" alt="Supprimer"/>
				</td>
			</tr>
		</s:iterator>
		<s:form theme="simple" action="TableList!addTable">
			<tr class="genCrossTable">
				<td class="genCrossTable">
					<s:textfield name="tableName"/>
				</td class="genCrossTable">
				<td class="genCrossTable"><s:select name="tableType" list="tableTypeList"/></td>
				<td class="genCrossTable"><s:property value="#session.user.username"/></td>
				<td class="genCrossTable"><s:submit value="Créer"/></td>
			</tr>
		</s:form>
	</tbody>
</table>

<div id="deleteTableDiv">
	<s:form theme="simple" action="TableList!removeTable">
		<s:hidden id="tableId" name="tableId" value=""/>
		<div id="deleteTableConfirm">
			Etes vous sur de vouloir supprimer cette table?
		</div>
		<div id="deleteTablePersonnage">
			Supprimer également <s:select name="personnageDeletion" list="@com.mrprez.gencross.web.action.TableListAction$PersonnageDeleteOption@values()" listValue="%{getText()}"/>
		</div>
		<center id="deleteTableButtons">
			<s:submit class="deleteTableFormButton" value="Valider"/>
			<button type="button" class="deleteTableFormButton" onclick="javascript:cancelDeleteTable();">Annuler</button>
		</center>
	</s:form>
</div>


<s:include value="include/footer.jsp"/>