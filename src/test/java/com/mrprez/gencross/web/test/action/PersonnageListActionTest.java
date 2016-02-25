package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.PersonnageListAction;
import com.mrprez.gencross.web.bs.face.IGcrFileBS;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class PersonnageListActionTest extends AbstractActionTest {

	@Mock
	private IPersonnageBS personnageBS;

	@Mock
	private IGcrFileBS gcrFileBS;

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
		String result = personnageListAction.sort();

		// Check
		Assert.assertEquals("input", result);
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
		String result = personnageListAction.execute();

		// Check
		Assert.assertEquals("input", result);
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
		String result = personnageListAction.deletePersonnage();

		// Check
		Assert.assertEquals("input", result);
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
		String result = personnageListAction.downloadAsPlayer();

		// Check
		Assert.assertEquals("input", result);
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
		String result = personnageListAction.downloadAsGameMaster();

		// Check
		Assert.assertEquals("input", result);
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
