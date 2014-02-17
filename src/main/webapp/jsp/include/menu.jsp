<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<table class="menu">
	<tbody>
		<tr>
			<s:if test="#session.user!=null">
				<td id="personnageMenu" class="menu" onclick="javascript:showSubMenu(this)">
					<span class="menu">Personnages</span>
					<ul class="subMenu">
						<li class="subMenu"><s:a action="List">Liste des personnages</s:a></li>
						<li class="subMenu"><s:a action="TableList">Liste des tables</s:a></li>
						<li class="subMenu"><s:a action="Create">Créer un personnage</s:a></li>
					</ul>
				</td>
				<script language="Javascript">
					$('#personnageMenu').mouseleave(function(){$('#personnageMenu').children('ul').hide('slow');});
				</script>
			
				<td id="accountManagementMenu" class="menu" onclick="javascript:showSubMenu(this)">
					<span class="menu">Gérer son compte</span>
					<ul class="subMenu">
						<li class="subMenu"><s:a action="ChangePassword">Changer son mot de passe.</s:a></li>
						<li class="subMenu"><s:a action="ChangeMail">Changer son adresse mail.</s:a></li>
					</ul>
				</td>
				<script language="Javascript">
					$('#accountManagementMenu').mouseleave(function(){$('#accountManagementMenu').children('ul').hide('slow');});
				</script>
				
				<td class="menu">
					<s:a cssClass="menu" action="link!downloads">Téléchargements</s:a>
				</td>
				
				<gcr:role allowedRole="manager">
					<td id="administrationMenu" class="menu" onclick="javascript:showSubMenu(this)">
						<span class="menu">Administration</span>
						<ul class="subMenu">
							<li class="subMenu"><s:a action="ListUser">Liste des utilisateurs</s:a></li>
							<li class="subMenu"><s:a action="Params">Gestion des paramètres</s:a></li>
							<li class="subMenu"><s:a action="JobProcessing">Traitements programmés</s:a></li>
							<li class="subMenu"><s:a action="Logger">Gestion des logs</s:a></li>
						</ul>
					</td>
					<script language="Javascript">
						$('#administrationMenu').mouseleave(function(){$('#administrationMenu').children('ul').hide('slow');});
					</script>
				</gcr:role>
				
			</s:if>
		</tr>
	</tbody>
</table>

