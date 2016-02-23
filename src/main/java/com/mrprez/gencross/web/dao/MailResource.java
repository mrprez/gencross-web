package com.mrprez.gencross.web.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.owasp.html.HtmlChangeListener;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

import com.mrprez.gencross.web.bo.ParamBO;
import com.mrprez.gencross.web.bo.TableMessageBO;
import com.mrprez.gencross.web.dao.face.IMailResource;
import com.mrprez.gencross.web.dao.face.IParamDAO;

public class MailResource implements IMailResource {
	private static final String INBOX = "INBOX"; 
	private static PolicyFactory messagePolicyFactory = 
				Sanitizers.FORMATTING.and(Sanitizers.BLOCKS).and(Sanitizers.LINKS).and(Sanitizers.STYLES).and(Sanitizers.TABLES);
	
	private IParamDAO paramDAO;
	
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
		textBodyPart.setText(text, "UTF-8", "html");
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
	public void send(Collection<String> toAdresses, String fromAddress, String subject, String text, String attachmentName, byte[] attachment) throws Exception{
		InternetAddress internetAddresses[] = new InternetAddress[toAdresses.size()];
		int index = 0;
		for(String toAdress : toAdresses){
			internetAddresses[index++] = new InternetAddress(toAdress);
		}
		send(internetAddresses, new InternetAddress(fromAddress), subject, text, attachmentName, attachment);
	}
	
	
	@Override
	public void sendAdminMail(String subject, String message) throws Exception {
		ParamBO paramAdress = paramDAO.getParam(ParamBO.ADMIN_ADRESS);
		if(paramAdress==null){
			return;
		}
		String address = (String) paramAdress.getValue();
		
		send(address, subject, message);
	}
	
	@Override
	public void sendError(Exception exception) throws Exception {
		ParamBO paramAdress = paramDAO.getParam(ParamBO.ADMIN_ADRESS);
		if(paramAdress==null){
			return;
		}
		String address = (String) paramAdress.getValue();
		String subject = exception.getClass().getSimpleName()+": "+exception.getMessage();
		StringBuilder text = new StringBuilder("Exception levée à "+new Date()+"\n\n");
		writeException(exception, text);
		send(address, subject, text.toString());
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
		
		Store store = receiverSession.getStore();
		store.connect(receiverSession.getProperty("mail.user"), receiverSession.getProperty("mail.password"));
		Folder folder = store.getFolder(INBOX);
		try{
			folder.open(Folder.READ_WRITE);
			Message mailTab[] =  folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
			for(int i=0; i<mailTab.length; i++){
				Message mail = mailTab[i];
				if(mail.getFrom().length == 0){
					sendError(new Exception("Mail sans auteur "+mail.getSubject()));
				}else{
					TableMessageBO tableMessage = new TableMessageBO();
					InternetAddress from = (InternetAddress) mail.getFrom()[0];
					tableMessage.setSenderMail(from.getAddress());
					tableMessage.setDate(mail.getReceivedDate());
					String messageText = getText(mail);
					List<String> intrusions = new ArrayList<String>();
					messageText = sanitize(messageText, intrusions);
					tableMessage.setData(messageText);
					if( ! intrusions.isEmpty() ){
						sendAdminMail("Mail recu avec des tentatives d'intrusion", 
								"Message de "+from+" à "+mail.getReceivedDate()+" avec les intrusions :\n"
								+StringUtils.join(intrusions, "\n"));
					}
					tableMessage.setSubject(mail.getSubject());
					if(tableMessage.getTableId()!=null){
						mail.setFlag(Flags.Flag.SEEN, true);
						result.add(tableMessage);
					}else{
						send(tableMessage.getSenderMail(), (String) paramDAO.getParam(ParamBO.TABLE_ADRESS).getValue(),
								"Invalid subject: " + tableMessage.getSubject(), 
								"Votre message n'a pas pu être associé à une table. Il faut que l'objet du mail contienne le numéro de la table entre crochet ('[<numero_table>]')." + "\n\n" + tableMessage.getData());
					}
				}
			}
			Logger.getLogger(getClass()).info(result.size()+" messages loaded from "+receiverSession.getProperty("mail.user"));
			
			return result;
		}finally{
			if(folder.isOpen()){
				folder.close(false);
			}
			store.close();
		}
	}
	
	
	private String sanitize(String text, List<String> detections) {
		MailSanitizeListener mailSanitizeListener = new MailSanitizeListener(detections);
		return messagePolicyFactory.sanitize(text, mailSanitizeListener, null);
	}

	private String getText(Message message) throws IOException, MessagingException{
		Object content = message.getContent();
		if(content instanceof Multipart){
			String text = extractFromMultipart((Multipart) content, "TEXT/HTML");
			if(text!=null){
				return text;
			}
			text = extractFromMultipart((Multipart) content, "TEXT/PLAIN");
			if(text!=null){
				return text;
			}
		}
		return content.toString();
	}
	
	private String extractFromMultipart(Multipart multipart, String mimeType) throws MessagingException, IOException{
		for(int i=0; i<multipart.getCount(); i++){
			BodyPart bodyPart = multipart.getBodyPart(i);
			if(bodyPart.getContentType().toLowerCase().startsWith(mimeType.toLowerCase())){
				return bodyPart.getContent().toString();
			}
		}
		return null;
	}

	public IParamDAO getParamDAO() {
		return paramDAO;
	}

	public void setParamDAO(IParamDAO paramDAO) {
		this.paramDAO = paramDAO;
	}
	
	private static class MailSanitizeListener implements HtmlChangeListener<String>{
		private final List<String> intrusionDescriptions;

		public MailSanitizeListener(List<String> intrusionDescriptions) {
			super();
			this.intrusionDescriptions = intrusionDescriptions;
		}

		@Override
		public void discardedAttributes(String context, String tagName, String... attributeNames) {
			intrusionDescriptions.add("Tag &lt;"+tagName+"&gt; and attributes ["+StringUtils.join(attributeNames, ", ")+"]");
			
		}

		@Override
		public void discardedTag(String context, String tagName) {
			intrusionDescriptions.add("Tag &lt;"+tagName+"&gt;");
		}
		
	}

}
