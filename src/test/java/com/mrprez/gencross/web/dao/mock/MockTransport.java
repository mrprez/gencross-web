package com.mrprez.gencross.web.dao.mock;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;

public class MockTransport extends Transport {
	private static Map<String, MockTransport> mockTransportMap = new ConcurrentHashMap<String, MockTransport>();
	
	private String name;
	private List<SendMessageRequest> sendMessageRequestList = Collections.synchronizedList(new ArrayList<SendMessageRequest>());
	
	

	public MockTransport(Session session, URLName urlname) throws MalformedURLException {
		super(session, urlname);
		name = session.getProperty("mock.name");
		mockTransportMap.put(name, this);
	}
	
	public static MockTransport getMockTransport(String name){
		return mockTransportMap.get(name);
	}
	
	public static void clearMockTransport(){
		mockTransportMap.clear();;
	}

	@Override
	public void sendMessage(Message message, Address[] addresses) throws MessagingException {
		if(super.isConnected()){
			message.getSubject();
			sendMessageRequestList.add(new SendMessageRequest(this, message, addresses));
		}else{
			throw new MessagingException("Not connected");
		}
	}
	
	public List<SendMessageRequest> getSendMessageRequestList(){
		return sendMessageRequestList;
	}
	
	@Override
	public synchronized void connect(String arg0, int arg1, String arg2, String arg3) throws MessagingException {
		setConnected(true);
	}
	

	
	public class SendMessageRequest{
		private MockTransport transport;
		private Message message;
		private Address[] addresses;
		
		public SendMessageRequest(MockTransport transport, Message message, Address[] addresses) {
			super();
			this.transport = transport;
			this.message = message;
			this.addresses = addresses;
		}
		public MockTransport getTransport() {
			return transport;
		}
		public Message getMessage() {
			return message;
		}
		public Address[] getAddresses() {
			return addresses;
		}
	}

}
