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
		buildMocksResponses(personnageWorkId, user);
		Mockito.when(personnageBS.setNewValue(personnageWork, newValue, propertyAbsoluteName)).then(buildBusinessAnswer());
		
		// Execute
		PersonnageChange result = editPersonnageAjaxAction.updateValue(personnageWorkId, propertyAbsoluteName, newValue);
		
		// Check
		checkChanges(result);
		Mockito.verify(personnageBS).setNewValue(personnageWork, newValue, propertyAbsoluteName);
	}
	
	
	@Test
	public void testAddPropertyFromOption_Fail() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		int personnageWorkId = 2;
		String motherPropertyName = "Equipement";
		String optionName = "Cape";
		String specification = "Noire";
		
		// Execute
		Exception exception = null;
		try{
			editPersonnageAjaxAction.addPropertyFromOption(personnageWorkId, motherPropertyName, optionName, specification);
		}catch(Exception e){
			exception = e;
		}
		
		// Check
		Assert.assertNotNull(exception);
		Assert.assertEquals("Personnage not loaded", exception.getMessage());
	}
	
	
	@Test
	public void testAddPropertyFromOption_Success() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		int personnageWorkId = 2;
		String motherPropertyName = "Equipement";
		String optionName = "Cape";
		String specification = "Noire";
		HistoryItem historyItem = new HistoryItem();
		historyItem.setAbsoluteName("Equipement#Cape - Noire");
		newHistoryList.add(historyItem);
		buildMocksResponses(personnageWorkId, user);
		Mockito.when(personnageBS.addPropertyFromOption(personnageWork, motherPropertyName, optionName, specification)).then(buildBusinessAnswer());
		
		// Execute
		PersonnageChange result = editPersonnageAjaxAction.addPropertyFromOption(personnageWorkId, motherPropertyName, optionName, specification);
		
		// Check
		checkChanges(result);
		Mockito.verify(personnageBS).addPropertyFromOption(personnageWork, motherPropertyName, optionName, specification);
	}
	
	
	@Test
	public void testAddFreeProperty_Fail() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		int personnageWorkId = 2;
		String motherPropertyName = "Compétences";
		String newPropertyName = "Enquête";
		
		// Execute
		Exception exception = null;
		try{
			editPersonnageAjaxAction.addFreeProperty(personnageWorkId, motherPropertyName, newPropertyName);
		}catch(Exception e){
			exception = e;
		}
		
		// Check
		Assert.assertNotNull(exception);
		Assert.assertEquals("Personnage not loaded", exception.getMessage());
	}
	
	
	@Test
	public void testAddFreeProperty_Success() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		int personnageWorkId = 2;
		String motherPropertyName = "Compétences";
		String newPropertyName = "Enquête";
		phaseFinishedAfter = true;
		sameNextPhaseAvaibility = false;
		buildMocksResponses(personnageWorkId, user);
		Mockito.when(personnageBS.addFreeProperty(personnageWork, motherPropertyName, newPropertyName)).then(buildBusinessAnswer());
		
		// Execute
		PersonnageChange result = editPersonnageAjaxAction.addFreeProperty(personnageWorkId, motherPropertyName, newPropertyName);
		
		// Check
		checkChanges(result);
		Mockito.verify(personnageBS).addFreeProperty(personnageWork, motherPropertyName, newPropertyName);
	}
	
	
	@Test
	public void testRemoveProperty_Fail() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		int personnageWorkId = 2;
		String absolutePropertyName = "Compétences#Enquête";
		
		// Execute
		Exception exception = null;
		try{
			editPersonnageAjaxAction.removeProperty(personnageWorkId, absolutePropertyName);
		}catch(Exception e){
			exception = e;
		}
		
		// Check
		Assert.assertNotNull(exception);
		Assert.assertEquals("Personnage not loaded", exception.getMessage());
	}
	
	
	@Test
	public void testRemoveProperty_Success() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		int personnageWorkId = 2;
		String absolutePropertyName = "Compétences#Enquête";
		hasSameErrors = false;
		buildMocksResponses(personnageWorkId, user);
		Mockito.when(personnageBS.removeProperty(personnageWork, absolutePropertyName)).then(buildBusinessAnswer());
		
		// Execute
		PersonnageChange result = editPersonnageAjaxAction.removeProperty(personnageWorkId, absolutePropertyName);
		
		// Check
		checkChanges(result);
		Mockito.verify(personnageBS).removeProperty(personnageWork, absolutePropertyName);
	}
	
	
	@Test
	public void testModifyPointPool_Fail() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		int personnageWorkId = 2;
		String poolName = "Expérience";
		int addedValue = 10;
		
		// Execute
		Exception exception = null;
		try{
			editPersonnageAjaxAction.modifyPointPool(personnageWorkId, poolName, addedValue);
		}catch(Exception e){
			exception = e;
		}
		
		// Check
		Assert.assertNotNull(exception);
		Assert.assertEquals("Personnage not loaded", exception.getMessage());
	}
	
	
	@Test
	public void testModifyPointPool_Success() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		int personnageWorkId = 2;
		String poolName = "Expérience";
		int addedValue = 10;
		buildMocksResponses(personnageWorkId, user);
		Mockito.doAnswer(buildBusinessAnswer()).when(personnageBS).modifyPointPool(personnageWork, poolName, addedValue);
		
		// Execute
		PersonnageChange result = editPersonnageAjaxAction.modifyPointPool(personnageWorkId, poolName, addedValue);
		
		// Check
		checkChanges(result);
		Mockito.verify(personnageBS).modifyPointPool(personnageWork, poolName, addedValue);
	}
	
	
	@Test
	public void testModifyHistory_Fail() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		int personnageWorkId = 2;
		String poolName = "Expérience";
		int historyIndex = 4;
		int cost = 2;
		
		// Execute
		Exception exception = null;
		try{
			editPersonnageAjaxAction.modifyHistory(personnageWorkId, historyIndex, poolName, cost);
		}catch(Exception e){
			exception = e;
		}
		
		// Check
		Assert.assertNotNull(exception);
		Assert.assertEquals("Personnage not loaded", exception.getMessage());
	}
	
	
	@Test
	public void testModifyHistory_Success() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		int personnageWorkId = 2;
		String poolName = "Expérience";
		int historyIndex = 4;
		int cost = 2;
		buildMocksResponses(personnageWorkId, user);
		Mockito.doAnswer(buildBusinessAnswer()).when(personnageBS).modifyHistory(personnageWork, poolName, cost, historyIndex);
		
		// Execute
		PersonnageChange result = editPersonnageAjaxAction.modifyHistory(personnageWorkId, historyIndex, poolName, cost);
		
		// Check
		checkChanges(result);
		Mockito.verify(personnageBS).modifyHistory(personnageWork, poolName, cost, historyIndex);
	}
	
	
	private void buildMocksResponses(int personnageWorkId, UserBO user) throws Exception{
		Mockito.when(personnageBS.loadPersonnage(personnageWorkId, user)).thenReturn(personnageWork);
		Mockito.when(personnageWork.getPersonnage()).thenReturn(personnage);
		personnage.getPhaseList().add("Création");
		clone.getPhaseList().add("Création");
		Mockito.when(personnage.clone()).thenReturn(clone);
		Mockito.when(clone.phaseFinished()).thenReturn(phaseFinishedBefore);
		
		Mockito.when(personnageComparatorBS.findPropertiesDifferences(personnage, clone))
				.thenReturn(new HashSet<String>(propertiesDifference));
		Mockito.when(personnageComparatorBS.hasTheSameErrors(personnage, clone))
				.thenReturn(hasSameErrors);
		Mockito.when(personnageComparatorBS.findPointPoolDifferences(personnage, clone))
				.thenReturn(new HashSet<String>(pointPoolDifferences));
		Mockito.when(personnageComparatorBS.hasTheSameNextPhaseAvaibility(personnage, clone))
				.thenReturn(sameNextPhaseAvaibility);
	}
	
	
	private <T> Answer<T> buildBusinessAnswer(){
		return new Answer<T>() {
			@Override
			public T answer(InvocationOnMock invocation) throws Throwable {
				personnage.setActionMessage(actionMessage);
				personnage.getHistory().addAll(newHistoryList);
				Mockito.when(personnage.phaseFinished()).thenReturn(phaseFinishedAfter);
				return null;
			}
		};
	}
	
	
	private void checkChanges(PersonnageChange change){
		Assert.assertEquals(propertiesDifference, change.getPropertyNames());
		Assert.assertEquals(! hasSameErrors, change.getErrorChanges());
		Assert.assertEquals(pointPoolDifferences, change.getPointPoolNames());
		Assert.assertEquals(actionMessage, change.getActionMessage());
		Assert.assertNull(personnage.getActionMessage());
		if(phaseFinishedAfter==phaseFinishedBefore){
			Assert.assertNull(change.getPhaseFinished());
		}else{
			Assert.assertEquals(phaseFinishedAfter, change.getPhaseFinished());
		}
		Assert.assertEquals(newHistoryList.size(), change.getNewHistoryIndexes().size());
		for(int index=0; index<newHistoryList.size(); index++){
			Assert.assertEquals(index, change.getNewHistoryIndexes().get(index).intValue());
		}
	}
	
}
