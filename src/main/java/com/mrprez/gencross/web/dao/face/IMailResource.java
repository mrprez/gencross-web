package com.mrprez.gencross.web.dao.face;

import java.io.File;
import java.util.Collection;

import javax.mail.Message;
import javax.mail.MessagingException;

import com.mrprez.gencross.web.bo.TableMessageBO;


public interface IMailResource {
	
	String getDefaultFromAdress() throws Exception;

	void setDefaultFromAdress(String defaultFromAdress) throws Exception;
	
	void send(String toAdress, String subject, String text) throws Exception;

	void send(String toAdress, String subject, String text, String attachmentName, byte[] attachment) throws Exception;

	void sendError(Exception exception) throws Exception;

	void send(String toAdress, String subject, String text, File attachment) throws Exception;

	Collection<TableMessageBO> getMails() throws Exception;

	void send(Message message) throws MessagingException;

	void send(String toAdress, String fromAddress, String subject, String text) throws Exception;
	
	void send(Collection<String> toAdresses, String subject, String text) throws Exception;
	
	void send(Collection<String> toAdresses, String fromAddress, String subject, String text) throws Exception;
	
	void send(Collection<String> toAdresses, String fromAddress, String subject, String text, String attachmentName, byte[] attachment) throws Exception;

}
