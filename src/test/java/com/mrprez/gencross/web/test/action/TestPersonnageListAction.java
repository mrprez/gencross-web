package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.PersonnageListAction;
import com.mrprez.gencross.web.bs.face.IGcrFileBS;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;

@RunWith(MockitoJUnitRunner.class)
public class TestPersonnageListAction {

	@Mock
	private IGcrFileBS gcrFileBS;

	@Mock
	private IPersonnageBS personnageBS;

	@InjectMocks
	private PersonnageListAction personnageListAction;



	@Test
	public void testSort() throws Exception {
		// Prepare
		personnageListAction.setPersonnageId(1);
		personnageListAction.setPlayerPersonnageSort("string_2");
		personnageListAction.setPassword("string_3");
		personnageListAction.setGameMasterPersonnageSort("string_4");

		// Execute
		personnageListAction.sort();

		// Check
		Assert.assertEquals("failTest", personnageListAction.getPlayerPersonnageComparator());
		Assert.assertEquals("failTest", personnageListAction.getInputStream());
		Assert.assertEquals("failTest", personnageListAction.getPlayerSortDir());
		Assert.assertEquals("failTest", personnageListAction.getGameMasterSortDir());
		Assert.assertEquals("failTest", personnageListAction.getGMPersonnageComparator());
		Assert.assertEquals("failTest", personnageListAction.getFileSize());
		Assert.assertEquals("failTest", personnageListAction.getPlayerPersonnageList());
		Assert.assertEquals("failTest", personnageListAction.getFileName());
		Assert.assertEquals("failTest", personnageListAction.getGameMasterPersonnageList());
	}


	@Test
	public void testExecute() throws Exception {
		// Prepare
		personnageListAction.setPersonnageId(1);
		personnageListAction.setPlayerPersonnageSort("string_2");
		personnageListAction.setPassword("string_3");
		personnageListAction.setGameMasterPersonnageSort("string_4");

		// Execute
		personnageListAction.execute();

		// Check
		Assert.assertEquals("failTest", personnageListAction.getPlayerPersonnageComparator());
		Assert.assertEquals("failTest", personnageListAction.getInputStream());
		Assert.assertEquals("failTest", personnageListAction.getPlayerSortDir());
		Assert.assertEquals("failTest", personnageListAction.getGameMasterSortDir());
		Assert.assertEquals("failTest", personnageListAction.getGMPersonnageComparator());
		Assert.assertEquals("failTest", personnageListAction.getFileSize());
		Assert.assertEquals("failTest", personnageListAction.getPlayerPersonnageList());
		Assert.assertEquals("failTest", personnageListAction.getFileName());
		Assert.assertEquals("failTest", personnageListAction.getGameMasterPersonnageList());
	}


	@Test
	public void testDeletePersonnage() throws Exception {
		// Prepare
		personnageListAction.setPersonnageId(1);
		personnageListAction.setPlayerPersonnageSort("string_2");
		personnageListAction.setPassword("string_3");
		personnageListAction.setGameMasterPersonnageSort("string_4");

		// Execute
		personnageListAction.deletePersonnage();

		// Check
		Assert.assertEquals("failTest", personnageListAction.getPlayerPersonnageComparator());
		Assert.assertEquals("failTest", personnageListAction.getInputStream());
		Assert.assertEquals("failTest", personnageListAction.getPlayerSortDir());
		Assert.assertEquals("failTest", personnageListAction.getGameMasterSortDir());
		Assert.assertEquals("failTest", personnageListAction.getGMPersonnageComparator());
		Assert.assertEquals("failTest", personnageListAction.getFileSize());
		Assert.assertEquals("failTest", personnageListAction.getPlayerPersonnageList());
		Assert.assertEquals("failTest", personnageListAction.getFileName());
		Assert.assertEquals("failTest", personnageListAction.getGameMasterPersonnageList());
	}


	@Test
	public void testDownloadAsPlayer() throws Exception {
		// Prepare
		personnageListAction.setPersonnageId(1);
		personnageListAction.setPlayerPersonnageSort("string_2");
		personnageListAction.setPassword("string_3");
		personnageListAction.setGameMasterPersonnageSort("string_4");

		// Execute
		personnageListAction.downloadAsPlayer();

		// Check
		Assert.assertEquals("failTest", personnageListAction.getPlayerPersonnageComparator());
		Assert.assertEquals("failTest", personnageListAction.getInputStream());
		Assert.assertEquals("failTest", personnageListAction.getPlayerSortDir());
		Assert.assertEquals("failTest", personnageListAction.getGameMasterSortDir());
		Assert.assertEquals("failTest", personnageListAction.getGMPersonnageComparator());
		Assert.assertEquals("failTest", personnageListAction.getFileSize());
		Assert.assertEquals("failTest", personnageListAction.getPlayerPersonnageList());
		Assert.assertEquals("failTest", personnageListAction.getFileName());
		Assert.assertEquals("failTest", personnageListAction.getGameMasterPersonnageList());
	}


	@Test
	public void testDownloadAsGameMaster() throws Exception {
		// Prepare
		personnageListAction.setPersonnageId(1);
		personnageListAction.setPlayerPersonnageSort("string_2");
		personnageListAction.setPassword("string_3");
		personnageListAction.setGameMasterPersonnageSort("string_4");

		// Execute
		personnageListAction.downloadAsGameMaster();

		// Check
		Assert.assertEquals("failTest", personnageListAction.getPlayerPersonnageComparator());
		Assert.assertEquals("failTest", personnageListAction.getInputStream());
		Assert.assertEquals("failTest", personnageListAction.getPlayerSortDir());
		Assert.assertEquals("failTest", personnageListAction.getGameMasterSortDir());
		Assert.assertEquals("failTest", personnageListAction.getGMPersonnageComparator());
		Assert.assertEquals("failTest", personnageListAction.getFileSize());
		Assert.assertEquals("failTest", personnageListAction.getPlayerPersonnageList());
		Assert.assertEquals("failTest", personnageListAction.getFileName());
		Assert.assertEquals("failTest", personnageListAction.getGameMasterPersonnageList());
	}
}
