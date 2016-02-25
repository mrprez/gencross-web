package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.EditTableAction;
import com.mrprez.gencross.web.bs.face.IAdminBS;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.web.bs.face.ITableBS;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class EditTableActionTest extends AbstractActionTest {

	@Mock
	private IPersonnageBS personnageBS;

	@Mock
	private ITableBS tableBS;

	@Mock
	private IAdminBS adminBS;

	@InjectMocks
	private EditTableAction editTableAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare
		editTableAction.setLoadedMessageNumber(1);
		editTableAction.setPointPoolName("string_2");
		editTableAction.setPersonnageId(3);
		editTableAction.setMessage("string_4");
		editTableAction.setAddMessage("string_5");
		editTableAction.setPointPoolModification(6);
		editTableAction.setId(7);
		editTableAction.setPersonnageName("string_8");
		editTableAction.setMessageId(9);
		editTableAction.setSendMessage("string_10");

		// Execute
		String result = editTableAction.execute();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals("failTest", editTableAction.getPjList());
		Assert.assertEquals("failTest", editTableAction.getMessageId());
		Assert.assertEquals("failTest", editTableAction.getMinPjPoints());
		Assert.assertEquals("failTest", editTableAction.getSendMessage());
		Assert.assertEquals("failTest", editTableAction.getMessage());
		Assert.assertEquals("failTest", editTableAction.getPointPoolName());
		Assert.assertEquals("failTest", editTableAction.getAddablePersonnage());
		Assert.assertEquals("failTest", editTableAction.getTable());
		Assert.assertEquals("failTest", editTableAction.getPersonnageId());
		Assert.assertEquals("failTest", editTableAction.getId());
		Assert.assertEquals("failTest", editTableAction.getAddMessage());
		Assert.assertEquals("failTest", editTableAction.getLoadedMessageNumber());
		Assert.assertEquals("failTest", editTableAction.getMaxPnjPoints());
		Assert.assertEquals("failTest", editTableAction.getMinPnjPoints());
		Assert.assertEquals("failTest", editTableAction.getMaxPjPoints());
		Assert.assertEquals("failTest", editTableAction.getPointPoolModification());
		Assert.assertEquals("failTest", editTableAction.getPoolPointList());
		Assert.assertEquals("failTest", editTableAction.getPnjList());
		Assert.assertEquals("failTest", editTableAction.getPersonnageName());
	}


	@Test
	public void testTransformInPNJ() throws Exception {
		// Prepare
		editTableAction.setLoadedMessageNumber(1);
		editTableAction.setPointPoolName("string_2");
		editTableAction.setPersonnageId(3);
		editTableAction.setMessage("string_4");
		editTableAction.setAddMessage("string_5");
		editTableAction.setPointPoolModification(6);
		editTableAction.setId(7);
		editTableAction.setPersonnageName("string_8");
		editTableAction.setMessageId(9);
		editTableAction.setSendMessage("string_10");

		// Execute
		String result = editTableAction.transformInPNJ();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals("failTest", editTableAction.getPjList());
		Assert.assertEquals("failTest", editTableAction.getMessageId());
		Assert.assertEquals("failTest", editTableAction.getMinPjPoints());
		Assert.assertEquals("failTest", editTableAction.getSendMessage());
		Assert.assertEquals("failTest", editTableAction.getMessage());
		Assert.assertEquals("failTest", editTableAction.getPointPoolName());
		Assert.assertEquals("failTest", editTableAction.getAddablePersonnage());
		Assert.assertEquals("failTest", editTableAction.getTable());
		Assert.assertEquals("failTest", editTableAction.getPersonnageId());
		Assert.assertEquals("failTest", editTableAction.getId());
		Assert.assertEquals("failTest", editTableAction.getAddMessage());
		Assert.assertEquals("failTest", editTableAction.getLoadedMessageNumber());
		Assert.assertEquals("failTest", editTableAction.getMaxPnjPoints());
		Assert.assertEquals("failTest", editTableAction.getMinPnjPoints());
		Assert.assertEquals("failTest", editTableAction.getMaxPjPoints());
		Assert.assertEquals("failTest", editTableAction.getPointPoolModification());
		Assert.assertEquals("failTest", editTableAction.getPoolPointList());
		Assert.assertEquals("failTest", editTableAction.getPnjList());
		Assert.assertEquals("failTest", editTableAction.getPersonnageName());
	}


	@Test
	public void testBindPersonnage() throws Exception {
		// Prepare
		editTableAction.setLoadedMessageNumber(1);
		editTableAction.setPointPoolName("string_2");
		editTableAction.setPersonnageId(3);
		editTableAction.setMessage("string_4");
		editTableAction.setAddMessage("string_5");
		editTableAction.setPointPoolModification(6);
		editTableAction.setId(7);
		editTableAction.setPersonnageName("string_8");
		editTableAction.setMessageId(9);
		editTableAction.setSendMessage("string_10");

		// Execute
		String result = editTableAction.bindPersonnage();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals("failTest", editTableAction.getPjList());
		Assert.assertEquals("failTest", editTableAction.getMessageId());
		Assert.assertEquals("failTest", editTableAction.getMinPjPoints());
		Assert.assertEquals("failTest", editTableAction.getSendMessage());
		Assert.assertEquals("failTest", editTableAction.getMessage());
		Assert.assertEquals("failTest", editTableAction.getPointPoolName());
		Assert.assertEquals("failTest", editTableAction.getAddablePersonnage());
		Assert.assertEquals("failTest", editTableAction.getTable());
		Assert.assertEquals("failTest", editTableAction.getPersonnageId());
		Assert.assertEquals("failTest", editTableAction.getId());
		Assert.assertEquals("failTest", editTableAction.getAddMessage());
		Assert.assertEquals("failTest", editTableAction.getLoadedMessageNumber());
		Assert.assertEquals("failTest", editTableAction.getMaxPnjPoints());
		Assert.assertEquals("failTest", editTableAction.getMinPnjPoints());
		Assert.assertEquals("failTest", editTableAction.getMaxPjPoints());
		Assert.assertEquals("failTest", editTableAction.getPointPoolModification());
		Assert.assertEquals("failTest", editTableAction.getPoolPointList());
		Assert.assertEquals("failTest", editTableAction.getPnjList());
		Assert.assertEquals("failTest", editTableAction.getPersonnageName());
	}


	@Test
	public void testUnbindPersonnage() throws Exception {
		// Prepare
		editTableAction.setLoadedMessageNumber(1);
		editTableAction.setPointPoolName("string_2");
		editTableAction.setPersonnageId(3);
		editTableAction.setMessage("string_4");
		editTableAction.setAddMessage("string_5");
		editTableAction.setPointPoolModification(6);
		editTableAction.setId(7);
		editTableAction.setPersonnageName("string_8");
		editTableAction.setMessageId(9);
		editTableAction.setSendMessage("string_10");

		// Execute
		String result = editTableAction.unbindPersonnage();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals("failTest", editTableAction.getPjList());
		Assert.assertEquals("failTest", editTableAction.getMessageId());
		Assert.assertEquals("failTest", editTableAction.getMinPjPoints());
		Assert.assertEquals("failTest", editTableAction.getSendMessage());
		Assert.assertEquals("failTest", editTableAction.getMessage());
		Assert.assertEquals("failTest", editTableAction.getPointPoolName());
		Assert.assertEquals("failTest", editTableAction.getAddablePersonnage());
		Assert.assertEquals("failTest", editTableAction.getTable());
		Assert.assertEquals("failTest", editTableAction.getPersonnageId());
		Assert.assertEquals("failTest", editTableAction.getId());
		Assert.assertEquals("failTest", editTableAction.getAddMessage());
		Assert.assertEquals("failTest", editTableAction.getLoadedMessageNumber());
		Assert.assertEquals("failTest", editTableAction.getMaxPnjPoints());
		Assert.assertEquals("failTest", editTableAction.getMinPnjPoints());
		Assert.assertEquals("failTest", editTableAction.getMaxPjPoints());
		Assert.assertEquals("failTest", editTableAction.getPointPoolModification());
		Assert.assertEquals("failTest", editTableAction.getPoolPointList());
		Assert.assertEquals("failTest", editTableAction.getPnjList());
		Assert.assertEquals("failTest", editTableAction.getPersonnageName());
	}


	@Test
	public void testRefreshMessages() throws Exception {
		// Prepare
		editTableAction.setLoadedMessageNumber(1);
		editTableAction.setPointPoolName("string_2");
		editTableAction.setPersonnageId(3);
		editTableAction.setMessage("string_4");
		editTableAction.setAddMessage("string_5");
		editTableAction.setPointPoolModification(6);
		editTableAction.setId(7);
		editTableAction.setPersonnageName("string_8");
		editTableAction.setMessageId(9);
		editTableAction.setSendMessage("string_10");

		// Execute
		String result = editTableAction.refreshMessages();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals("failTest", editTableAction.getPjList());
		Assert.assertEquals("failTest", editTableAction.getMessageId());
		Assert.assertEquals("failTest", editTableAction.getMinPjPoints());
		Assert.assertEquals("failTest", editTableAction.getSendMessage());
		Assert.assertEquals("failTest", editTableAction.getMessage());
		Assert.assertEquals("failTest", editTableAction.getPointPoolName());
		Assert.assertEquals("failTest", editTableAction.getAddablePersonnage());
		Assert.assertEquals("failTest", editTableAction.getTable());
		Assert.assertEquals("failTest", editTableAction.getPersonnageId());
		Assert.assertEquals("failTest", editTableAction.getId());
		Assert.assertEquals("failTest", editTableAction.getAddMessage());
		Assert.assertEquals("failTest", editTableAction.getLoadedMessageNumber());
		Assert.assertEquals("failTest", editTableAction.getMaxPnjPoints());
		Assert.assertEquals("failTest", editTableAction.getMinPnjPoints());
		Assert.assertEquals("failTest", editTableAction.getMaxPjPoints());
		Assert.assertEquals("failTest", editTableAction.getPointPoolModification());
		Assert.assertEquals("failTest", editTableAction.getPoolPointList());
		Assert.assertEquals("failTest", editTableAction.getPnjList());
		Assert.assertEquals("failTest", editTableAction.getPersonnageName());
	}


	@Test
	public void testAddPersonnage() throws Exception {
		// Prepare
		editTableAction.setLoadedMessageNumber(1);
		editTableAction.setPointPoolName("string_2");
		editTableAction.setPersonnageId(3);
		editTableAction.setMessage("string_4");
		editTableAction.setAddMessage("string_5");
		editTableAction.setPointPoolModification(6);
		editTableAction.setId(7);
		editTableAction.setPersonnageName("string_8");
		editTableAction.setMessageId(9);
		editTableAction.setSendMessage("string_10");

		// Execute
		String result = editTableAction.addPersonnage();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals("failTest", editTableAction.getPjList());
		Assert.assertEquals("failTest", editTableAction.getMessageId());
		Assert.assertEquals("failTest", editTableAction.getMinPjPoints());
		Assert.assertEquals("failTest", editTableAction.getSendMessage());
		Assert.assertEquals("failTest", editTableAction.getMessage());
		Assert.assertEquals("failTest", editTableAction.getPointPoolName());
		Assert.assertEquals("failTest", editTableAction.getAddablePersonnage());
		Assert.assertEquals("failTest", editTableAction.getTable());
		Assert.assertEquals("failTest", editTableAction.getPersonnageId());
		Assert.assertEquals("failTest", editTableAction.getId());
		Assert.assertEquals("failTest", editTableAction.getAddMessage());
		Assert.assertEquals("failTest", editTableAction.getLoadedMessageNumber());
		Assert.assertEquals("failTest", editTableAction.getMaxPnjPoints());
		Assert.assertEquals("failTest", editTableAction.getMinPnjPoints());
		Assert.assertEquals("failTest", editTableAction.getMaxPjPoints());
		Assert.assertEquals("failTest", editTableAction.getPointPoolModification());
		Assert.assertEquals("failTest", editTableAction.getPoolPointList());
		Assert.assertEquals("failTest", editTableAction.getPnjList());
		Assert.assertEquals("failTest", editTableAction.getPersonnageName());
	}


	@Test
	public void testNewMessage() throws Exception {
		// Prepare
		editTableAction.setLoadedMessageNumber(1);
		editTableAction.setPointPoolName("string_2");
		editTableAction.setPersonnageId(3);
		editTableAction.setMessage("string_4");
		editTableAction.setAddMessage("string_5");
		editTableAction.setPointPoolModification(6);
		editTableAction.setId(7);
		editTableAction.setPersonnageName("string_8");
		editTableAction.setMessageId(9);
		editTableAction.setSendMessage("string_10");

		// Execute
		String result = editTableAction.newMessage();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals("failTest", editTableAction.getPjList());
		Assert.assertEquals("failTest", editTableAction.getMessageId());
		Assert.assertEquals("failTest", editTableAction.getMinPjPoints());
		Assert.assertEquals("failTest", editTableAction.getSendMessage());
		Assert.assertEquals("failTest", editTableAction.getMessage());
		Assert.assertEquals("failTest", editTableAction.getPointPoolName());
		Assert.assertEquals("failTest", editTableAction.getAddablePersonnage());
		Assert.assertEquals("failTest", editTableAction.getTable());
		Assert.assertEquals("failTest", editTableAction.getPersonnageId());
		Assert.assertEquals("failTest", editTableAction.getId());
		Assert.assertEquals("failTest", editTableAction.getAddMessage());
		Assert.assertEquals("failTest", editTableAction.getLoadedMessageNumber());
		Assert.assertEquals("failTest", editTableAction.getMaxPnjPoints());
		Assert.assertEquals("failTest", editTableAction.getMinPnjPoints());
		Assert.assertEquals("failTest", editTableAction.getMaxPjPoints());
		Assert.assertEquals("failTest", editTableAction.getPointPoolModification());
		Assert.assertEquals("failTest", editTableAction.getPoolPointList());
		Assert.assertEquals("failTest", editTableAction.getPnjList());
		Assert.assertEquals("failTest", editTableAction.getPersonnageName());
	}


	@Test
	public void testRemoveMessage() throws Exception {
		// Prepare
		editTableAction.setLoadedMessageNumber(1);
		editTableAction.setPointPoolName("string_2");
		editTableAction.setPersonnageId(3);
		editTableAction.setMessage("string_4");
		editTableAction.setAddMessage("string_5");
		editTableAction.setPointPoolModification(6);
		editTableAction.setId(7);
		editTableAction.setPersonnageName("string_8");
		editTableAction.setMessageId(9);
		editTableAction.setSendMessage("string_10");

		// Execute
		String result = editTableAction.removeMessage();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals("failTest", editTableAction.getPjList());
		Assert.assertEquals("failTest", editTableAction.getMessageId());
		Assert.assertEquals("failTest", editTableAction.getMinPjPoints());
		Assert.assertEquals("failTest", editTableAction.getSendMessage());
		Assert.assertEquals("failTest", editTableAction.getMessage());
		Assert.assertEquals("failTest", editTableAction.getPointPoolName());
		Assert.assertEquals("failTest", editTableAction.getAddablePersonnage());
		Assert.assertEquals("failTest", editTableAction.getTable());
		Assert.assertEquals("failTest", editTableAction.getPersonnageId());
		Assert.assertEquals("failTest", editTableAction.getId());
		Assert.assertEquals("failTest", editTableAction.getAddMessage());
		Assert.assertEquals("failTest", editTableAction.getLoadedMessageNumber());
		Assert.assertEquals("failTest", editTableAction.getMaxPnjPoints());
		Assert.assertEquals("failTest", editTableAction.getMinPnjPoints());
		Assert.assertEquals("failTest", editTableAction.getMaxPjPoints());
		Assert.assertEquals("failTest", editTableAction.getPointPoolModification());
		Assert.assertEquals("failTest", editTableAction.getPoolPointList());
		Assert.assertEquals("failTest", editTableAction.getPnjList());
		Assert.assertEquals("failTest", editTableAction.getPersonnageName());
	}
}
