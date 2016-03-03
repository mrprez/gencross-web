package com.mrprez.gencross.web.test.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.PoolPoint;
import com.mrprez.gencross.web.action.TablePointsPoolsAction;
import com.mrprez.gencross.web.bo.PersonnageDataBO;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.ITableBS;
import com.mrprez.gencross.web.test.bs.AuthentificationBSTest;
import com.mrprez.gencross.web.test.bs.PersonnageWorkBSTest;
import com.opensymphony.xwork2.ActionContext;

@RunWith(MockitoJUnitRunner.class)
public class TablePointsPoolsActionTest extends AbstractActionTest {

	@Mock
	private ITableBS tableBS;

	@InjectMocks
	private TablePointsPoolsAction tablePointsPoolsAction;

	
	@Test
	public void testExecute_Fail() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer id = 2;
		
		// Execute
		tablePointsPoolsAction.setId(id);
		String result = tablePointsPoolsAction.execute();
		
		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(tablePointsPoolsAction.getActionErrors().contains("Impossible de charger cette table"));
	}


	@Test
	public void testExecute_Success() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer id = 2;
		Collection<String> pointPoolList = Arrays.asList("Attribut", "Compétences", "Expérience");
		PersonnageWorkBO personnageWork1 = PersonnageWorkBSTest.buildPersonnageWorkMock(1, "B", "robin", "batman");
		PersonnageWorkBO personnageWork2 = PersonnageWorkBSTest.buildPersonnageWorkMock(1, "A", "catwoman", "batman");
		Mockito.when(tableBS.getTableForGM(id, user)).thenReturn(new TableBO());
		Mockito.when(tableBS.getPjList(id)).thenReturn(Arrays.asList(personnageWork1, personnageWork2));
		Mockito.when(tableBS.getPointPoolList(id)).thenReturn(pointPoolList);
		
		// Execute
		tablePointsPoolsAction.setId(id);
		String result = tablePointsPoolsAction.execute();
		
		// Check
		Assert.assertEquals("input", result);
		Iterator<PersonnageWorkBO> pjIt = tablePointsPoolsAction.getPjList().iterator();
		Assert.assertEquals(personnageWork2, pjIt.next());
		Assert.assertEquals(personnageWork1, pjIt.next());
		Assert.assertFalse(pjIt.hasNext());
		Assert.assertEquals(pointPoolList, tablePointsPoolsAction.getPointPoolList());
	}


	@Test
	public void testAddPoints_Fail_NumberFormat() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer id = 2;
		String pointPoolModification = "badNumber";
		Mockito.when(tableBS.getTableForGM(id, user)).thenReturn(new TableBO());
		Mockito.when(tableBS.getPjList(id)).thenReturn(new ArrayList<PersonnageWorkBO>());
		
		// Execute
		tablePointsPoolsAction.setId(id);
		tablePointsPoolsAction.setPointPoolModification(pointPoolModification);
		String result = tablePointsPoolsAction.addPoints();

		// Check
		Assert.assertEquals("input", result);
		Mockito.verify(tableBS, Mockito.never()).addPointsToPj(Mockito.anyInt(), Mockito.any(UserBO.class), Mockito.anyString(), Mockito.anyInt());
		Assert.assertTrue(tablePointsPoolsAction.getActionErrors().contains("Nombre invalide: \"badNumber\""));
	}
	
	
	@Test
	public void testAddPoints_Fail_AddPointsToPj() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer id = 2;
		String pointPoolModification = "10";
		String pointPoolName = "Expérience";
		Mockito.when(tableBS.getTableForGM(id, user)).thenReturn(new TableBO());
		Mockito.when(tableBS.getPjList(id)).thenReturn(new ArrayList<PersonnageWorkBO>());
		Mockito.when(tableBS.addPointsToPj(id, user, pointPoolName, 10)).thenReturn("New error message");
		
		// Execute
		tablePointsPoolsAction.setId(id);
		tablePointsPoolsAction.setPointPoolModification(pointPoolModification);
		tablePointsPoolsAction.setPointPoolName(pointPoolName);
		String result = tablePointsPoolsAction.addPoints();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertTrue(tablePointsPoolsAction.getActionErrors().contains("New error message"));
	}
	
	
	@Test
	public void testAddPoints_Success() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer id = 2;
		String pointPoolModification = " 10 ";
		Mockito.when(tableBS.getPjList(id)).thenReturn(null);
		
		// Execute
		tablePointsPoolsAction.setId(id);
		tablePointsPoolsAction.setPointPoolModification(pointPoolModification);
		String result = tablePointsPoolsAction.addPoints();

		// Check
		Assert.assertEquals("success", result);
		Mockito.verify(tableBS, Mockito.never()).addPointsToPj(id, user, pointPoolModification, 10);
	}


	@Test
	public void testFindMinMaxPjPoints() throws Exception {
		// Prepare
		PersonnageWorkBO personnageWork1 = PersonnageWorkBSTest.buildPersonnageWorkMock(1, "A", "robin", "batman");
		addPointPool(personnageWork1, "Attribut", 10);
		addPointPool(personnageWork1, "Compétences", 50);
		addPointPool(personnageWork1, "Expérience", 0);
		PersonnageWorkBO personnageWork2 = PersonnageWorkBSTest.buildPersonnageWorkMock(2, "B", "catwoman", "batman");
		addPointPool(personnageWork2, "Attribut", 11);
		addPointPool(personnageWork2, "Compétences", 50);
		addPointPool(personnageWork2, "Expérience", 0);
		addPointPool(personnageWork2, "Spécialité", 5);
		PersonnageWorkBO personnageWork3 = PersonnageWorkBSTest.buildPersonnageWorkMock(3, "C", "alfred", "batman");
		addPointPool(personnageWork3, "Attribut", 10);
		addPointPool(personnageWork3, "Compétences", 50);
		addPointPool(personnageWork3, "Expérience", 20);
		SortedSet<PersonnageWorkBO> pjList = new TreeSet<PersonnageWorkBO>(new PersonnageWorkComparator());
		pjList.addAll(Arrays.asList(personnageWork1, personnageWork2, personnageWork3));
		
		// Execute
		tablePointsPoolsAction.setPointPoolList(Arrays.asList("Attribut", "Compétences", "Expérience", "Spécialité"));
		tablePointsPoolsAction.setPjList(pjList);
		Map<String, int[]> result = tablePointsPoolsAction.findMinMaxPjPoints();

		// Check
		Assert.assertEquals(4, result.size());
		Assert.assertArrayEquals(new int[]{10,11}, result.get("Attribut"));
		Assert.assertArrayEquals(new int[]{50,50}, result.get("Compétences"));
		Assert.assertArrayEquals(new int[]{0,20}, result.get("Expérience"));
		Assert.assertArrayEquals(new int[]{5,5}, result.get("Spécialité"));
	}
	
	
	private void addPointPool(PersonnageWorkBO personnageWork, String name, int total){
		if(personnageWork.getPersonnage()==null){
			Personnage personnage = Mockito.mock(Personnage.class);
			personnageWork.setPersonnageData(new PersonnageDataBO());
			personnageWork.getPersonnageData().setPersonnage(personnage);
			Mockito.when(personnage.getPointPools()).thenReturn(new HashMap<String, PoolPoint>());
		}
		PoolPoint pointPool = new PoolPoint(name, total);
		personnageWork.getPersonnage().getPointPools().put(name, pointPool);
	}
	
	private class PersonnageWorkComparator implements Comparator<PersonnageWorkBO> {
		@Override
		public int compare(PersonnageWorkBO arg0, PersonnageWorkBO arg1) {
			return arg0.getName().compareTo(arg1.getName());
		}
	}
}
