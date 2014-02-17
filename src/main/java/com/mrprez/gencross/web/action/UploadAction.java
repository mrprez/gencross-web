package com.mrprez.gencross.web.action;

import java.io.File;

import org.springframework.web.context.ContextLoader;

import com.mrprez.gencross.web.action.util.SessionUtil;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IGcrFileBS;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class UploadAction extends ActionSupport {
	private static final long serialVersionUID = 1L;

	private File gcrFile;
	private String personnageName;
	private boolean gm;
	private String password;
	private Integer personnageId;
	
	
	@Override
	public String execute() throws Exception {
		if(personnageId!=null){
			UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
			IPersonnageBS personnageBS = (IPersonnageBS) ContextLoader.getCurrentWebApplicationContext().getBean("personnageBS");
			PersonnageWorkBO personnageWork = personnageBS.loadPersonnageAsGameMaster(personnageId, user);
			gm = (personnageWork!=null);
			if(!gm){
				personnageWork = personnageBS.loadPersonnageAsPlayer(personnageId, user);
			}
			personnageName = personnageWork.getName();
		}
		return INPUT;
	}
	
	public String upload() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		
		IGcrFileBS gcrFileBS = (IGcrFileBS)ContextLoader.getCurrentWebApplicationContext().getBean("gcrFileBS");
		if(personnageId==null){
			if(gm){
				PersonnageWorkBO personnageWork = gcrFileBS.createPersonnageAsGameMaster(gcrFile, personnageName, user, password);
				if(personnageWork==null){
					this.addActionError("Mot de passe du MJ erroné.");
					return ERROR;
				}
				personnageId = personnageWork.getId();
			}else{
				PersonnageWorkBO personnageWork = gcrFileBS.createPersonnageAsPlayer(gcrFile, personnageName, user);
				SessionUtil.putPersonnageWorkInSession(personnageWork);
				personnageId = personnageWork.getId();
			}
		}else{
			SessionUtil.removePersonnageWorkFromSession(personnageId);
			String error = gcrFileBS.uploadGcrPersonnage(gcrFile, personnageId, user, password);
			if(error!=null){
				this.addActionError(error);
				execute();
				return ERROR;
			}
		}
		return SUCCESS;
	}

	
	public void setGcrFile(File gcrFile) {
		this.gcrFile = gcrFile;
	}

	public void setPersonnageName(String personnageName) {
		this.personnageName = personnageName;
	}

	public String getPersonnageName() {
		return personnageName;
	}

	public void setGm(boolean gm) {
		this.gm = gm;
	}
	
	public boolean isGm() {
		return gm;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getPersonnageId() {
		return personnageId;
	}

	public void setPersonnageId(Integer personnageId) {
		this.personnageId = personnageId;
	}
	
	

}
