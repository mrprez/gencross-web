package com.mrprez.gencross.web.action;

import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class ChangeMailAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private IAuthentificationBS authentificationBS;
	
	private String mail;
	
	
	public String execute() throws Exception{
		return INPUT;
	}
	
	public String changeMail() throws Exception{
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		authentificationBS.changeMail(user, mail);
		return SUCCESS;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public IAuthentificationBS getAuthentificationBS() {
		return authentificationBS;
	}

	public void setAuthentificationBS(IAuthentificationBS authentificationBS) {
		this.authentificationBS = authentificationBS;
	}
	
	
	
	
	

}
