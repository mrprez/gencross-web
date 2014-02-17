package com.mrprez.gencross.web.action;

import org.springframework.web.context.ContextLoader;

import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class SubscriptionAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
	private String confirmPassword;
	private String mail;

	
	@Override
	public String execute() throws Exception {
		return INPUT;	
	}
	
	public String authentification() throws Exception {
		IAuthentificationBS authentificationBS = (IAuthentificationBS)ContextLoader.getCurrentWebApplicationContext().getBean("authentificationBS");
		UserBO user = authentificationBS.createUser(username, confirmPassword, mail);
		if(user==null){
			this.addActionError("Ce login existe déjà");
			return ERROR;
		}
		ActionContext.getContext().getSession().put("user", user);
		return SUCCESS;
		
	}


	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
}
