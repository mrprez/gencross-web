package com.mrprez.gencross.web.test.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.mrprez.gencross.web.bo.ParamBO;
import com.mrprez.gencross.web.bo.TableMessageBO;
import com.mrprez.gencross.web.dao.MailResource;
import com.mrprez.gencross.web.dao.face.IParamDAO;
import com.mrprez.gencross.web.test.mock.MockFolder;
import com.mrprez.gencross.web.test.mock.MockStore;
import com.mrprez.gencross.web.test.mock.MockTransport;
import com.mrprez.gencross.web.test.mock.MockTransport.SendMessageRequest;

public class MailResourceTest {
	
	private static String MAIL_MOCK_NAME = "gencross-web-mock";
	private static String DEFAULT_FROM_ADDRESS = "defaultFromAddress@mail.com";
	
	private Session senderSession;
	private Session receiverSession;
	
	private MailResource mailResource; 
	
	
	
	@Before
	public void setup() throws AddressException, NoSuchProviderException{
		Properties mailProps = new Properties();
        mailProps.put("mock.name", MAIL_MOCK_NAME);
        mailProps.put("mail.store.protocol", "imap");
        MockTransport.clearMockTransport();
		senderSession = Session.getInstance(mailProps);
		receiverSession = Session.getInstance(mailProps);
		receiverSession.getStore();
		
		mailResource = new MailResource(senderSession, receiverSession);
		mailResource.setDefaultFromAdress(DEFAULT_FROM_ADDRESS);
	}
	
	@Test
	public void testSend_Message_Success() throws MessagingException{
		// Prepare
		Message message = Mockito.mock(Message.class);
		
		// Execute
		mailResource.send(message);
		
		// Check
		MockTransport mockTransport = MockTransport.getMockTransport(MAIL_MOCK_NAME);
		Assert.assertEquals(1, mockTransport.getSendMessageRequestList().size());
		Assert.assertSame(message, mockTransport.getSendMessageRequestList().get(0).getMessage());
		Assert.assertFalse(mockTransport.isConnected());
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
		MockTransport mockTransport = MockTransport.getMockTransport(MAIL_MOCK_NAME);
		Assert.assertNotNull(messagingException);
		Assert.assertFalse(mockTransport.isConnected());
	}
	
	
	@Test
	public void testSend_SimplestMethod() throws Exception{
		// Execute
		mailResource.send("toAddress@mail.com", "New subject", "test simplest send method");
		
		// Check
		MockTransport mockTransport = MockTransport.getMockTransport(MAIL_MOCK_NAME);
		Assert.assertEquals(1, mockTransport.getSendMessageRequestList().size());
		SendMessageRequest sendMessageRequest = mockTransport.getSendMessageRequestList().get(0);
		checkSendMessageRequest(sendMessageRequest, "New subject", "test simplest send method", null, null, null, "toAddress@mail.com");
	}
	
	
	@Test
	public void testSend_WithFromAddress() throws Exception{
		// Execute
		mailResource.send("toAddress@mail.com", "anotherFromAddress@mail.com", "New subject", "test send method with from address");
		
		// Check
		MockTransport mockTransport = MockTransport.getMockTransport(MAIL_MOCK_NAME);
		Assert.assertEquals(1, mockTransport.getSendMessageRequestList().size());
		SendMessageRequest sendMessageRequest = mockTransport.getSendMessageRequestList().get(0);
		checkSendMessageRequest(sendMessageRequest, "New subject", "test send method with from address", "anotherFromAddress@mail.com", null, null, "toAddress@mail.com");
	}
	
	@Test
	public void testSend_WithAttachement() throws Exception{
		// Prepare
		byte[] attachement = "I am batman".getBytes();
		
		// Execute
		mailResource.send("toAddress@mail.com", "New subject", "This is batman file", "batman attachment", attachement);
		
		// Check
		MockTransport mockTransport = MockTransport.getMockTransport(MAIL_MOCK_NAME);
		Assert.assertEquals(1, mockTransport.getSendMessageRequestList().size());
		SendMessageRequest sendMessageRequest = mockTransport.getSendMessageRequestList().get(0);
		checkSendMessageRequest(sendMessageRequest, "New subject", "This is batman file", null, "batman attachment", attachement, "toAddress@mail.com");
	}
	
	@Test
	public void testSend_ToAddresslist() throws Exception{
		// Prepare
		List<String> toAdresses = Arrays.asList("batman@mail.com", "robin@mail.com", "catwoman@mail.com");
		String subject = "A subject";
		String text = "Test send mail to a list of e-mail addresses.";
		
		// Execute
		mailResource.send(toAdresses, subject, text);
		
		// Check
		MockTransport mockTransport = MockTransport.getMockTransport(MAIL_MOCK_NAME);
		Assert.assertEquals(1, mockTransport.getSendMessageRequestList().size());
		SendMessageRequest sendMessageRequest = mockTransport.getSendMessageRequestList().get(0);
		checkSendMessageRequest(sendMessageRequest, subject, text, null, null, null, "batman@mail.com", "robin@mail.com", "catwoman@mail.com");
	}
	
	@Test
	public void testSend_ToAddresslistWithFromAddress() throws Exception{
		// Prepare
		List<String> toAdresses = Arrays.asList("batman@mail.com", "robin@mail.com", "catwoman@mail.com");
		String subject = "A subject";
		String text = "Test send mail to a list of e-mail addresses.";
		String fromAddress = "hulk@mail.com";
		
		// Execute
		mailResource.send(toAdresses, fromAddress, subject, text);
		
		// Check
		MockTransport mockTransport = MockTransport.getMockTransport(MAIL_MOCK_NAME);
		Assert.assertEquals(1, mockTransport.getSendMessageRequestList().size());
		SendMessageRequest sendMessageRequest = mockTransport.getSendMessageRequestList().get(0);
		checkSendMessageRequest(sendMessageRequest, subject, text, fromAddress, null, null, "batman@mail.com", "robin@mail.com", "catwoman@mail.com");
	}
	
	@Test
	public void testSend_File() throws Exception{
		// Prepare
		String toAdress = "batman@mail.com";
		String subject = "File test";
		String text = "Test send mail with a file.";
		File attachment = new File("src/test/java/com/mrprez/gencross/web/test/dao/MailResourceTest.java");
		
		// Execute
		mailResource.send(toAdress, subject, text, attachment);
		
		// Check
		MockTransport mockTransport = MockTransport.getMockTransport(MAIL_MOCK_NAME);
		Assert.assertEquals(1, mockTransport.getSendMessageRequestList().size());
		SendMessageRequest sendMessageRequest = mockTransport.getSendMessageRequestList().get(0);
		byte[] attachmentBytes = IOUtils.toByteArray(new FileInputStream(attachment));
		checkSendMessageRequest(sendMessageRequest, subject, text, null, "MailResourceTest.java", attachmentBytes, "batman@mail.com");
	}
	
	@Test
	public void testSend_ToAddressListWithFromAddressAndAttchment() throws Exception{
		// Prepare
		List<String> toAdresses = Arrays.asList("batman@mail.com", "robin@mail.com", "catwoman@mail.com");
		String subject = "A subject";
		String text = "Test send mail to a list of e-mail addresses with attachment.";
		String fromAddress = "hulk@mail.com";
		String attachmentName = "to address list attachment name";
		byte[] attachment = "I am batman".getBytes();
		
		// Execute
		mailResource.send(toAdresses, fromAddress, subject, text, attachmentName, attachment);
		
		// Check
		MockTransport mockTransport = MockTransport.getMockTransport(MAIL_MOCK_NAME);
		Assert.assertEquals(1, mockTransport.getSendMessageRequestList().size());
		SendMessageRequest sendMessageRequest = mockTransport.getSendMessageRequestList().get(0);
		checkSendMessageRequest(sendMessageRequest, subject, text, fromAddress, attachmentName, attachment, "batman@mail.com", "robin@mail.com", "catwoman@mail.com");
	}
	
	@Test
	public void testGetSetDefaultFromAdress() throws AddressException{
		mailResource.setDefaultFromAdress("flash@mail.com");
		Assert.assertEquals("flash@mail.com", mailResource.getDefaultFromAdress());
	}
	
	@Test
	public void testGetSetParamDAO() throws AddressException{
		IParamDAO paramDAO = Mockito.mock(IParamDAO.class);
		mailResource.setParamDAO(paramDAO);
		Assert.assertEquals(paramDAO, mailResource.getParamDAO());
	}
	
	@Test
	public void testSendError_Success() throws Exception{
		// Prepare
		IParamDAO paramDAO = Mockito.mock(IParamDAO.class);
		ParamBO param = new ParamBO();
		param.setKey(ParamBO.ADMIN_ADRESS);
		param.setType(ParamBO.STRING_TYPE);
		param.setValue("gothamMayor@mail.com");
		Mockito.when(paramDAO.getParam(ParamBO.ADMIN_ADRESS)).thenReturn(param);
		mailResource.setParamDAO(paramDAO);
		Exception causeException = new Exception("Cause Exception");
		Exception exception = new Exception("Exception to send", causeException);
		
		// Execute
		mailResource.sendError(exception);
		
		// Check
		MockTransport mockTransport = MockTransport.getMockTransport(MAIL_MOCK_NAME);
		Assert.assertEquals(1, mockTransport.getSendMessageRequestList().size());
		SendMessageRequest sendMessageRequest = mockTransport.getSendMessageRequestList().get(0);
		
		Message message = sendMessageRequest.getMessage();
		Assert.assertEquals("Exception: Exception to send", message.getSubject());
		Multipart multipart = (Multipart) message.getContent();
		MimeBodyPart textBodyPart = (MimeBodyPart) multipart.getBodyPart(0);
		String mailContent = (String) textBodyPart.getContent();
		String[] mailLines = mailContent.split("\n");
		Assert.assertTrue(mailLines[0].startsWith("Exception levée à "));
		
		StackTraceElement[] stackTrace = exception.getStackTrace();
		for(int i = 0; i<stackTrace.length; i++){
			String expectedLine = "\t"+stackTrace[i].getClassName()+"."+stackTrace[i].getMethodName()+"("+stackTrace[i].getLineNumber()+")";
			Assert.assertEquals(expectedLine, mailLines[i+3]);
		}
		
		Assert.assertEquals("Caused by: java.lang.Exception: Cause Exception", mailLines[stackTrace.length+3]);
		
		StackTraceElement[] causeStackTrace = causeException.getStackTrace();
		for(int i = 0; i<causeStackTrace.length; i++){
			String expectedLine = "\t"+causeStackTrace[i].getClassName()+"."+causeStackTrace[i].getMethodName()+"("+causeStackTrace[i].getLineNumber()+")";
			Assert.assertEquals(expectedLine, mailLines[i+stackTrace.length+4]);
		}
		
		Assert.assertEquals(1, sendMessageRequest.getAddresses().length);
		InternetAddress messageAddress = (InternetAddress) sendMessageRequest.getAddresses()[0];
		Assert.assertEquals("gothamMayor@mail.com", messageAddress.getAddress());
	}
	
	@Test
	public void testSendError_Fail() throws Exception{
		// Prepare
		IParamDAO paramDAO = Mockito.mock(IParamDAO.class);
		mailResource.setParamDAO(paramDAO);
		Exception exception = new Exception("Exception to send");
		
		// Execute
		mailResource.sendError(exception);
		
		// Check
		Assert.assertNull(MockTransport.getMockTransport(MAIL_MOCK_NAME));
	}
	
	
	@Test
	public void testGetMails_Success() throws Exception{
		// Prepare
		IParamDAO paramDao = Mockito.mock(IParamDAO.class);
		ParamBO param = new ParamBO();
		param.setKey(ParamBO.TABLE_ADRESS);
		param.setType(ParamBO.STRING_TYPE);
		param.setValue("table.address@mail.com");
		Mockito.when(paramDao.getParam(ParamBO.TABLE_ADRESS)).thenReturn(param);
		mailResource.setParamDAO(paramDao);
		
		MockStore.getMockStore(MAIL_MOCK_NAME).installFolder("INBOX");
		MockFolder folder = (MockFolder) MockStore.getMockStore(MAIL_MOCK_NAME).getFolder("INBOX");
		
		folder.addMessage(buildMessage("mayor@mail.com", "table@mail.com", "[5] Mission 01", "Eteignez le bat signal!"));
		folder.addMessage(buildMessage("batman@mail.com", "table@mail.com", "Re: [5] Mission 01", "La mission est annulée.\nJe te retrouve à la la bat-cave."));
		folder.addMessage(buildMessage("joker@mail.com", "table@mail.com", "Ha ha ha", "Why so serious?"));
		
		// Execute
		Collection<TableMessageBO> tableMessageList = mailResource.getMails();
		
		// Check
		Assert.assertEquals(2, tableMessageList.size());
		Iterator<TableMessageBO> tableMessageIt = tableMessageList.iterator();
		
		TableMessageBO tableMessage1 = tableMessageIt.next();
		Assert.assertEquals("Eteignez le bat signal!", tableMessage1.getData());
		Assert.assertEquals("mayor@mail.com", tableMessage1.getSenderMail());
		Assert.assertEquals(new Integer(5), tableMessage1.getTableId());
		Assert.assertEquals("Mission 01", tableMessage1.getTitle());
		Assert.assertEquals("[5] Mission 01", tableMessage1.getSubject());
		
		TableMessageBO tableMessage2 = tableMessageIt.next();
		Assert.assertEquals("La mission est annulée.\nJe te retrouve à la la bat-cave.", tableMessage2.getData());
		Assert.assertEquals("batman@mail.com", tableMessage2.getSenderMail());
		Assert.assertEquals(new Integer(5), tableMessage2.getTableId());
		Assert.assertEquals("Mission 01", tableMessage2.getTitle());
		Assert.assertEquals("Re: [5] Mission 01", tableMessage2.getSubject());
		
		MockTransport mockTransport = MockTransport.getMockTransport(MAIL_MOCK_NAME);
		Assert.assertEquals(1, mockTransport.getSendMessageRequestList().size());
		
		SendMessageRequest sendMessageRequest = mockTransport.getSendMessageRequestList().get(0);
		String text = "Votre message n'a pas pu être associé à une table. Il faut que l'objet du mail contienne le numéro de la table entre crochet ('[<numero_table>]').\n\n\n\nWhy so serious?";
		checkSendMessageRequest(sendMessageRequest, "Invalid subject: Ha ha ha", text, (String)param.getValue(), null, null, "joker@mail.com");
		
		Assert.assertFalse(folder.isOpen());
		Assert.assertFalse(MockStore.getMockStore(MAIL_MOCK_NAME).isConnected());
	}
	
	
	@Test
	public void testGetMails_Fail_InvalidFromAddress() throws Exception{
		// Prepare
		IParamDAO paramDAO = Mockito.mock(IParamDAO.class);
		ParamBO param = new ParamBO();
		param.setKey(ParamBO.ADMIN_ADRESS);
		param.setType(ParamBO.STRING_TYPE);
		param.setValue("gothamMayor@mail.com");
		Mockito.when(paramDAO.getParam(ParamBO.ADMIN_ADRESS)).thenReturn(param);
		mailResource.setParamDAO(paramDAO);
		
		MockStore.getMockStore(MAIL_MOCK_NAME).installFolder("INBOX");
		MockFolder folder = (MockFolder) MockStore.getMockStore(MAIL_MOCK_NAME).getFolder("INBOX");
		Message message = Mockito.mock(Message.class);
		Mockito.when(message.getFrom()).thenReturn(new Address[0]);
		Mockito.when(message.match(Mockito.any(FlagTerm.class))).thenAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				FlagTerm flagTerm = (FlagTerm) invocation.getArguments()[0];
				if(flagTerm.getFlags().contains(Flags.Flag.SEEN)){
					return ! flagTerm.getTestSet();
				}
				return false;
			}
		});
		Mockito.when(message.getContent()).thenReturn("Any content");
		Mockito.when(message.getSubject()).thenReturn("Any subject");
		folder.addMessage(message);
		
		// Execute
		Collection<TableMessageBO> tableMessageList = mailResource.getMails();
		
		// Check
		Assert.assertTrue(tableMessageList.isEmpty());
		
		MockTransport mockTransport = MockTransport.getMockTransport(MAIL_MOCK_NAME);
		Assert.assertEquals(1, mockTransport.getSendMessageRequestList().size());
		SendMessageRequest sendMessageRequest = mockTransport.getSendMessageRequestList().get(0);
		Assert.assertEquals("Exception: Mail sans auteur Any subject", sendMessageRequest.getMessage().getSubject());
		Assert.assertEquals("gothamMayor@mail.com", sendMessageRequest.getAddresses()[0].toString());
		Assert.assertEquals("defaultFromAddress@mail.com", sendMessageRequest.getMessage().getFrom()[0].toString());
		
		Assert.assertFalse(folder.isOpen());
	}
		
	
	@Test
	public void testGetMails_Fail_Exception() throws Exception{
		// Prepare
		MockStore.getMockStore(MAIL_MOCK_NAME).installFolder("INBOX");
		MockFolder folder = (MockFolder) MockStore.getMockStore(MAIL_MOCK_NAME).getFolder("INBOX");
		Message message = Mockito.mock(Message.class);
		Mockito.when(message.match(Mockito.any(FlagTerm.class))).thenAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				FlagTerm flagTerm = (FlagTerm) invocation.getArguments()[0];
				if(flagTerm.getFlags().contains(Flags.Flag.SEEN)){
					return ! flagTerm.getTestSet();
				}
				return false;
			}
		});
		Exception thrownException = new MessagingException("Test Exception");
		Mockito.when(message.getReceivedDate()).thenThrow(thrownException);
		folder.addMessage(message);
		
		// Execute
		Exception caughtException = null;
		try{
			mailResource.getMails();
		}catch(Exception e){
			caughtException = e;
		}
		
		// Check
		Assert.assertNotNull(caughtException);
		Assert.assertEquals(thrownException, caughtException);
		
		Assert.assertFalse(folder.isOpen());
	}
	
	
	private Message buildMessage(String fromAddress, String toAddress, String subject, String text) throws AddressException, MessagingException{
		MimeMessage message = new MimeMessage(receiverSession);
		
		message.setFrom(new InternetAddress(fromAddress));
		message.addRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(toAddress)});
		message.setSubject(subject);
		Multipart multipart = new MimeMultipart();
		MimeBodyPart textBodyPart = new MimeBodyPart();
		textBodyPart.setText(text);
		multipart.addBodyPart(textBodyPart);
		message.setContent(multipart);
		
		message.setFlag(Flags.Flag.SEEN, false);
		
		return message;
	}
	
	
	
	private void checkSendMessageRequest(SendMessageRequest sendMessageRequest, String subject, String text, String fromAddress, String attachmentName, byte[] attachment, String... addresses) throws MessagingException, IOException{
		Message message = sendMessageRequest.getMessage();
		Assert.assertEquals(subject, message.getSubject());
		Multipart multipart = (Multipart) message.getContent();
		MimeBodyPart textBodyPart = (MimeBodyPart) multipart.getBodyPart(0);
		Assert.assertEquals(text, textBodyPart.getContent());
		
		Assert.assertEquals(1, message.getFrom().length);
		InternetAddress internetAddress = (InternetAddress) message.getFrom()[0];
		if(fromAddress!=null){
			Assert.assertEquals(fromAddress, internetAddress.getAddress());
		}else{
			Assert.assertEquals(DEFAULT_FROM_ADDRESS, internetAddress.getAddress());
		}
		
		if(attachment!=null && attachmentName!=null){
			Assert.assertEquals(2, multipart.getCount());
			MimeBodyPart attachmentBodyPart = (MimeBodyPart) multipart.getBodyPart(1);
			Assert.assertEquals(attachmentName, attachmentBodyPart.getFileName());
			ByteArrayDataSource dataSource = (ByteArrayDataSource) attachmentBodyPart.getDataHandler().getDataSource();
			InputStream inputStream = dataSource.getInputStream();
			byte[] dataSourceByte = new byte[attachment.length];
			inputStream.read(dataSourceByte);
			Assert.assertEquals(0, inputStream.available());
			Assert.assertArrayEquals(attachment, dataSourceByte);
		}else{
			Assert.assertEquals(1, multipart.getCount());
		}
		
		Assert.assertEquals(addresses.length, sendMessageRequest.getAddresses().length);
		for(int i=0; i<addresses.length; i++){
			InternetAddress messageAddress = (InternetAddress) sendMessageRequest.getAddresses()[i];
			Assert.assertEquals(addresses[i], messageAddress.getAddress());
		}
	}
	

}
