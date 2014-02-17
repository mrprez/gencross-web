package com.mrprez.gencross.web.action;

import org.springframework.web.context.ContextLoader;

import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class ChangePasswordAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private String password;
	private String newPassword;
	private String confirm;
	
	
	public String changePassword() throws Exception{
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		IAuthentificationBS authentificationBS = (IAuthentificationBS)ContextLoader.getCurrentWebApplicationContext().getBean("authentificationBS");
		if(authentificationBS.authentificateUser(user.getUsername(), password)==null){
			this.addActionError("Ancien mot de passe incorrect");
			return ERROR;
		}
		authentificationBS.changePassword(user, newPassword);
		
		return SUCCESS;
	}
	
	public String execute() throws Exception{
		return INPUT;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getConfirm() {
		return confirm;
	}
	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}
	public String getSuccessMessage(){
		return "Votre mot de passe a été changé.";
	}
	

}

