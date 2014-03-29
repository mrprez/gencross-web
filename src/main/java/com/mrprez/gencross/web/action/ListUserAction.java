package com.mrprez.gencross.web.action;

import java.util.List;

import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.opensymphony.xwork2.ActionSupport;

public class ListUserAction extends ActionSupport {
	private static final long serialVersionUID = 1L;

	private List<UserBO> userList;
	private String username;
	
	private IAuthentificationBS authentificationBS;
	
	
	@Override
	public String execute() throws Exception {
		userList = authentificationBS.getUserList();
		
		return INPUT;
	}

	public String remove() throws Exception {
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

	public IAuthentificationBS getAuthentificationBS() {
		return authentificationBS;
	}

	public void setAuthentificationBS(IAuthentificationBS authentificationBS) {
		this.authentificationBS = authentificationBS;
	}
	

}
