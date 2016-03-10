package com.mrprez.gencross.web.test.action.dwr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.history.HistoryItem;
import com.mrprez.gencross.web.action.dwr.EditPersonnageAjaxAction;
import com.mrprez.gencross.web.action.dwr.PersonnageChange;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.web.bs.face.IPersonnageComparatorBS;
import com.mrprez.gencross.web.test.action.AbstractActionTest;
import com.mrprez.gencross.web.test.bs.AuthentificationBSTest;
import com.opensymphony.xwork2.ActionContext;

@RunWith(MockitoJUnitRunner.class)
public class EditPersonnageAjaxActionTest extends AbstractActionTest {

	@Mock
	private IPersonnageBS personnageBS;
	
	@Mock
	private IPersonnageComparatorBS personnageComparatorBS;

	@InjectMocks
	private EditPersonnageAjaxAction editPersonnageAjaxAction;
	
	
	@Mock
	private PersonnageWorkBO personnageWork;
	@Spy
	private Personnage personnage;
	@Spy
	private Personnage clone;
	private boolean phaseFinishedBefore = false;
	
	private Set<String> propertiesDifference = new HashSet<String>();
	private boolean hasSameErrors = true;
	private Set<String> pointPoolDifferences = new HashSet<String>();
	private boolean sameNextPhaseAvaibility = true;
	
	private String actionMessage;
	private List<HistoryItem> newHistoryList = new ArrayList<HistoryItem>();
	private boolean phaseFinishedAfter = false;


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
	
	
	@Test
	public void testUpdateValue_Fail() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		int personnageWorkId = 2;
		String propertyAbsoluteName = "Compéténces#Boomerang";
		String newValue = "3";
		
		// Execute
		Exception exception = null;
		try{
			editPersonnageAjaxAction.updateValue(personnageWorkId, propertyAbsoluteName, newValue);
		}catch(Exception e){
			exception = e;
		}
		
		// Check
		Assert.assertNotNull(exception);
		Assert.assertEquals("Personnage not loaded", exception.getMessage());
	}
	
	
	@Test
	public void testUpdateValue_Success() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		int personnageWorkId = 2;
		String propertyAbsoluteName = "Attributs#Carrure";
		String newValue = "3";
		buildMocksResponses(personnageWorkId, user, personnageBS.setNewValue(personnageWork, newValue, propertyAbsoluteName));
		
		// Execute
		PersonnageChange result = editPersonnageAjaxAction.updateValue(personnageWorkId, propertyAbsoluteName, newValue);
		
		// Check
		checkChanges(result);
	}
	
	
	private <T> void buildMocksResponses(int personnageWorkId, UserBO user, T businessMethodCall) throws Exception{
		Mockito.when(personnageBS.loadPersonnage(personnageWorkId, user)).thenReturn(personnageWork);
		Mockito.when(personnageWork.getPersonnage()).thenReturn(personnage);
		Mockito.when(personnage.clone()).thenReturn(clone);
		
		Mockito.when(businessMethodCall).thenAnswer(new Answer<T>() {
			@Override
			public T answer(InvocationOnMock invocation) throws Throwable {
				personnage.setActionMessage(actionMessage);
				personnage.getHistory().addAll(newHistoryList);
				return null;
			}
		});
		
		Mockito.when(personnageComparatorBS.findPropertiesDifferences(personnage, clone))
				.thenReturn(new HashSet<String>(propertiesDifference));
		Mockito.when(personnageComparatorBS.hasTheSameErrors(personnage, clone))
				.thenReturn(hasSameErrors);
		Mockito.when(personnageComparatorBS.findPointPoolDifferences(personnage, clone))
				.thenReturn(new HashSet<String>(pointPoolDifferences));
		Mockito.when(personnageComparatorBS.hasTheSameNextPhaseAvaibility(personnage, clone))
				.thenReturn(sameNextPhaseAvaibility);
	}
	
	
	private void checkChanges(PersonnageChange change){
		Assert.assertEquals(propertiesDifference, change.getPropertyNames());
		Assert.assertEquals(hasSameErrors, change.getErrorChanges());
		Assert.assertEquals(pointPoolDifferences, change.getPointPoolNames());
		
		// TODO
	}
	
}
