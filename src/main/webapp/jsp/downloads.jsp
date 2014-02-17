<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>

<ul>
	<li><s:a action="Download!getGenCrossUI">GenCross (setup.exe)</s:a></li>
</ul>


<s:include value="include/footer.jsp"/>