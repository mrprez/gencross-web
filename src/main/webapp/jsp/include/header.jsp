<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<head>
		<title>Gen-Cross</title>
		<meta http-equiv="X-UA-Compatible" content="IE=8"/>
		<script language="JavaScript" type="text/javascript" src="<s:url value="/js/jquery-1.10.1.js"/>" charset="utf-8"></script>
		<script language="JavaScript" type="text/javascript" src="<s:url value="/dhtmlxscheduler/dhtmlxscheduler.js"/>" charset="utf-8"></script>
		<script language="JavaScript" type="text/javascript" src="<s:url value="/dhtmlxscheduler/locale/locale_fr.js"/>" charset="utf-8"></script>
		<script language="JavaScript" type="text/javascript" src="<s:url value="/js/ajax.jsp"/>" charset="utf-8"></script>
		<script language="JavaScript" type="text/javascript" src="<s:url value="/js/tree.js"/>" charset="utf-8"></script>
		<script language="JavaScript" type="text/javascript" src="<s:url value="/js/script.js"/>" charset="utf-8"></script>
		<link href="<s:url value="/css/genCross.css"/>" rel="stylesheet" type="text/css"/>
		<link href="<s:url value="/dhtmlxscheduler/dhtmlxscheduler.css"/>" rel="stylesheet" type="text/css"/>
		<link rel="icon" type="image/jpeg" href="<s:url value="/img/icone_GenCross.jpg"/>" />
	</head>
	<body onload="$('#ajaxError').ajaxError(displayAjaxError);">
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
		