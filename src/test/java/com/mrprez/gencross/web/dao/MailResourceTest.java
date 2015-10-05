package com.mrprez.gencross.web.dao;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.mrprez.gencross.web.dao.mock.MockTransport;

public class MailResourceTest {
	
	private MailResource mailResource; 
	
	
	
	
	@Before
	public void setup(){
		Properties mailProps = new Properties();
        Session senderSession = Session.getInstance(mailProps);
		Session receiverSession = Session.getInstance(mailProps);
		
		mailResource = new MailResource(senderSession, receiverSession);
	}
	
	@Test
	public void testSend_Message() throws MessagingException{
		// Prepare
		Message message = Mockito.mock(Message.class);
		
		// Execute
		mailResource.send(message);
		
		// Check
		Assert.assertEquals(1, MockTransport.getSendMessageRequestList().size());
		Assert.assertSame(message, MockTransport.getSendMessageRequestList().get(0).getMessage());
	}
	

}
