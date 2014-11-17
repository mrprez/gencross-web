package com.mrprez.gencross.web.action;

import java.io.InputStream;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.web.bs.face.ITableBS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class EditPersonnageAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private InputStream helpFileInputStream;
	private Integer personnageId;
	private PersonnageWorkBO personnageWork;
	
	private IPersonnageBS personnageBS;
	private ITableBS tableBS;
	
	
	public String execute() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnage(personnageId, user);
		return INPUT;
	}
	
	
	
	public String nextPhase() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnage(personnageId, user);
		if(personnageWork==null){
			addActionError("Vous devez sélectionner un personnage avant de le modifier.");
			return ERROR;
		}
		personnageBS.nextPhase(personnageWork);
		
		return SUCCESS;
	}
	
	public String save() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnage(personnageId, user);
		if(personnageWork==null){
			addActionError("Vous devez sélectionner un personnage avant de le modifier.");
			return ERROR;
		}
		personnageBS.savePersonnage(personnageWork);
		return INPUT;
	}
	
	public String validatePersonnage() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnage(personnageId, user);
		if(personnageWork==null){
			addActionError("Vous devez sélectionner un personnage avant de le modifier.");
			return ERROR;
		}
		personnageBS.validatePersonnage(personnageWork);
		return INPUT;
	}
	
	public String unvalidatePersonnage() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnage(personnageId, user);
		if(personnageWork==null){
			addActionError("Vous devez sélectionner un personnage avant de le modifier.");
			return ERROR;
		}
		personnageBS.unvalidatePersonnage(personnageWork);
		return INPUT;
	}

	public InputStream getHelpFileInputStream() {
		return helpFileInputStream;
	}
	public void setHelpFileInputStream(InputStream helpFileInputStream) {
		this.helpFileInputStream = helpFileInputStream;
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

	public boolean getIsGameMaster(){
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		if(personnageWork.getGameMaster()==null){
			return false;
		}
		return user.getUsername().equals(personnageWork.getGameMaster().getUsername());
	}

	public IPersonnageBS getPersonnageBS() {
		return personnageBS;
	}
	public void setPersonnageBS(IPersonnageBS personnageBS) {
		this.personnageBS = personnageBS;
	}
	public ITableBS getTableBS() {
		return tableBS;
	}
	public void setTableBS(ITableBS tableBS) {
		this.tableBS = tableBS;
	}
	
	
}
