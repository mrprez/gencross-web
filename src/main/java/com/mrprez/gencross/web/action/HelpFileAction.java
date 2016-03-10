package com.mrprez.gencross.web.action;

import java.io.InputStream;
import java.util.Map;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class HelpFileAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private String helpFileName;
	private Long fileLength;
	private InputStream helpFileInputStream;
	private Integer personnageWorkId;
	

	@Override
	public String execute() throws Exception {
		PersonnageWorkBO personnageWork = getPersonnageWorkInSession();
		if(personnageWork==null){
			return ERROR;
		}
		if(!personnageWork.getPersonnage().hasHelpFile()){
			addActionError("Pas de fichier d'aide pour ce type de personnage.");
			return ERROR;
		}
		fileLength = personnageWork.getPersonnage().getPluginDescriptor().getHelpFileSize();
		helpFileInputStream = personnageWork.getPersonnage().getHelpFileInputStream();
		helpFileName= personnageWork.getPersonnage().getPluginDescriptor().getHelpFileName();
		
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	private PersonnageWorkBO getPersonnageWorkInSession(){
		if(!ActionContext.getContext().getSession().containsKey("personnagesWorks")){
			addActionError("Session invalide.");
			return null;
		}
		PersonnageWorkBO personnageWork = ((Map<Integer, PersonnageWorkBO>)ActionContext.getContext().getSession().get("personnagesWorks")).get(personnageWorkId);
		if(personnageWork==null){
			addActionError("Session invalide.");
			return null;
		}
		return personnageWork;
	}
	
	public String getContentDisposition(){
		return "attachment;filename=\""+helpFileName+"\"";
	}
	
	public String getContentLength(){
		return fileLength.toString();
	}

	public InputStream getHelpFileInputStream() {
		return helpFileInputStream;
	}

	public Integer getPersonnageWorkId() {
		return personnageWorkId;
	}

	public void setPersonnageWorkId(Integer personnageWorkId) {
		this.personnageWorkId = personnageWorkId;
	}
	

}
