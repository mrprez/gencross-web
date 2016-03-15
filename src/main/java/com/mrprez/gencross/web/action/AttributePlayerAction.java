package com.mrprez.gencross.web.action;

import java.util.List;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class AttributePlayerAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private static final String NO_PLAYER_KEY = "_no_player_";
	
	private IPersonnageBS personnageBS;
	private IAuthentificationBS authentificationBS;
	
	private Integer personnageId;
	private Integer tableId;
	private PersonnageWorkBO personnageWork;
	private String newPlayerName;
	
	
	
	public String execute() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnageAsGameMaster(personnageId, user);
		if(personnageWork==null){
			return ERROR;
		}
		
		return INPUT;
	}
	
	public String attribute() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnageAsGameMaster(personnageId, user);
		if(personnageWork==null){
			return ERROR;
		}
		
		if(newPlayerName.equals(NO_PLAYER_KEY)){
			personnageBS.attribute(personnageWork, null, personnageWork.getGameMaster());
		}else{
			UserBO newPlayer = authentificationBS.getUser(newPlayerName);
			if(newPlayer==null){
				return ERROR;
			}
			
			personnageBS.attribute(personnageWork, newPlayer, personnageWork.getGameMaster());
		}
		
		return SUCCESS;
	}
	
	
	public List<UserBO> getUserList() throws Exception{
		return authentificationBS.getUserList();
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
	public String getNewPlayerName() {
		return newPlayerName;
	}
	public void setNewPlayerName(String newPlayerName) {
		this.newPlayerName = newPlayerName;
	}
	public Integer getTableId() {
		return tableId;
	}
	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}
	public static String getNoPlayerKey() {
		return NO_PLAYER_KEY;
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
