package com.mrprez.gencross.web.action;

import org.springframework.web.context.ContextLoader;

import com.mrprez.gencross.web.action.util.SessionUtil;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.web.bs.face.ITableBS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class BackgroundAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private Integer personnageId;
	private PersonnageWorkBO personnageWork;
	private String background;
	
	
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
		background = personnageWork.getBackground();
		return INPUT;
	}
	
	public String save() throws Exception {
		IPersonnageBS personnageBS = (IPersonnageBS)ContextLoader.getCurrentWebApplicationContext().getBean("personnageBS");
		personnageWork = SessionUtil.getPersonnageInSession(personnageId);
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
	
	
}
