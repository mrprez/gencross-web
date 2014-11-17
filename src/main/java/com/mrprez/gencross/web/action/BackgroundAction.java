package com.mrprez.gencross.web.action;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.web.bs.face.ITableBS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class BackgroundAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private IPersonnageBS personnageBS;
	private ITableBS tableBS;
	
	private Integer personnageId;
	private PersonnageWorkBO personnageWork;
	private String background;
	
	
	public String execute() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnage(personnageId, user);
		background = personnageWork.getBackground();
		return INPUT;
	}
	
	public String save() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnage(personnageId, user);
		personnageBS.savePersonnageBackground(personnageWork, background);
		
		return SUCCESS;
	}

	public boolean getIsGameMaster(){
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		if(personnageWork.getGameMaster()==null){
			return false;
		}
		return user.getUsername().equals(personnageWork.getGameMaster().getUsername());
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

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
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
