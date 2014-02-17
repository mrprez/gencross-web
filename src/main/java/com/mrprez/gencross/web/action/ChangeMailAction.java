package com.mrprez.gencross.web.action;

import org.springframework.web.context.ContextLoader;

import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class ChangeMailAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private String mail;
	
	
	public String execute() throws Exception{
		return INPUT;
	}
	
	public String changeMail() throws Exception{
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		IAuthentificationBS authentificationBS = (IAuthentificationBS)ContextLoader.getCurrentWebApplicationContext().getBean("authentificationBS");
		authentificationBS.changeMail(user, mail);
		return SUCCESS;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
	
	
	

}
