package com.mrprez.gencross.web.action;

import java.util.Date;

import org.apache.log4j.Logger;

import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private String password;
	private String username;

	private IAuthentificationBS authentificationBS;
	
	
	public String execute() throws Exception {
		if(username==null && password==null){
			return INPUT;
		}
		UserBO user = authentificationBS.authentificateUser(username, password);
		if(user==null){
			this.addActionError("Login ou mot de passe incorrect");
			return ERROR;
		}
		ActionContext.getContext().getSession().put("user", user);
		ActionContext.getContext().getSession().put("loginDate", new Date());
		Logger.getLogger(getClass()).info("User connected: "+user.getUsername());
		return SUCCESS;
	}
	
	public String disconnect() throws Exception {
		ActionContext.getContext().getSession().clear();
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
	public IAuthentificationBS getAuthentificationBS() {
		return authentificationBS;
	}
	public void setAuthentificationBS(IAuthentificationBS authentificationBS) {
		this.authentificationBS = authentificationBS;
	}
	

}
