package com.mrprez.gencross.web.action;

import java.util.List;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class AttributeGameMasterAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	public static final String NO_GM_KEY = "_no_gm_";
	
	private IPersonnageBS personnageBS;
	private IAuthentificationBS authentificationBS;
	
	private Integer personnageId;
	private PersonnageWorkBO personnageWork;
	private String newGameMasterName;
	
	
	public String execute() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnageAsPlayer(personnageId, user);
		if(personnageWork==null){
			return ERROR;
		}
		
		return INPUT;
	}
	
	public String attribute() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnageAsPlayer(personnageId, user);
		if(personnageWork==null){
			return ERROR;
		}
		
		if(newGameMasterName.equals(NO_GM_KEY)){
			personnageBS.attribute(personnageWork, personnageWork.getPlayer(), null);
		}else{
			UserBO newGameMaster = authentificationBS.getUser(newGameMasterName);
			if(newGameMaster==null){
				return ERROR;
			}
			personnageBS.attribute(personnageWork, personnageWork.getPlayer(), newGameMaster);
		}
		
		return SUCCESS;
	}


	
	public Integer getPersonnageId() {
		return personnageId;
	}
	public void setPersonnageId(Integer personnageId) {
		this.personnageId = personnageId;
	}
	public PersonnageWorkBO getPersonnageWork() {
		return personnageWork;
	}
	public void setPersonnageWork(PersonnageWorkBO personnageWork) {
		this.personnageWork = personnageWork;
	}
	public List<UserBO> getUserList() throws Exception{
		return authentificationBS.getUserList();
	}
	public String getNewGameMasterName() {
		return newGameMasterName;
	}
	public void setNewGameMasterName(String newGameMasterName) {
		this.newGameMasterName = newGameMasterName;
	}
	public static String getNoGmKey() {
		return NO_GM_KEY;
	}
	public IPersonnageBS getPersonnageBS() {
		return personnageBS;
	}
	public void setPersonnageBS(IPersonnageBS personnageBS) {
		this.personnageBS = personnageBS;
	}
	public IAuthentificationBS getAuthentificationBS() {
		return authentificationBS;
	}
	public void setAuthentificationBS(IAuthentificationBS authentificationBS) {
		this.authentificationBS = authentificationBS;
	}

}
