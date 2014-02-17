<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/gencross-taglib-URI" prefix="gcr"%>

<s:if test="subProperties!=null">
	<s:if test="personnageWork.propertiesExpanding.containsKey(absoluteName)">
		<s:set name="expandStyle" value="personnageWork.propertiesExpanding.get(absoluteName)"/>
	</s:if>
	<s:else>
		<s:set name="expandStyle" value="%{'expandable'}"/>
	</s:else>
</s:if>
<s:else>
	<s:set name="expandStyle" value="%{'unexpandable'}"/>
</s:else>

<li id="li_${param.propertyNum}" class="${expandStyle}, property">
	<script language="Javascript">
		numberAssociation['<s:property value="absoluteName" escape="false" escapeJavaScript="true"/>'] = '${param.propertyNum}';
	</script>
	<s:if test="subProperties!=null">
		<a class="motherPropertyName" onClick="javascript:expandCollapse('<s:property value="absoluteName" escapeJavaScript="true"/>','${param.propertyNum}');" href="javascript:;">
			<img class="expandImg" src="<s:url value="/img/%{expandStyle}.jpg" includeParams="none"/>" alt="Déplier/Replier" width="10" height="10"/>
			<span id="span_${param.propertyNum}" onmouseover="javascript:displayDelayComment(this, event,'<s:property value="absoluteName" escape="false" escapeJavaScript="true"/>')" onMouseOut="javascript:stopDelayComment()">
				<s:property value="fullName"/>
				<s:if test="value!=null">
					: <s:property value="value"/>
				</s:if>
			</span>
		</a>
	</s:if>
	<s:else>
		<img class="expandImg" src="<s:url value="/img/%{expandStyle}.jpg" includeParams="none"/>" alt="Déplier/Replier" width="10" height="10"/>
		<span id="span_${param.propertyNum}" onmouseover="javascript:displayDelayComment(this, event,'<s:property value="absoluteName" escape="false" escapeJavaScript="true"/>')" onMouseOut="javascript:stopDelayComment()">
			<s:property value="fullName"/>
			<s:if test="value!=null">
				: <s:property value="value"/>
			</s:if>
		</span>
	</s:else>
	
	<s:if test="%{editable && value!=null}">
		<img class="editImg editPropertyImg" src="<s:url value="/img/edit.png"/>" alt="Editer" onClick="javascript:displayEditForm(this);"/>
		<s:set name="formId">form_${param.propertyNum}</s:set>
		<s:form id="%{formId}" cssClass="editForm" theme="simple" onsubmit="return false;">
			<s:hidden name="propertyAbsoluteName" value="%{absoluteName}"/>
			<s:if test="options!=null">
				<s:select name="newValue" list="options" value="value"/>
			</s:if>
			<s:elseif test="value.offset!=null">
				<s:hidden cssClass="hiddenNewValue" name="newValue" value="%{value}"/>
				<s:if test="value.equals(min)">
					<s:set name="minusDisabled" value="%{'disabled'}"/>
				</s:if>
				<s:else>
					<s:set name="minusDisabled" value=""/>
				</s:else>
				<s:if test="value.equals(max)">
					<s:set name="plusDisabled" value="%{'disabled'}"/>
				</s:if>
				<s:else>
					<s:set name="plusDisabled" value="%{''}"/>
				</s:else>
				<s:if test="min==null">
					<s:set name="min" value="%{'NaN'}"/>
				</s:if>
				<s:if test="max==null">
					<s:set name="max" value="%{'NaN'}"/>
				</s:if>
				<button class="minusButton" type="button" onClick="javascript:minusValue(this, ${value.offset}, ${min}, ${max});" ${minusDisabled}><img src="<s:url value="/img/minus%{minusDisabled}.png"/>" alt="-"/></button>
				<span class="displayedNewValue"><s:property value="value"/></span>
				<button class="plusButton" type="button" onClick="javascript:plusValue(this, ${value.offset}, ${min}, ${max});" ${plusDisabled}><img src="<s:url value="/img/plus%{plusDisabled}.png"/>" alt="+"/></button>
			</s:elseif>
			<s:else>
				<s:textfield name="newValue" key="value" value="%{value}"/>
			</s:else>
			<button onclick="javascript:setNewValue('${param.propertyNum}')" type="button">Valider</button>
			<img class="foldImg" src="<s:url value="/img/fold.png"/>" alt="Replier" onClick="javascript:hideEditFormFromImg(this);"/>
		</s:form>
	</s:if>
	
	<s:if test="removable && owner instanceof com.mrprez.gencross.Property && owner.getSubProperties().canRemoveElement()">
		<s:set name="formId">removeForm_${param.propertyNum}</s:set>
		<s:form cssClass="removeForm" id="%{formId}" theme="simple">
			<s:hidden name="propertyAbsoluteName" value="%{absoluteName}"/>
			<img class="removeImg" src="<s:url value="/img/remove.png"/>" alt="Supprimer" onClick="javascript:removeProperty('${param.propertyNum}');"/>
		</s:form>
	</s:if>
	
	<div class="comment" id="comment_${param.propertyNum}">
		<s:if test="comment!=null">
			<s:property value="comment.replaceAll(\"\\n\",\"<br/>\")"/>
		</s:if>
		<s:else>
			Aucun commentaire
		</s:else>
	</div>
	
	<s:if test="subProperties!=null">
		<ul id="subProperties_${param.propertyNum}" class="${expandStyle}">
			<s:set name="num" value="%{0}"/>
			<s:iterator value="subProperties.iterator()">
				<s:include value="editProperty.jsp">
					<s:param name="propertyNum">${param.propertyNum}_${num}</s:param>
				</s:include>
				<s:set name="num">${num+1}</s:set>
			</s:iterator>
			<s:if test="!subProperties.isFixe()">
				<s:property value="subProperties.options"/>
				<s:if test="subProperties.getOptions().size()>0">
					<li class="addProperty">
						<span class="addProperty" onclick="javascript:displayAddPropertyForm(this)">Ajouter par choix</span>
						<s:set name="formId">addPropertyFromOptionForm_${param.propertyNum}</s:set>
						<s:form id="%{formId}" cssClass="addPropertyForm" theme="simple" onsubmit="return false;">
							<s:hidden name="motherProperty" value="%{absoluteName}"/>
							<s:select name="optionProperty" list="subProperties.getOptions().values()" listValue="fullName" listKey="fullName"/>
							<button onclick="javascript:addPropertyFromOption('${param.propertyNum}')" type="button">Valider</button>
							<img class="foldImg" src="<s:url value="/img/fold.png"/>" alt="Replier" onClick="javascript:hideAddPropertyForm(this);"/>
						</s:form>
						<script language="Javascript">
							specification['${param.propertyNum}'] = new Array();
							<s:iterator value="subProperties.getOptions().values()">
								<s:if test="specification!=null">
									specification['${param.propertyNum}']['<s:property value="fullName" escape="false" escapeJavaScript="true"/>']='<s:property value="specification"/>';
								</s:if>
							</s:iterator>
						</script>
					</li>
				</s:if>
				<s:if test="subProperties.isOpen()">
					<li class="addProperty">
						<span class="addProperty" onclick="javascript:displayAddPropertyForm(this)">Ajout libre</span>
						<s:set name="formId">addFreePropertyForm_${param.propertyNum}</s:set>
						<s:form id="%{formId}" cssClass="addPropertyForm" theme="simple" onsubmit="return false;">
							<s:hidden name="motherProperty" value="%{absoluteName}"/>
							<s:textfield name="newProperty"/>
							<button onclick="javascript:addFreeProperty('${param.propertyNum}')" type="button">Valider</button>
							<img class="foldImg" src="<s:url value="/img/fold.png"/>" alt="Replier" onClick="javascript:hideAddPropertyForm(this);"/>
						</s:form>
					</li>
				</s:if>
				
				
			</s:if>
	
		</ul>
	</s:if>
	
</li>
	


		