package com.mrprez.gencross.web.test.bs;

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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.mrprez.gencross.web.bo.PlannedGameBO;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.PlanGameBS;
import com.mrprez.gencross.web.bs.util.BusinessException;
import com.mrprez.gencross.web.dao.TableDAO;
import com.mrprez.gencross.web.dao.face.IMailResource;
import com.mrprez.gencross.web.dao.face.ITableDAO;

@RunWith(MockitoJUnitRunner.class)
public class PlanGameBSTest {
	
	@InjectMocks
	private PlanGameBS planGameBS;
	
	@Mock
	private ITableDAO tableDao;
	
	@Mock
	private IMailResource mailResource;
	
	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	
	
	@Test
	public void testGetPlannedGames() throws Exception{
		// Prepare
		int tableId = 1;
		List<PlannedGameBO> plannedGameList = new ArrayList<PlannedGameBO>();
		Mockito.when(tableDao.loadTablePlannedGames(tableId)).thenReturn(plannedGameList);
		
		// Execute
		Collection<PlannedGameBO> result = planGameBS.getPlannedGames(tableId);
		
		// Check
		Assert.assertEquals(plannedGameList, result);
		Mockito.verify(tableDao).loadTablePlannedGames(tableId);
	}
	
	
	@Test
	public void testCreateGame_Fail_GameMaster() throws Exception{
		// Prepare
		String name = "Gotham";
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		UserBO user = AuthentificationBSTest.buildUser("robin");
		String type = "DC-Comics";
		TableBO table = TableBSTest.buildTable(gm, name, type);
		Mockito.when(tableDao.loadTable(table.getId())).thenReturn(table);
		
		// Execute
		BusinessException businessException = null;
		try{
			planGameBS.createGame(table.getId(), "Black night", dateFormat.parse("01/02/2015 18:00:00"), dateFormat.parse("02/02/2015 00:00:00"), user);
		}catch(BusinessException e){
			businessException = e;
		}
		
		// Check
		Assert.assertNotNull(businessException);
		Assert.assertEquals("L'utilisateur n'est pas le MJ de la table", businessException.getMessage());
		Mockito.verify(tableDao, Mockito.never()).savePlannedGame(Mockito.any(PlannedGameBO.class));
		Mockito.verify(mailResource, Mockito.never()).send(Mockito.anyCollectionOf(String.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(byte[].class));
	}
	
	
	@Test
	public void testCreateGame_Success() throws Exception {
		// Prepare
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		UserBO user = AuthentificationBSTest.buildUser("batman");
		TableBO table = TableBSTest.buildFullTable(gm, "Gotham", "DC-Comics");
		
		Mockito.when(tableDao.loadTable(table.getId())).thenReturn(table);
		
		Answer<TableDAO> answer = new Answer<TableDAO>() {
			@Override
			public TableDAO answer(InvocationOnMock invocation) throws Throwable {
				PlannedGameBO plannedGame = (PlannedGameBO) invocation.getArguments()[0];
				plannedGame.setId(25);
				return null;
			}
		};
		Mockito.doAnswer(answer).when(tableDao).savePlannedGame(Mockito.any(PlannedGameBO.class));
		
		// Execute
		PlannedGameBO result = planGameBS.createGame(table.getId(), "Black night", dateFormat.parse("01/02/2015 18:00:00"), dateFormat.parse("02/02/2015 00:00:00"), user);
		
		// Check
		Assert.assertEquals("Black night", result.getTitle());
		Assert.assertEquals(dateFormat.parse("02/02/2015 00:00:00"), result.getEndTime());
		Assert.assertEquals(dateFormat.parse("01/02/2015 18:00:00"), result.getStartTime());
		Assert.assertEquals(table, result.getTable());
		Assert.assertNotNull(result.getId());
		Mockito.verify(tableDao).savePlannedGame(result);
		Set<String> toAddresses = new HashSet<String>(Arrays.asList("robin@gmail.com", "catwoman@gmail.com", "batman@gmail.com"));
		Mockito.verify(mailResource).send(Mockito.eq(toAddresses), Mockito.eq(table.getGameMaster().getMail()), Mockito.eq(table.getName()+": nouvelle partie"), Mockito.eq("Une nouvelle partie a été planifiée"), Mockito.eq(table.getName()+".ics"), Mockito.any(byte[].class));
	}
	
	
	@Test
	public void testUpdateGame_Success() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		TableBO table = TableBSTest.buildFullTable(user, "Gotham", "DC-Comics");
		PlannedGameBO plannedGame = new PlannedGameBO();
		plannedGame.setId(25);
		plannedGame.setTitle("Black knight");
		plannedGame.setStartTime(dateFormat.parse("01/02/2015 18:00:00"));
		plannedGame.setEndTime(dateFormat.parse("02/02/2015 00:00:00"));
		plannedGame.setTable(table);
		Mockito.when(tableDao.loadPlannedGame(25)).thenReturn(plannedGame);
		
		// Execute
		PlannedGameBO result = planGameBS.updateGame(25, "Arkham Asylium", dateFormat.parse("04/02/2015 19:00:00"), dateFormat.parse("05/02/2015 18:00:00"), user);
		
		// Check
		Assert.assertEquals("Arkham Asylium", result.getTitle());
		Assert.assertEquals(dateFormat.parse("04/02/2015 19:00:00"), result.getStartTime());
		Assert.assertEquals(dateFormat.parse("05/02/2015 18:00:00"), result.getEndTime());
		Assert.assertEquals(25, (int)result.getId());
		Set<String> toAddresses = new HashSet<String>(Arrays.asList("robin@gmail.com", "catwoman@gmail.com", "batman@gmail.com"));
		Mockito.verify(mailResource).send(Mockito.eq(toAddresses), Mockito.eq(table.getGameMaster().getMail()), Mockito.eq(table.getName()+": nouvelle partie"), Mockito.eq("Une nouvelle partie a été planifiée"), Mockito.eq(table.getName()+".ics"), Mockito.any(byte[].class));
	}
	
	
	@Test
	public void testUpdateGame_Fail() throws Exception{
		// Prepare
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		TableBO table = TableBSTest.buildFullTable(gm, "Gotham", "DC-Comics");
		PlannedGameBO plannedGame = new PlannedGameBO();
		plannedGame.setId(25);
		plannedGame.setTitle("Black knight");
		plannedGame.setStartTime(dateFormat.parse("01/02/2015 18:00:00"));
		plannedGame.setEndTime(dateFormat.parse("02/02/2015 00:00:00"));
		plannedGame.setTable(table);
		Mockito.when(tableDao.loadPlannedGame(25)).thenReturn(plannedGame);
		
		// Execute
		BusinessException businessException = null;
		try{
			planGameBS.updateGame(25, "Arkham Asylium", dateFormat.parse("04/02/2015 19:00:00"), dateFormat.parse("05/02/2015 18:00:00"), AuthentificationBSTest.buildUser("robin"));
		}catch(BusinessException be){
			businessException = be;
		}
		
		// Check
		Assert.assertNotNull(businessException);
		Assert.assertEquals("L'utilisateur n'est pas le MJ de la table", businessException.getMessage());
	}
	
	
	@Test
	public void testDeleteGame_Fail() throws Exception{
		// Prepare
		UserBO gm = AuthentificationBSTest.buildUser("batman");
		TableBO table = TableBSTest.buildFullTable(gm, "Gotham", "DC-Comics");
		PlannedGameBO plannedGame = new PlannedGameBO();
		plannedGame.setId(25);
		plannedGame.setTitle("Black knight");
		plannedGame.setStartTime(dateFormat.parse("01/02/2015 18:00:00"));
		plannedGame.setEndTime(dateFormat.parse("02/02/2015 00:00:00"));
		plannedGame.setTable(table);
		Mockito.when(tableDao.loadPlannedGame(25)).thenReturn(plannedGame);
		
		// Execute
		BusinessException businessException = null;
		try{
			planGameBS.deleteGame(25, AuthentificationBSTest.buildUser("robin"));
		}catch(BusinessException be){
			businessException = be;
		}
		
		// Check
		Assert.assertNotNull(businessException);
		Assert.assertEquals("L'utilisateur n'est pas le MJ de la table", businessException.getMessage());
	}
	
	
	@Test
	public void testDeleteGame_Success() throws Exception{
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		TableBO table = TableBSTest.buildFullTable(user, "Gotham", "DC-Comics");
		PlannedGameBO plannedGame = new PlannedGameBO();
		plannedGame.setId(25);
		plannedGame.setTitle("Black knight");
		plannedGame.setStartTime(dateFormat.parse("01/02/2015 18:00:00"));
		plannedGame.setEndTime(dateFormat.parse("02/02/2015 00:00:00"));
		plannedGame.setTable(table);
		Mockito.when(tableDao.loadPlannedGame(25)).thenReturn(plannedGame);
		
		// Execute
		planGameBS.deleteGame(25, user);
		
		// Check
		Set<String> toAddresses = new HashSet<String>(Arrays.asList("robin@gmail.com", "catwoman@gmail.com", "batman@gmail.com"));
		Mockito.verify(mailResource).send(Mockito.eq(toAddresses), Mockito.eq(table.getGameMaster().getMail()), Mockito.eq(table.getName()+": nouvelle partie"), Mockito.eq("Une nouvelle partie a été planifiée"), Mockito.eq(table.getName()+".ics"), Mockito.any(byte[].class));
		Mockito.verify(tableDao).deletePlannedGame(plannedGame);
	}
	

}
