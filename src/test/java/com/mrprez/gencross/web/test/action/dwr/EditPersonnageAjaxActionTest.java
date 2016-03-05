package com.mrprez.gencross.web.test.action.dwr;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.dwr.EditPersonnageAjaxAction;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.web.test.action.AbstractActionTest;
import com.mrprez.gencross.web.test.bs.AuthentificationBSTest;
import com.opensymphony.xwork2.ActionContext;

@RunWith(MockitoJUnitRunner.class)
public class EditPersonnageAjaxActionTest extends AbstractActionTest {

	@Mock
	private IPersonnageBS personnageBS;

	@InjectMocks
	private EditPersonnageAjaxAction editPersonnageAjaxAction;


	@Test
	public void testExpand_Success() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		int personnageWorkId = 2;
		String propertyAbsoluteName = "Compétences#Boomerang";
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		Mockito.when(personnageBS.loadPersonnage(personnageWorkId, user)).thenReturn(personnageWork);
		
		// Execute
		editPersonnageAjaxAction.expand(personnageWorkId, propertyAbsoluteName);
		
		// Check
		@SuppressWarnings("unchecked")
		Set<String> expandedProperties = (Set<String>) ActionContext.getContext().getSession().get("personnagesWorks2");
		Assert.assertTrue(expandedProperties.contains(propertyAbsoluteName));
	}
	
	
	@Test
	public void testExpand_Fail() throws Exception{
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		int personnageWorkId = 2;
		String propertyAbsoluteName = "Compétences#Boomerang";
		Mockito.when(personnageBS.loadPersonnage(personnageWorkId, user)).thenReturn(null);
		
		// Execute
		editPersonnageAjaxAction.expand(personnageWorkId, propertyAbsoluteName);
		
		// Check
		Assert.assertNull(ActionContext.getContext().getSession().get("personnagesWorks2"));
	}
	
	
	@Test
	public void testCollapse_Success() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		int personnageWorkId = 2;
		String propertyAbsoluteName = "Compétences#Boomerang";
		ActionContext.getContext().getSession().put("personnagesWorks2", new HashSet<String>(Arrays.asList(propertyAbsoluteName)));
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		Mockito.when(personnageBS.loadPersonnage(personnageWorkId, user)).thenReturn(personnageWork);
		
		// Execute
		editPersonnageAjaxAction.collapse(personnageWorkId, propertyAbsoluteName);
		
		// Check
		@SuppressWarnings("unchecked")
		Set<String> expandedProperties = (Set<String>) ActionContext.getContext().getSession().get("personnagesWorks2");
		Assert.assertFalse(expandedProperties.contains(propertyAbsoluteName));
	}
	
	
	@Test
	public void testCollapse_Fail() throws Exception{
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		int personnageWorkId = 2;
		String propertyAbsoluteName = "Compétences#Boomerang";
		ActionContext.getContext().getSession().put("personnagesWorks2", new HashSet<String>(Arrays.asList(propertyAbsoluteName)));
		Mockito.when(personnageBS.loadPersonnage(personnageWorkId, user)).thenReturn(null);
		
		// Execute
		editPersonnageAjaxAction.collapse(personnageWorkId, propertyAbsoluteName);
		
		// Check
		@SuppressWarnings("unchecked")
		Set<String> expandedProperties = (Set<String>) ActionContext.getContext().getSession().get("personnagesWorks2");
		Assert.assertTrue(expandedProperties.contains(propertyAbsoluteName));
	}
	
}
