package com.mrprez.gencross.web.bs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.PlannedGameBO;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.util.BusinessException;
import com.mrprez.gencross.web.dao.face.IMailResource;
import com.mrprez.gencross.web.dao.face.ITableDAO;

public class PlanGameBSTest {
	
	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	
	
	@Test
	public void testGetPlannedGames() throws Exception{
		PlanGameBS planGameBS = buildMockedPlannedGameBS();
		int tableId = 1;
		List<PlannedGameBO> plannedGameList = new ArrayList<PlannedGameBO>();
		Mockito.when(planGameBS.getTableDao().loadTablePlannedGames(tableId)).thenReturn(plannedGameList);
		
		Collection<PlannedGameBO> result = planGameBS.getPlannedGames(tableId);
		
		Assert.assertEquals(plannedGameList, result);
		Mockito.verify(planGameBS.getTableDao()).loadTablePlannedGames(tableId);
	}
	
	
	@Test
	public void testCreateGame_Fail_GameMaster() throws Exception{
		PlanGameBS planGameBS = buildMockedPlannedGameBS();
		String name = "Gotham";
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		UserBO user = AuthentificationBSTest.buildUser("robin");
		String type = "DC-Comics";
		TableBO table = TableBSTest.buildTable(gm, name, type);
		Mockito.when(planGameBS.getTableDao().loadTable(table.getId())).thenReturn(table);
		
		BusinessException businessException = null;
		try{
			planGameBS.createGame(table.getId(), "Black night", dateFormat.parse("01/02/2015 18:00:00"), dateFormat.parse("02/02/2015 00:00:00"), user);
		}catch(BusinessException e){
			businessException = e;
		}
		
		Assert.assertNotNull(businessException);
		Assert.assertEquals("L'utilisateur n'est pas le MJ de la table", businessException.getMessage());
		Mockito.verify(planGameBS.getTableDao(), Mockito.never()).savePlannedGame(Mockito.any(PlannedGameBO.class));
		Mockito.verify(planGameBS.getMailResource(), Mockito.never()).send(Mockito.anyCollectionOf(String.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(byte[].class));
	}
	
	
	@Test
	public void testCreateGame_Success() throws Exception {
		PlanGameBS planGameBS = buildMockedPlannedGameBS();
		String name = "Gotham";
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		UserBO user = AuthentificationBSTest.buildUser("batman");
		String type = "DC-Comics";
		TableBO table = TableBSTest.buildTable(gm, name, type);
		PersonnageWorkBO personnageWork1 = PersonnageWorkBSTest.buildPersonnageWork();
		personnageWork1.setGameMaster(gm);
		personnageWork1.setPlayer(AuthentificationBSTest.buildUser("robin"));
		table.getPersonnages().add(personnageWork1);
		PersonnageWorkBO personnageWork2 = PersonnageWorkBSTest.buildPersonnageWork();
		personnageWork2.setGameMaster(gm);
		personnageWork2.setPlayer(AuthentificationBSTest.buildUser("catwoman"));
		table.getPersonnages().add(personnageWork2);
		
		PlannedGameBO result = planGameBS.createGame(table.getId(), "Black night", dateFormat.parse("01/02/2015 18:00:00"), dateFormat.parse("02/02/2015 00:00:00"), user);
		
		Assert.assertEquals("Black night", result.getTitle());
		Assert.assertEquals(dateFormat.parse("02/02/2015 00:00:00"), result.getEndTime());
		Assert.assertEquals(dateFormat.parse("01/02/2015 18:00:00"), result.getStartTime());
		Assert.assertEquals(table, result.getTable());
		Assert.assertNotNull(result.getId());
		Mockito.verify(planGameBS.getTableDao()).savePlannedGame(result);
		Set<String> toAddresses = new HashSet<String>(Arrays.asList("robin@gmail.com", "catwoman@gmail.com"));
		Mockito.verify(planGameBS.getMailResource()).send(toAddresses, table.getGameMaster().getMail(), table.getName()+": nouvelle partie", "Une nouvelle partie a été planifiée", table.getName()+".ics", Mockito.any(byte[].class));
	}
	
	
	private PlanGameBS buildMockedPlannedGameBS(){
		PlanGameBS planGameBS = new PlanGameBS();
		planGameBS.setTableDao(Mockito.mock(ITableDAO.class));
		planGameBS.setMailResource(Mockito.mock(IMailResource.class));
		return planGameBS;
	}

}
