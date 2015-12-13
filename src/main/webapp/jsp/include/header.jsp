<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Gen-Cross</title>
		<meta http-equiv="X-UA-Compatible" content="IE=8"></meta>
		<script language="JavaScript" type="text/javascript" src="<s:url value="/js/jquery-1.10.1.js"/>" charset="utf-8"></script>
		<script language="JavaScript" type="text/javascript" src="<s:url value="/js/jquery-ui.js"/>" charset="utf-8"></script>
		<script language="JavaScript" type="text/javascript" src="<s:url value="/js/jquery.ui.datepicker-fr.js"/>" charset="utf-8"></script>
		<script language="JavaScript" type="text/javascript" src="<s:url value="/dhtmlxscheduler/dhtmlxscheduler.js"/>" charset="utf-8"></script>
		<script language="JavaScript" type="text/javascript" src="<s:url value="/dhtmlxscheduler/locale/locale_fr.js"/>" charset="utf-8"></script>
		<script language="JavaScript" type="text/javascript" src="<s:url value="/js/tree.js"/>" charset="utf-8"></script>
		<script language="JavaScript" type="text/javascript" src="<s:url value="/js/script.js"/>" charset="utf-8"></script>
		<link href="<s:url value="/css/genCross.css"/>" rel="stylesheet" type="text/css"></link>
		<link href="<s:url value="/css/jquery-ui-1.10.3.custom.css"/>" rel="stylesheet" type="text/css"></link>
		<link href="<s:url value="/dhtmlxscheduler/dhtmlxscheduler.css"/>" rel="stylesheet" type="text/css"></link>
		<link rel="icon" type="image/jpeg" href="<s:url value="/img/icone_GenCross.jpg"/>" ></link>
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
		