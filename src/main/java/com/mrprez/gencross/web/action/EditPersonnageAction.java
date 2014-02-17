package com.mrprez.gencross.web.action;

import java.io.InputStream;

import org.springframework.web.context.ContextLoader;

import com.mrprez.gencross.web.action.util.SessionUtil;
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
	
	
	public String execute() throws Exception {
		personnageWork = SessionUtil.getPersonnageInSession(personnageId);
		if(personnageWork==null){
			IPersonnageBS personnageBS = (IPersonnageBS)ContextLoader.getCurrentWebApplicationContext().getBean("personnageBS");
			UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
			personnageWork = personnageBS.loadPersonnage(personnageId, user);
			ITableBS tableBS = (ITableBS)ContextLoader.getCurrentWebApplicationContext().getBean("tableBS");
			tableBS.getPersonnageTable(personnageWork);
			SessionUtil.putPersonnageWorkInSession(personnageWork);
		}
		return INPUT;
	}
	
	
	
	public String nextPhase() throws Exception {
		personnageWork = SessionUtil.getPersonnageInSession(personnageId);
		if(personnageWork==null){
			addActionError("Vous devez sélectionner un personnage avant de le modifier.");
			return ERROR;
		}
		
		IPersonnageBS personnageBS = (IPersonnageBS)ContextLoader.getCurrentWebApplicationContext().getBean("personnageBS");
		personnageBS.nextPhase(personnageWork);
		
		return INPUT;
	}
	
	public String save() throws Exception {
		personnageWork = SessionUtil.getPersonnageInSession(personnageId);
		if(personnageWork==null){
			addActionError("Vous devez sélectionner un personnage avant de le modifier.");
			return ERROR;
		}
		IPersonnageBS personnageBS = (IPersonnageBS)ContextLoader.getCurrentWebApplicationContext().getBean("personnageBS");
		personnageBS.savePersonnage(personnageWork);
		return INPUT;
	}
	
	public String validatePersonnage() throws Exception {
		personnageWork = SessionUtil.getPersonnageInSession(personnageId);
		if(personnageWork==null){
			addActionError("Vous devez sélectionner un personnage avant de le modifier.");
			return ERROR;
		}
		IPersonnageBS personnageBS = (IPersonnageBS)ContextLoader.getCurrentWebApplicationContext().getBean("personnageBS");
		personnageBS.validatePersonnage(personnageWork);
		return INPUT;
	}
	
	public String unvalidatePersonnage() throws Exception {
		personnageWork = SessionUtil.getPersonnageInSession(personnageId);
		if(personnageWork==null){
			addActionError("Vous devez sélectionner un personnage avant de le modifier.");
			return ERROR;
		}
		IPersonnageBS personnageBS = (IPersonnageBS)ContextLoader.getCurrentWebApplicationContext().getBean("personnageBS");
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
	
	
	
	
}
