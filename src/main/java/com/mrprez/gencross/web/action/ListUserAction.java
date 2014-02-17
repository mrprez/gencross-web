package com.mrprez.gencross.web.action;

import java.util.List;

import org.springframework.web.context.ContextLoader;

import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.opensymphony.xwork2.ActionSupport;

public class ListUserAction extends ActionSupport {
	private static final long serialVersionUID = 1L;

	private List<UserBO> userList;
	private String username;
	
	@Override
	public String execute() throws Exception {
		IAuthentificationBS authentificationBS = (IAuthentificationBS)ContextLoader.getCurrentWebApplicationContext().getBean("authentificationBS");
		userList = authentificationBS.getUserList();
		
		return INPUT;
	}

	public String remove() throws Exception {
		IAuthentificationBS authentificationBS = (IAuthentificationBS)ContextLoader.getCurrentWebApplicationContext().getBean("authentificationBS");
		authentificationBS.removeUser(username);
		
		return execute();
	}
	
	
	public List<UserBO> getUserList() {
		return userList;
	}
	public void setUserList(List<UserBO> userList) {
		this.userList = userList;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	

}
