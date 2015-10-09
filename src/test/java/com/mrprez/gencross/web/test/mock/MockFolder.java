package com.mrprez.gencross.web.test.mock;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;

public class MockFolder extends Folder {
	
	private boolean open = false;
	
	private List<Message> messageList = new ArrayList<Message>();

	public MockFolder(Store store) {
		super(store);
	}
	
	public void addMessage(Message message){
		messageList.add(message);
	}

	@Override
	public void appendMessages(Message[] arg0) throws MessagingException {
		// TODO Auto-generated method stub
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	@Override
	public void close(boolean arg0) throws MessagingException {
		open = false;
	}

	@Override
	public boolean create(int arg0) throws MessagingException {
		// TODO Auto-generated method stub
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		return false;
	}

	@Override
	public boolean delete(boolean arg0) throws MessagingException {
		// TODO Auto-generated method stub
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		return false;
	}

	@Override
	public boolean exists() throws MessagingException {
		// TODO Auto-generated method stub
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		return false;
	}

	@Override
	public Message[] expunge() throws MessagingException {
		// TODO Auto-generated method stub
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}

	@Override
	public Folder getFolder(String arg0) throws MessagingException {
		// TODO Auto-generated method stub
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}

	@Override
	public String getFullName() {
		// TODO Auto-generated method stub
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}

	@Override
	public Message getMessage(int index) throws MessagingException {
		return messageList.get(index - 1);
	}

	@Override
	public int getMessageCount() throws MessagingException {
		return messageList.size();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}

	@Override
	public Folder getParent() throws MessagingException {
		// TODO Auto-generated method stub
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}

	@Override
	public Flags getPermanentFlags() {
		// TODO Auto-generated method stub
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}

	@Override
	public char getSeparator() throws MessagingException {
		// TODO Auto-generated method stub
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		return 0;
	}

	@Override
	public int getType() throws MessagingException {
		// TODO Auto-generated method stub
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		return 0;
	}

	@Override
	public boolean hasNewMessages() throws MessagingException {
		// TODO Auto-generated method stub
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		return false;
	}

	@Override
	public boolean isOpen() {
		return open;
	}

	@Override
	public Folder[] list(String arg0) throws MessagingException {
		// TODO Auto-generated method stub
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}

	@Override
	public void open(int arg0) throws MessagingException {
		open = true;
	}

	@Override
	public boolean renameTo(Folder arg0) throws MessagingException {
		// TODO Auto-generated method stub
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		return false;
	}

}
