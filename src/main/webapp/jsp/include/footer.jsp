<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
		</div>
		<div id="footer">
			<center>
				<s:if test="#session.gencrossVersion!=null">
					Version de Gencross:<s:property value="#session.gencrossVersion"/>, 
				</s:if>
				<s:if test="#session.gencrossWebVersion!=null">
					Version de Gencross-Web:${version}<s:property value="#session.gencrossWebVersion"/>
				</s:if>
			</center>
		</div>
	</body>
</html>
