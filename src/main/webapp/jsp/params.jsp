<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<gcr:security-redirection target="/jsp/login.jsp"/>
<s:include value="include/header.jsp"/>

<table class="genCrossTable">
	<tr class="genCrossTable">
		<th class="genCrossTable">Clé</th>
		<th class="genCrossTable">Valeur</th>
		<th class="genCrossTable">Action</th>
	</tr>
	<s:iterator value="paramList">
		<tr class="genCrossTable">
			<td class="genCrossTable"><s:property value="key"/></td>
			<td class="genCrossTable">
				<s:if test="%{type.equals('D')}">
					<s:date name="value" format="dd/MM/yyyy hh:mm:ss,SSS"/>
				</s:if>
				<s:else>
					<s:property value="value"/>
				</s:else>
			</td>
			<td class="genCrossTable">
				<img class="editImg" src="<s:url value="/img/edit.png"/>" alt="Edit" onclick="javascript:displayEditParam(this, '${key}');"/>
				<s:form action="Params!edit" theme="simple" cssClass="paramForm" id="paramForm_%{key}">
					<s:hidden name="key"/>
					<s:if test="%{type.equals('D')}">
						<s:textfield name="day" id="dateField_%{key.replace('.','_')}" size="10">
							<s:param name="value"><s:date name="value" format="dd/MM/yyyy" /></s:param>
						</s:textfield>
						<script type="text/javascript">
							$("#dateField_${key}".replace(/\./g,"_")).datepicker();
						</script>
						<s:select name="hour" list="{'00','01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23'}">
							<s:param name="value"><s:date name="value" format="HH" /></s:param>
						</s:select>
						:
						<s:select name="minutes" list="{'00','01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31','32','33','34','35','36','37','38','39','40','41','42','43','44','45','46','47','48','49','50','51','52','53','54','55','56','57','58','59'}">
							<s:param name="value"><s:date name="value" format="mm" /></s:param>
						</s:select>
						:
						<s:select name="seconds" list="{'00','01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31','32','33','34','35','36','37','38','39','40','41','42','43','44','45','46','47','48','49','50','51','52','53','54','55','56','57','58','59'}">
							<s:param name="value"><s:date name="value" format="ss" /></s:param>
						</s:select>
						,
						<s:textfield name="milliSeconds" size="3">
							<s:param name="value"><s:date name="value" format="SSS" /></s:param>
						</s:textfield>
					</s:if>
					<s:elseif test="%{type.equals('B')}">
						<s:radio name="newValue" value="value" list="{'true','false'}"/>
					</s:elseif>
					<s:else>
						<s:textfield name="newValue" value="%{value}"/>
					</s:else>
					<s:set name="imageUrl"><s:url value="/img/validate.png"/></s:set>
					<s:submit type="image" src="%{imageUrl}"/>
				</s:form>
			</td>
		</tr>

	</s:iterator>
</table>

<s:include value="include/footer.jsp"/>