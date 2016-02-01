<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>

<script language="JavaScript" type="text/javascript" src="<s:url value="/js/ckeditor/ckeditor.js"/>"></script>


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

<div id="backgroundDiv">
	<s:form id="backgroundForm" action="Background!save">
		<s:hidden name="personnageId" value="%{personnageWork.id}"/>
		<s:textarea id="bg" name="background" cols="60" rows="50"/>
		<script type="text/javascript">
			var editor = CKEDITOR.replace('bg', {
				toolbar : 'Basic',
				height: '500'
			});
			
			var saved = true;
			
			window.onbeforeunload = function(){
				if(saved == false){
					return confirm('Vous n'avez pas enregistré, vos modifications seront perdues.');
				}
			};
			
			CKEDITOR.instances['bg'].on('blur', function() {
				if (editor.checkDirty()) {
					saved = false;
				}
			});
			
			$("#backgroundForm").submit(function() {
				saved = true;
			});
			
		</script>
		<s:submit value="Enregistrer" align="center"/>
	</s:form>
</div>


<span class="actionIcon" id="characterSheetLink">
	<s:form action="Edit" theme="simple" method="get">
		<s:hidden name="personnageId" value="%{personnageWork.id}"/>
		<s:submit value="Feuille de personnage"/>
	</s:form>
</span>



<s:include value="include/footer.jsp"/>

