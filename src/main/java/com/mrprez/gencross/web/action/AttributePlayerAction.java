package com.mrprez.gencross.web.action;

import java.util.List;

import org.springframework.web.context.ContextLoader;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class AttributePlayerAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private Integer personnageId;
	private Integer tableId;
	private PersonnageWorkBO personnageWork;
	private String newPlayerName;
	private String successMessage;
	private String successLink = "List";
	private String successLinkLabel = "Retourner à la liste des Personnages.";
	private static final String NO_PLAYER_KEY = "_no_player_";
	
	
	public String execute() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		IPersonnageBS personnageBS = (IPersonnageBS)ContextLoader.getCurrentWebApplicationContext().getBean("personnageBS");
		personnageWork = personnageBS.loadPersonnageAsGameMaster(personnageId, user);
		if(personnageWork==null){
			return ERROR;
		}
		
		return INPUT;
	}
	
	public String attribute() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		IPersonnageBS personnageBS = (IPersonnageBS)ContextLoader.getCurrentWebApplicationContext().getBean("personnageBS");
		personnageWork = personnageBS.loadPersonnageAsGameMaster(personnageId, user);
		if(personnageWork==null){
			return ERROR;
		}
		
		if(newPlayerName.equals(NO_PLAYER_KEY)){
			personnageBS.attribute(personnageWork, null, personnageWork.getGameMaster());
			successMessage = "Le personnage n'est plus associé à aucun joueur.";
		}else{
			IAuthentificationBS authentificationBS = (IAuthentificationBS)ContextLoader.getCurrentWebApplicationContext().getBean("authentificationBS");
			UserBO newPlayer = authentificationBS.getUser(newPlayerName);
			if(newPlayer==null){
				return ERROR;
			}
			
			personnageBS.attribute(personnageWork, newPlayer, personnageWork.getGameMaster());
			successMessage = "Le personnage a été attribué à: "+newPlayer.getUsername()+".";
		}
		
		return SUCCESS;
	}
	
	
	public List<UserBO> getUserList() throws Exception{
		IAuthentificationBS authentificationBS = (IAuthentificationBS)ContextLoader.getCurrentWebApplicationContext().getBean("authentificationBS");
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
	public String getSuccessMessage() {
		return successMessage;
	}
	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}
	public String getSuccessLink() {
		return successLink;
	}
	public void setSuccessLink(String successLink) {
		this.successLink = successLink;
	}
	public String getSuccessLinkLabel() {
		return successLinkLabel;
	}
	public void setSuccessLinkLabel(String successLinkLabel) {
		this.successLinkLabel = successLinkLabel;
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
	

}
