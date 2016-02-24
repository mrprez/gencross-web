package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.BackgroundAction;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.web.bs.face.ITableBS;

@RunWith(MockitoJUnitRunner.class)
public class TestBackgroundAction {

	@Mock
	private IPersonnageBS personnageBS;

	@Mock
	private ITableBS tableBS;

	@InjectMocks
	private BackgroundAction backgroundAction;



	@Test
	public void testSave() throws Exception {
		// Prepare
		backgroundAction.setBackground("string_1");
		backgroundAction.setPersonnageId(2);

		// Execute
		backgroundAction.save();

		// Check
		Assert.assertEquals("failTest", backgroundAction.getBackground());
		Assert.assertEquals("failTest", backgroundAction.getPersonnageWork());
		Assert.assertEquals("failTest", backgroundAction.getPersonnageId());
		Assert.assertEquals(10, backgroundAction.getIsGameMaster());
	}


	@Test
	public void testExecute() throws Exception {
		// Prepare
		backgroundAction.setBackground("string_1");
		backgroundAction.setPersonnageId(2);

		// Execute
		backgroundAction.execute();

		// Check
		Assert.assertEquals("failTest", backgroundAction.getBackground());
		Assert.assertEquals("failTest", backgroundAction.getPersonnageWork());
		Assert.assertEquals("failTest", backgroundAction.getPersonnageId());
		Assert.assertEquals(10, backgroundAction.getIsGameMaster());
	}
}
