package com.mrprez.gencross.web.service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;

import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bo.UserDate;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.mrprez.gencross.ws.api.IAuthentificationService;

@WebService(endpointInterface = "com.mrprez.gencross.ws.api.IAuthentificationService" )
public class AuthentificationService implements IAuthentificationService, SOAPHandler<SOAPMessageContext> {

	public static Map<String, UserDate> authentifiedUsers = Collections.synchronizedMap(new HashMap<String, UserDate>());
	
	public static ThreadLocal<UserBO> localThreadUser = new ThreadLocal<UserBO>();
	
	private long sessionDuration = 30*60*1000;
	
	
	@Override
	public String authentificate(String username, String digest) throws Exception{
		IAuthentificationBS authentificationBS = (IAuthentificationBS) ContextLoader.getCurrentWebApplicationContext().getBean("authentificationBS");
		UserBO user = authentificationBS.authentificateUserDigest(username, digest);
		
		String token;
		synchronized (authentifiedUsers) {
			do{
				token = generateToken();
			}while(authentifiedUsers.containsKey(token));
			authentifiedUsers.put(token, new UserDate(user));
		}
		
		return token;
	}
	
	
	private String generateToken(){
		String allowedChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		int tokenSize = 20;
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<tokenSize; i++){
			int index = (int) (Math.random()*allowedChar.length());
			sb.append(allowedChar.charAt(index));
		}
		
		return sb.toString();
	}
	

	@Override
	public void close(MessageContext messageContext) {}

	@Override
	public boolean handleFault(SOAPMessageContext soapMessageContext) {
		return true;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext soapMessageContext) {
		try {
			Boolean outbound = (Boolean) soapMessageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
			if(outbound){
				return true;
			}
			String token = null;
			Iterator<?>  it = soapMessageContext.getMessage().getSOAPHeader().getChildElements();
			while(it.hasNext()){
				Object object = it.next();
				if(object instanceof SOAPElement){
					SOAPElement element = (SOAPElement) object;
					String key = element.getLocalName();
					if(TOKEN_KEY.equals(key)){
						token = element.getTextContent();
						if(authentifiedUsers.containsKey(token)){
							purgeOldUserToken();
							UserDate userDate = authentifiedUsers.get(token);
							userDate.resetTime();
							localThreadUser.set(userDate.getUser());
							return true;
						}
					}
				}
			}
			return false;
		} catch (SOAPException e) {
			logException(e);
			return false;
		}
	}

	@Override
	public Set<QName> getHeaders() {
		return null;
	}
	
	
	private void purgeOldUserToken(){
		synchronized (authentifiedUsers) {
			Iterator<String> tokenIt = authentifiedUsers.keySet().iterator();
			while(tokenIt.hasNext()){
				String token = tokenIt.next();
				UserDate userDate = authentifiedUsers.get(token);
				if(new Date().getTime() - userDate.getDate().getTime() > sessionDuration){
					authentifiedUsers.remove(token);
				}
			}
		}
	}
	
	private void logException(Throwable exception){
		Logger.getLogger(this.getClass()).error(exception.getMessage());
		StackTraceElement stackTrace[] = exception.getStackTrace();
		for(int i=0; i<stackTrace.length; i++){
			Logger.getLogger(this.getClass()).error("\t"+stackTrace[i].toString());
		}
		if(exception.getCause()!=exception){
			Logger.getLogger(this.getClass()).error("Caused by:");
			logException(exception.getCause());
		}
	}
	

}
