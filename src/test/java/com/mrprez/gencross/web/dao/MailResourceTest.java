package com.mrprez.gencross.web.dao;

import java.io.IOException;
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
	
	private static String MAIL_TRANSPORT_MOCK_NAME = "gencross-web-mock";
	
	private MailResource mailResource; 
	
	
	
	@Before
	public void setup(){
		Properties mailProps = new Properties();
        mailProps.put("mock.name", MAIL_TRANSPORT_MOCK_NAME);
		Session senderSession = Session.getInstance(mailProps);
		Session receiverSession = Session.getInstance(mailProps);
		
		mailResource = new MailResource(senderSession, receiverSession);
	}
	
	@Test
	public void testSend_Message_Success() throws MessagingException{
		// Prepare
		Message message = Mockito.mock(Message.class);
		
		// Execute
		mailResource.send(message);
		
		// Check
		MockTransport mockTransport = MockTransport.getMockTransport(MAIL_TRANSPORT_MOCK_NAME);
		Assert.assertEquals(1, mockTransport.getSendMessageRequestList().size());
		Assert.assertSame(message, mockTransport.getSendMessageRequestList().get(0).getMessage());
		
	}
	
	
	@Test
	public void testSend_Message_Fail() throws MessagingException, IOException{
		// Prepare
		Message message = Mockito.mock(Message.class);
		Mockito.when(message.getSubject()).thenThrow(new MessagingException());
		
		// Execute
		MessagingException messagingException = null;
		try{
			mailResource.send(message);
		}catch(MessagingException me){
			messagingException = me;
		}
		
		// Check
		MockTransport mockTransport = MockTransport.getMockTransport(MAIL_TRANSPORT_MOCK_NAME);
		Assert.assertNotNull(messagingException);
		Assert.assertFalse(mockTransport.isConnected());
		
	}
	

}
