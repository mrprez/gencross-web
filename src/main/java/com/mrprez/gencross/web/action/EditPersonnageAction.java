package com.mrprez.gencross.web.action;

import java.io.InputStream;

import org.apache.struts2.ServletActionContext;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.history.HistoryItem;
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
	private String propertyAbsoluteName;
	private String pointPoolName;
	private Integer historyItemIndex;
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
	
	public String getProperty() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnage(personnageId, user);
		if(personnageWork==null){
			return ERROR;
		}
		Personnage personnage = personnageWork.getPersonnage();
		Property property = personnage.getProperty(propertyAbsoluteName);
		String namesTab[] = propertyAbsoluteName.split("#");
		int index = 0;
		Property currentProperty = null;
		for(Property comparProperty : personnage.getProperties()){
			if(comparProperty.getFullName().equals(namesTab[0])){
				currentProperty = comparProperty;
				break;
			}else{
				index++;
			}
		}
		StringBuilder propertyNum = new StringBuilder(""+index);
		for(int i=1; i<namesTab.length; i++){
			index = 0;
			for(Property comparProperty : currentProperty.getSubProperties()){
				if(comparProperty.getFullName().equals(namesTab[i])){
					currentProperty = comparProperty;
					break;
				}else{
					index++;
				}
			}
			propertyNum.append("_"+index);
		}
		ServletActionContext.getRequest().setAttribute("propertyNum", propertyNum.toString());
		ActionContext.getContext().getValueStack().push(property);
		
		return "property";
	}
	
	public String getErrorList() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnage(personnageId, user);
		if(personnageWork==null){
			return ERROR;
		}
		ActionContext.getContext().getValueStack().push(personnageWork.getPersonnage());
		
		return "errorList";
	}
	
	public String getPointPool() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnage(personnageId, user);
		if(personnageWork==null){
			return ERROR;
		}
		ActionContext.getContext().getValueStack().push(personnageWork.getPersonnage().getPointPools().get(pointPoolName));
		return "pointPool";
	}
	
	public String getHistoryItem() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnage(personnageId, user);
		if(personnageWork==null){
			return ERROR;
		}
		
		HistoryItem historyItem = personnageWork.getPersonnage().getHistory().get(historyItemIndex);
		ActionContext.getContext().getValueStack().push(personnageWork.getPersonnage());
		ActionContext.getContext().getValueStack().push(historyItem);
		return "historyItem";
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
	public String getPropertyAbsoluteName() {
		return propertyAbsoluteName;
	}
	public void setPropertyAbsoluteName(String propertyAbsoluteName) {
		this.propertyAbsoluteName = propertyAbsoluteName;
	}
	public String getPointPoolName() {
		return pointPoolName;
	}
	public void setPointPoolName(String pointPoolName) {
		this.pointPoolName = pointPoolName;
	}
	public Integer getHistoryItemIndex() {
		return historyItemIndex;
	}
	public void setHistoryItemIndex(Integer historyItemIndex) {
		this.historyItemIndex = historyItemIndex;
	}
	
	
}
