package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.EditPersonnageAction;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.web.bs.face.ITableBS;

@RunWith(MockitoJUnitRunner.class)
public class TestEditPersonnageAction {

	@Mock
	private IPersonnageBS personnageBS;

	@Mock
	private ITableBS tableBS;

	@InjectMocks
	private EditPersonnageAction editPersonnageAction;



	@Test
	public void testSave() throws Exception {
		// Prepare
		editPersonnageAction.setHistoryItemIndex(1);
		editPersonnageAction.setPersonnageId(2);
		editPersonnageAction.setPropertyAbsoluteName("string_3");
		editPersonnageAction.setPointPoolName("string_4");

		// Execute
		editPersonnageAction.save();

		// Check
		Assert.assertEquals("failTest", editPersonnageAction.getPropertyAbsoluteName());
		Assert.assertEquals("failTest", editPersonnageAction.getPersonnageId());
		Assert.assertEquals("failTest", editPersonnageAction.getHelpFileInputStream());
		Assert.assertEquals(10, editPersonnageAction.getIsGameMaster());
		Assert.assertEquals("failTest", editPersonnageAction.getHistoryItemIndex());
		Assert.assertEquals("failTest", editPersonnageAction.getProperty());
		Assert.assertEquals("failTest", editPersonnageAction.getErrorList());
		Assert.assertEquals("failTest", editPersonnageAction.getPointPoolName());
		Assert.assertEquals("failTest", editPersonnageAction.getHistoryItem());
		Assert.assertEquals("failTest", editPersonnageAction.getPointPool());
		Assert.assertEquals("failTest", editPersonnageAction.getPersonnageWork());
	}


	@Test
	public void testExecute() throws Exception {
		// Prepare
		editPersonnageAction.setHistoryItemIndex(1);
		editPersonnageAction.setPersonnageId(2);
		editPersonnageAction.setPropertyAbsoluteName("string_3");
		editPersonnageAction.setPointPoolName("string_4");

		// Execute
		editPersonnageAction.execute();

		// Check
		Assert.assertEquals("failTest", editPersonnageAction.getPropertyAbsoluteName());
		Assert.assertEquals("failTest", editPersonnageAction.getPersonnageId());
		Assert.assertEquals("failTest", editPersonnageAction.getHelpFileInputStream());
		Assert.assertEquals(10, editPersonnageAction.getIsGameMaster());
		Assert.assertEquals("failTest", editPersonnageAction.getHistoryItemIndex());
		Assert.assertEquals("failTest", editPersonnageAction.getProperty());
		Assert.assertEquals("failTest", editPersonnageAction.getErrorList());
		Assert.assertEquals("failTest", editPersonnageAction.getPointPoolName());
		Assert.assertEquals("failTest", editPersonnageAction.getHistoryItem());
		Assert.assertEquals("failTest", editPersonnageAction.getPointPool());
		Assert.assertEquals("failTest", editPersonnageAction.getPersonnageWork());
	}


	@Test
	public void testValidatePersonnage() throws Exception {
		// Prepare
		editPersonnageAction.setHistoryItemIndex(1);
		editPersonnageAction.setPersonnageId(2);
		editPersonnageAction.setPropertyAbsoluteName("string_3");
		editPersonnageAction.setPointPoolName("string_4");

		// Execute
		editPersonnageAction.validatePersonnage();

		// Check
		Assert.assertEquals("failTest", editPersonnageAction.getPropertyAbsoluteName());
		Assert.assertEquals("failTest", editPersonnageAction.getPersonnageId());
		Assert.assertEquals("failTest", editPersonnageAction.getHelpFileInputStream());
		Assert.assertEquals(10, editPersonnageAction.getIsGameMaster());
		Assert.assertEquals("failTest", editPersonnageAction.getHistoryItemIndex());
		Assert.assertEquals("failTest", editPersonnageAction.getProperty());
		Assert.assertEquals("failTest", editPersonnageAction.getErrorList());
		Assert.assertEquals("failTest", editPersonnageAction.getPointPoolName());
		Assert.assertEquals("failTest", editPersonnageAction.getHistoryItem());
		Assert.assertEquals("failTest", editPersonnageAction.getPointPool());
		Assert.assertEquals("failTest", editPersonnageAction.getPersonnageWork());
	}


	@Test
	public void testUnvalidatePersonnage() throws Exception {
		// Prepare
		editPersonnageAction.setHistoryItemIndex(1);
		editPersonnageAction.setPersonnageId(2);
		editPersonnageAction.setPropertyAbsoluteName("string_3");
		editPersonnageAction.setPointPoolName("string_4");

		// Execute
		editPersonnageAction.unvalidatePersonnage();

		// Check
		Assert.assertEquals("failTest", editPersonnageAction.getPropertyAbsoluteName());
		Assert.assertEquals("failTest", editPersonnageAction.getPersonnageId());
		Assert.assertEquals("failTest", editPersonnageAction.getHelpFileInputStream());
		Assert.assertEquals(10, editPersonnageAction.getIsGameMaster());
		Assert.assertEquals("failTest", editPersonnageAction.getHistoryItemIndex());
		Assert.assertEquals("failTest", editPersonnageAction.getProperty());
		Assert.assertEquals("failTest", editPersonnageAction.getErrorList());
		Assert.assertEquals("failTest", editPersonnageAction.getPointPoolName());
		Assert.assertEquals("failTest", editPersonnageAction.getHistoryItem());
		Assert.assertEquals("failTest", editPersonnageAction.getPointPool());
		Assert.assertEquals("failTest", editPersonnageAction.getPersonnageWork());
	}


	@Test
	public void testNextPhase() throws Exception {
		// Prepare
		editPersonnageAction.setHistoryItemIndex(1);
		editPersonnageAction.setPersonnageId(2);
		editPersonnageAction.setPropertyAbsoluteName("string_3");
		editPersonnageAction.setPointPoolName("string_4");

		// Execute
		editPersonnageAction.nextPhase();

		// Check
		Assert.assertEquals("failTest", editPersonnageAction.getPropertyAbsoluteName());
		Assert.assertEquals("failTest", editPersonnageAction.getPersonnageId());
		Assert.assertEquals("failTest", editPersonnageAction.getHelpFileInputStream());
		Assert.assertEquals(10, editPersonnageAction.getIsGameMaster());
		Assert.assertEquals("failTest", editPersonnageAction.getHistoryItemIndex());
		Assert.assertEquals("failTest", editPersonnageAction.getProperty());
		Assert.assertEquals("failTest", editPersonnageAction.getErrorList());
		Assert.assertEquals("failTest", editPersonnageAction.getPointPoolName());
		Assert.assertEquals("failTest", editPersonnageAction.getHistoryItem());
		Assert.assertEquals("failTest", editPersonnageAction.getPointPool());
		Assert.assertEquals("failTest", editPersonnageAction.getPersonnageWork());
	}
}
