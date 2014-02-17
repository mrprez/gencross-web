<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<head>
		<title>Gen-Cross</title>
		<meta http-equiv="X-UA-Compatible" content="IE=8"/>
		<script language="JavaScript" type="text/javascript" src="<s:url value="/js/jquery-1.10.1.js"/>"></script>
		<script language="JavaScript" type="text/javascript" src="<s:url value="/js/jquery-ui.js"/>"></script>
		<script language="JavaScript" type="text/javascript" src="<s:url value="/js/jquery.ui.datepicker-fr.js"/>"></script>
		<script language="JavaScript" type="text/javascript" src="<s:url value="/js/fullcalendar.js"/>"></script>
		<script language="JavaScript" type="text/javascript" src="<s:url value="/js/ajax.jsp"/>"></script>
		<script language="JavaScript" type="text/javascript" src="<s:url value="/js/tree.js"/>"></script>
		<script language="JavaScript" type="text/javascript" src="<s:url value="/js/script.js"/>"></script>
		<link href="<s:url value="/css/genCross.css"/>" rel="stylesheet" type="text/css"/>
		<link href="<s:url value="/css/jquery-ui-1.10.3.custom.css"/>" rel="stylesheet" type="text/css"/>
		<link href="<s:url value="/css/fullcalendar.css"/>" rel="stylesheet" type="text/css"/>
		<link rel="icon" type="image/jpeg" href="<s:url value="/img/icone_GenCross.jpg"/>" />
	</head>
	<body onload="$('#ajaxError').ajaxError(displayAjaxError);" class="<s:property value="#request.action.class.simpleName"/>">
		<div id="header">
			<div id="headerLeft">
				<s:a action="List">
					<img src="<s:url value="/img/logo_GenCross.jpg"/>"/>
				</s:a>
				<s:include value="menu.jsp"/>
			</div>
			<div id="headerRight">
				<s:if test="#session.user!=null">
					<span id="username"><s:property value="#session.user.username"/></span>
					<br/>
					<s:a id="disconnect" action="Disconnect">Déconnexion</s:a>
				</s:if>
			</div>
			<div class="separator"></div>
		</div>
		<div id="content">
		