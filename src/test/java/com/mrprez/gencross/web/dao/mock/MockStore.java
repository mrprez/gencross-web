package com.mrprez.gencross.web.dao.mock;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

public class MockStore extends Store {

	protected MockStore(Session session, URLName urlname) {
		super(session, urlname);
	}

	@Override
	public Folder getDefaultFolder() throws MessagingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Folder getFolder(String arg0) throws MessagingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Folder getFolder(URLName arg0) throws MessagingException {
		// TODO Auto-generated method stub
		return null;
	}

}
