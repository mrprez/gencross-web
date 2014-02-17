<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>

<s:form cssClass="uploadForm" action="Upload!upload" enctype="multipart/form-data" method="post">
	<s:actionerror />
	<s:hidden name="personnageId"/>
	<s:textfield label="Nom" name="personnageName" disabled="%{personnageId!=null?\"true\":\"false\"}"/>
	<s:checkbox onchange="javascript:hideDisplayPassword(this);" label="En tant que MJ" name="gm" disabled="%{personnageId!=null?\"true\":\"false\"}"/>
	
	<s:password id="mjPassword" label="Mot de passe du MJ" name="password"/>
	<s:file label="Fichier à charger" name="gcrFile" accept="x-application/gcr"/>
	<s:submit align="center" value="Valider"/>
</s:form>

<s:if test="!gm">
	<script language="Javascript">
		$('#mjPassword').parent().parent().hide();
	</script>
</s:if>

<s:include value="include/footer.jsp"/>