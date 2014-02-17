package com.mrprez.gencross.web.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.web.context.ContextLoader;

import com.mrprez.gencross.web.bo.ParamBO;
import com.mrprez.gencross.web.bo.TableMessageBO;
import com.mrprez.gencross.web.dao.face.IMailResource;
import com.mrprez.gencross.web.dao.face.IParamDAO;

public class MailResource implements IMailResource {
	private static final String INBOX = "INBOX"; 
	
	private Session senderSession;
	private Session receiverSession;
	private InternetAddress defaultFromAdress;
	

	public MailResource(Object senderSession, Object receiverSession) {
		super();
		this.senderSession = (Session)senderSession;
		this.receiverSession = (Session)receiverSession;
	}

	public String getDefaultFromAdress() {
		return defaultFromAdress.getAddress();
	}

	public void setDefaultFromAdress(String defaultFromAdress) throws AddressException {
		this.defaultFromAdress = new InternetAddress(defaultFromAdress);
	}
	
	private void send(InternetAddress[] toAdresses, InternetAddress fromAdress, String subject, String text, String attachmentName, byte[] attachment) throws Exception{
		MimeMessage message = new MimeMessage(senderSession);
		message.setFrom(fromAdress);
		message.addRecipients(Message.RecipientType.TO, toAdresses);
		message.setSubject(subject);
		Multipart multipart = new MimeMultipart();
		MimeBodyPart textBodyPart = new MimeBodyPart();
		textBodyPart.setText(text);
		multipart.addBodyPart(textBodyPart);
		if(attachment!=null){
			MimeBodyPart attachmentBodyPart = new MimeBodyPart();
			DataSource source = new ByteArrayDataSource(attachment, "application/octet-stream");
			attachmentBodyPart.setDataHandler(new DataHandler(source));
			attachmentBodyPart.setFileName(attachmentName);
			multipart.addBodyPart(attachmentBodyPart);
		}
		message.setContent(multipart);
		
		send(message);
	}
	
	@Override
	public void send(Message message) throws MessagingException{
		Transport transport = senderSession.getTransport("smtp");
		try{
			transport.connect();
			transport.sendMessage(message, message.getAllRecipients());
		}finally{
			transport.close();
		}
	}

	@Override
	public void send(String toAdress, String subject, String text) throws Exception {
		send(new InternetAddress[]{new InternetAddress(toAdress)}, defaultFromAdress, subject, text, null, null);
	}
	
	@Override
	public void send(String toAdress, String fromAddress, String subject, String text) throws Exception {
		send(new InternetAddress[]{new InternetAddress(toAdress)}, new InternetAddress(fromAddress), subject, text, null, null);
	}
	
	@Override
	public void send(String toAdress, String subject, String text, String attachmentName, byte[] attachment) throws Exception {
		send(new InternetAddress[]{new InternetAddress(toAdress)}, defaultFromAdress, subject, text, attachmentName, attachment);
	}
	
	@Override
	public void send(Collection<String> toAdresses, String subject, String text) throws Exception {
		InternetAddress internetAddresses[] = new InternetAddress[toAdresses.size()];
		int index = 0;
		for(String toAdress : toAdresses){
			internetAddresses[index++] = new InternetAddress(toAdress);
		}
		send(internetAddresses, defaultFromAdress, subject, text, null, null);
	}
	
	@Override
	public void send(Collection<String> toAdresses, String fromAdress, String subject, String text) throws Exception {
		InternetAddress internetAddresses[] = new InternetAddress[toAdresses.size()];
		int index = 0;
		for(String toAdress : toAdresses){
			internetAddresses[index++] = new InternetAddress(toAdress);
		}
		send(internetAddresses, new InternetAddress(fromAdress), subject, text, null, null);
	}
	
	@Override
	public void send(String toAdress, String subject, String text, File attachment) throws Exception {
		InternetAddress internetAddresses[] = new InternetAddress[]{new InternetAddress(toAdress)};
		byte content[] = new byte[(int) attachment.length()];
		FileInputStream fis = new FileInputStream(attachment);
		try{
			fis.read(content);
		}finally{
			fis.close();
		}
		send(internetAddresses, defaultFromAdress, subject, text, attachment.getName(), content);
	}
	
	@Override
	public void sendError(Exception exception) throws Exception {
		IParamDAO paramDAO = (IParamDAO) ContextLoader.getCurrentWebApplicationContext().getBean("ParamDAO");
		ParamBO paramAdress = paramDAO.getParam(ParamBO.ADMIN_ADRESS);
		if(paramAdress==null){
			return;
		}
		String adress = (String) paramAdress.getValue();
		String subject = exception.getClass().getSimpleName()+": "+exception.getMessage();
		StringBuilder text = new StringBuilder("Exception levée à "+new Date()+"\n\n");
		writeException(exception, text);
		send(adress, subject, text.toString());
	}
	
	private void writeException(Throwable exception, StringBuilder text){
		text.append(exception.getClass().getName()+": "+exception.getMessage()+"\n");
		StackTraceElement stackTrace[] = exception.getStackTrace();
		for(int i=0; i<stackTrace.length; i++){
			text.append("\t");
			text.append(stackTrace[i].getClassName());
			text.append(".");
			text.append(stackTrace[i].getMethodName());
			text.append("(");
			text.append(stackTrace[i].getLineNumber());
			text.append(")\n");
		}
		if(exception.getCause()!=null && exception.getCause()!=exception){
			text.append("Caused by: ");
			writeException(exception.getCause(), text);
		}
	}
	
	@Override
	public Collection<TableMessageBO> getMails() throws Exception {
		Collection<TableMessageBO> result = new ArrayList<TableMessageBO>();
		Store store = receiverSession.getStore("imap");
		store.connect();
		Folder folder = store.getFolder(INBOX);
		try{
			folder.open(Folder.READ_WRITE);
			Message mailTab[] =  folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
			for(int i=0; i<mailTab.length; i++){
				Message mail = mailTab[i];
				TableMessageBO tableMessage = new TableMessageBO();
				tableMessage.setDate(mail.getReceivedDate());
				tableMessage.setData(getText(mail));
				if(mail.getFrom().length > 0){
					InternetAddress from = (InternetAddress) mail.getFrom()[0];
					tableMessage.setSenderMail(from.getAddress());
				}
				tableMessage.setSubject(mail.getSubject());
				mail.setFlag(Flags.Flag.SEEN, true);
				result.add(tableMessage);
			}
			return result;
		}finally{
			if(folder.isOpen()){
				folder.close(false);
			}
		}
	}
	
	private String getText(Message message) throws IOException, MessagingException{
		Object content = message.getContent();
		if(content instanceof Multipart){
			Multipart multipart = (Multipart) content;
			for(int i=0; i<multipart.getCount(); i++){
				BodyPart bodyPart = multipart.getBodyPart(i);
				if(bodyPart.getContentType().toLowerCase().startsWith("text/plain")){
					return bodyPart.getContent().toString();
				}
			}
		}
		return content.toString();
	}

}
