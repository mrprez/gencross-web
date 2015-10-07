package com.mrprez.gencross.web.dao.mock;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

public class MockStore extends Store {
	private static Map<String, MockStore> mockStoreMap = new ConcurrentHashMap<String, MockStore>();
	
	private Map<String, MockFolder> folderMap = new HashMap<String, MockFolder>();
	private String name;
	
	public MockStore(Session session, URLName urlname) {
		super(session, urlname);
		name = session.getProperty("mock.name");
		synchronized (mockStoreMap) {
			if(mockStoreMap.containsKey(name)){
				folderMap.putAll(mockStoreMap.get(name).folderMap);
			}
			mockStoreMap.put(name, this);
		}
		
	}
	
	public static MockStore getMockStore(String name){
		synchronized(mockStoreMap){
			if( ! mockStoreMap.containsKey(name)){
				mockStoreMap.put(name, new MockStore(null, null));
			}
			return mockStoreMap.get(name);
		}
	}
	
	public static void clearMockStoreMap(){
		mockStoreMap.clear();
	}
	
	public void installFolder(String folderName){
		folderMap.put(folderName, new MockFolder(this));
	}

	@Override
	public Folder getDefaultFolder() throws MessagingException {
		return folderMap.get(null);
	}

	@Override
	public Folder getFolder(String arg0) throws MessagingException {
		return folderMap.get(arg0);
	}

	@Override
	public Folder getFolder(URLName arg0) throws MessagingException {
		try {
			return folderMap.get(arg0.getURL().getPath());
		} catch (MalformedURLException e) {
			throw new MessagingException("Malformed URL: "+arg0, e);
		}
	}
	
	@Override
	protected boolean protocolConnect(String host, int port, String user, String password) throws MessagingException {
		return true;
	}

}
