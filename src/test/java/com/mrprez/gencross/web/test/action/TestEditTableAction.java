package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.EditTableAction;
import com.mrprez.gencross.web.bs.face.IAdminBS;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.web.bs.face.ITableBS;

@RunWith(MockitoJUnitRunner.class)
public class TestEditTableAction {

	@Mock
	private IAdminBS adminBS;

	@Mock
	private IPersonnageBS personnageBS;

	@Mock
	private ITableBS tableBS;

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
		editTableAction.setSendMessage("string_9");
		editTableAction.setMessageId(10);

		// Execute
		editTableAction.execute();

		// Check
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
		Assert.assertEquals("failTest", editTableAction.getPointPoolModification());
		Assert.assertEquals("failTest", editTableAction.getMaxPjPoints());
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
		editTableAction.setSendMessage("string_9");
		editTableAction.setMessageId(10);

		// Execute
		editTableAction.transformInPNJ();

		// Check
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
		Assert.assertEquals("failTest", editTableAction.getPointPoolModification());
		Assert.assertEquals("failTest", editTableAction.getMaxPjPoints());
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
		editTableAction.setSendMessage("string_9");
		editTableAction.setMessageId(10);

		// Execute
		editTableAction.bindPersonnage();

		// Check
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
		Assert.assertEquals("failTest", editTableAction.getPointPoolModification());
		Assert.assertEquals("failTest", editTableAction.getMaxPjPoints());
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
		editTableAction.setSendMessage("string_9");
		editTableAction.setMessageId(10);

		// Execute
		editTableAction.addPersonnage();

		// Check
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
		Assert.assertEquals("failTest", editTableAction.getPointPoolModification());
		Assert.assertEquals("failTest", editTableAction.getMaxPjPoints());
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
		editTableAction.setSendMessage("string_9");
		editTableAction.setMessageId(10);

		// Execute
		editTableAction.newMessage();

		// Check
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
		Assert.assertEquals("failTest", editTableAction.getPointPoolModification());
		Assert.assertEquals("failTest", editTableAction.getMaxPjPoints());
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
		editTableAction.setSendMessage("string_9");
		editTableAction.setMessageId(10);

		// Execute
		editTableAction.removeMessage();

		// Check
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
		Assert.assertEquals("failTest", editTableAction.getPointPoolModification());
		Assert.assertEquals("failTest", editTableAction.getMaxPjPoints());
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
		editTableAction.setSendMessage("string_9");
		editTableAction.setMessageId(10);

		// Execute
		editTableAction.unbindPersonnage();

		// Check
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
		Assert.assertEquals("failTest", editTableAction.getPointPoolModification());
		Assert.assertEquals("failTest", editTableAction.getMaxPjPoints());
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
		editTableAction.setSendMessage("string_9");
		editTableAction.setMessageId(10);

		// Execute
		editTableAction.refreshMessages();

		// Check
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
		Assert.assertEquals("failTest", editTableAction.getPointPoolModification());
		Assert.assertEquals("failTest", editTableAction.getMaxPjPoints());
		Assert.assertEquals("failTest", editTableAction.getPoolPointList());
		Assert.assertEquals("failTest", editTableAction.getPnjList());
		Assert.assertEquals("failTest", editTableAction.getPersonnageName());
	}
}
