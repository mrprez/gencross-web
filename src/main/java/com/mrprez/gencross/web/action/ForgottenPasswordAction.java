package com.mrprez.gencross.web.action;

import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.opensymphony.xwork2.ActionSupport;

public class ForgottenPasswordAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String successMessage;
	private String successLink;
	private String successLinkLabel;
	
	private IAuthentificationBS authentificationBS;
	
	
	@Override
	public String execute() throws Exception {
		if(username==null){
			return INPUT;
		}
		UserBO user = authentificationBS.sendPassword(username);
		if(user==null){
			this.addActionError("Login incorrect");
			return ERROR;
		}
		successMessage = "Un nouveau mot de passe vous a été envoyé à l'adresse: "+user.getMail();
		successLink = "Login";
		successLinkLabel = "Retour";
		return SUCCESS;
	}


	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSuccessMessage(){
		return successMessage;
	}
	public String getSuccessLink() {
		return successLink;
	}
	public String getSuccessLinkLabel() {
		return successLinkLabel;
	}
	public IAuthentificationBS getAuthentificationBS() {
		return authentificationBS;
	}
	public void setAuthentificationBS(IAuthentificationBS authentificationBS) {
		this.authentificationBS = authentificationBS;
	}
	
	
	
}
