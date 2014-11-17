package com.mrprez.gencross.web.action;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.history.HistoryItem;
import com.mrprez.gencross.util.ReversedListIterator;
import com.mrprez.gencross.web.action.util.SessionUtil;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.web.bs.face.IPersonnageComparatorBS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class EditPersonnageAjaxAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private String newValue;
	private String propertyAbsoluteName;
	private Set<String> updatedProperties;
	private boolean reloadErrors;
	private boolean reloadHistory;
	private Set<Integer> updatedPointPoolIndexes;
	private Integer pointPoolIndex;
	private String pointPoolName;
	private String actionMessage;
	private String propertyNum;
	private Boolean phaseFinished;
	private String motherProperty;
	private String optionProperty;
	private String specification;
	private String newProperty;
	private Integer historyLastIndex;
	private String comment;
	private Integer pointPoolModification;
	private Integer historyIndex;
	private Integer cost;
	private Set<Integer> changedHistory;
	private List<HistoryItem> newHistory;
	private Integer personnageWorkId;
	private PersonnageWorkBO personnageWork;
	
	private IPersonnageBS personnageBS;
	private IPersonnageComparatorBS personnageComparatorBS;
	
	
	public String expand() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnage(personnageWorkId, user);
		if(personnageWork==null){
			return null;
		}
		SessionUtil.addExpandingInSession(personnageWorkId, propertyAbsoluteName);
		return null;
	}
	
	public String collapse() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnage(personnageWorkId, user);
		if(personnageWork==null){
			return null;
		}
		SessionUtil.removeExpandingFromSession(personnageWorkId, propertyAbsoluteName);
		return null;
	}

	public String updateValue() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnage(personnageWorkId, user);
		if(personnageWork==null){
			Logger.getLogger(getClass()).warn("Personnage not in session (personnageWorkId="+personnageWorkId+", user="+ActionContext.getContext().getSession().get("user")+")");
			return ERROR;
		}
		Personnage personnageRef = personnageWork.getPersonnage().clone();
		personnageBS.setNewValue(personnageWork, newValue, propertyAbsoluteName);
		loadDifferences(personnageWork.getPersonnage(), personnageRef);
		
		return INPUT;
	}
	
	public String addPropertyFromOption() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnage(personnageWorkId, user);
		if(personnageWork==null){
			Logger.getLogger(getClass()).warn("Personnage not in session (personnageWorkId="+personnageWorkId+", user="+ActionContext.getContext().getSession().get("user")+")");
			return ERROR;
		}
		Personnage personnageRef = personnageWork.getPersonnage().clone();
		personnageBS.addPropertyFromOption(personnageWork, motherProperty, optionProperty, specification);
		loadDifferences(personnageWork.getPersonnage(), personnageRef);
		
		return INPUT;
	}
	
	public String addFreeProperty() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnage(personnageWorkId, user);
		if(personnageWork==null){
			Logger.getLogger(getClass()).warn("Personnage not in session (personnageWorkId="+personnageWorkId+", user="+ActionContext.getContext().getSession().get("user")+")");
			return ERROR;
		}
		Personnage personnageRef = personnageWork.getPersonnage().clone();
		personnageBS.addFreeProperty(personnageWork, motherProperty, newProperty);
		loadDifferences(personnageWork.getPersonnage(), personnageRef);
		
		return INPUT;
	}
	
	public String removeProperty() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnage(personnageWorkId, user);
		if(personnageWork==null){
			Logger.getLogger(getClass()).warn("Personnage not in session (personnageWorkId="+personnageWorkId+", user="+ActionContext.getContext().getSession().get("user")+")");
			return ERROR;
		}
		Personnage personnageRef = personnageWork.getPersonnage().clone();
		personnageBS.removeProperty(personnageWork, propertyAbsoluteName);
		loadDifferences(personnageWork.getPersonnage(), personnageRef);
		
		return INPUT;
	}
	
	private void loadDifferences(Personnage personnage, Personnage personnageRef) throws Exception{
		updatedProperties = personnageComparatorBS.findPropertiesDifferences(personnage, personnageRef);
		reloadErrors = !personnageComparatorBS.hasTheSameErrors(personnage, personnageRef);
		updatedPointPoolIndexes = personnageComparatorBS.findPointPoolDifferences(personnage, personnageRef);
		actionMessage = personnage.getActionMessage();
		personnage.clearActionMessage();
		if(!personnageComparatorBS.hasTheSameNextPhaseAvaibility(personnage, personnageRef)){
			phaseFinished = personnage.phaseFinished();
		}
		reloadHistory = personnage.getHistory().size()!=personnageRef.getHistory().size();
	}
	
	
	public String getProperty() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnage(personnageWorkId, user);
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
		personnageWork = personnageBS.loadPersonnage(personnageWorkId, user);
		if(personnageWork==null){
			return ERROR;
		}
		ActionContext.getContext().getValueStack().push(personnageWork.getPersonnage());
		
		return "errorList";
	}
	
	public String getPointPool() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnage(personnageWorkId, user);
		if(personnageWork==null){
			return ERROR;
		}
		ActionContext.getContext().getValueStack().push(personnageWork.getPersonnage().getPointPools().values().toArray()[pointPoolIndex]);
		ActionContext.getContext().getValueStack().set("index", ""+pointPoolIndex);
		return "pointPool";
	}

	public String getLastHistory() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnage(personnageWorkId, user);
		if(personnageWork==null){
			return ERROR;
		}
		newHistory = personnageWork.getPersonnage().getHistory().subList(historyLastIndex+1, personnageWork.getPersonnage().getHistory().size());
		return "history";
	}
	
	public String changeComment() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnage(personnageWorkId, user);
		if(personnageWork==null){
			return ERROR;
		}
		personnageBS.changeComment(personnageWork, propertyAbsoluteName, comment);
		updatedProperties = new HashSet<String>(1);
		updatedProperties.add(propertyAbsoluteName);
		
		return INPUT;
	}
	
	public String modifyPointPool() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnageAsGameMaster(personnageWorkId, user);
		if(personnageWork==null){
			return ERROR;
		}
		personnageBS.modifyPointPool(personnageWork, pointPoolName, pointPoolModification);
		updatedPointPoolIndexes = new HashSet<Integer>(1);
		int index = Arrays.asList(personnageWork.getPersonnage().getPointPools().keySet().toArray()).indexOf(pointPoolName);
		updatedPointPoolIndexes.add(new Integer(index));
		return INPUT;
	}
	
	public String modifyHistory() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnageAsGameMaster(personnageWorkId, user);
		if(personnageWork==null){
			return ERROR;
		}
		updatedPointPoolIndexes = new HashSet<Integer>(2);
		String oldPointPoolName = personnageWork.getPersonnage().getHistory().get(historyIndex).getPointPool();
		updatedPointPoolIndexes.add(Arrays.asList(personnageWork.getPersonnage().getPointPools().keySet().toArray()).indexOf(oldPointPoolName));
		updatedPointPoolIndexes.add(Arrays.asList(personnageWork.getPersonnage().getPointPools().keySet().toArray()).indexOf(pointPoolName));
		personnageBS.modifyHistory(personnageWork, pointPoolName, cost, historyIndex);
		changedHistory = new HashSet<Integer>();
		changedHistory.add(historyIndex);
		return INPUT;
	}
	
	public String getHistoryItem() throws Exception{
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageWork = personnageBS.loadPersonnage(personnageWorkId, user);
		if(personnageWork==null){
			return ERROR;
		}
		
		ActionContext.getContext().getValueStack().push(personnageWork.getPersonnage());
		ActionContext.getContext().getValueStack().push(personnageWork.getPersonnage().getHistory().get(historyIndex));
		ActionContext.getContext().getValueStack().set("index", ""+historyIndex);
		
		return "historyItem";
	}
	
	public boolean getIsGameMaster(){
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		return isUserPersonnageGameMaster(user, personnageWork);
	}
	public boolean isUserPersonnageGameMaster(UserBO user, PersonnageWorkBO personnageWork){
		if(personnageWork.getGameMaster()==null){
			return false;
		}
		return personnageWork.getGameMaster().getUsername().equals(user.getUsername());
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	public String getPropertyAbsoluteName() {
		return propertyAbsoluteName;
	}
	public void setPropertyAbsoluteName(String propertyAbsoluteName) {
		this.propertyAbsoluteName = propertyAbsoluteName;
	}
	public Set<String> getUpdatedProperties() {
		return updatedProperties;
	}
	public void setUpdatedProperties(Set<String> updatedProperties) {
		this.updatedProperties = updatedProperties;
	}
	public boolean isReloadErrors() {
		return reloadErrors;
	}
	public void setReloadErrors(boolean reloadErrors) {
		this.reloadErrors = reloadErrors;
	}
	public Set<Integer> getUpdatedPointPoolIndexes() {
		return updatedPointPoolIndexes;
	}
	public void setUpdatedPointPoolIndexes(Set<Integer> updatedPointPoolIndexes) {
		this.updatedPointPoolIndexes = updatedPointPoolIndexes;
	}
	public String getPointPoolName() {
		return pointPoolName;
	}
	public void setPointPoolName(String pointPoolName) {
		this.pointPoolName = pointPoolName;
	}
	public Integer getPointPoolIndex() {
		return pointPoolIndex;
	}
	public void setPointPoolIndex(Integer pointPoolIndex) {
		this.pointPoolIndex = pointPoolIndex;
	}
	public String getActionMessage() {
		return actionMessage;
	}
	public void setActionMessage(String actionMessage) {
		this.actionMessage = actionMessage;
	}
	public String getPropertyNum() {
		return propertyNum;
	}
	public void setPropertyNum(String propertyNum) {
		this.propertyNum = propertyNum;
	}
	public Boolean getPhaseFinished() {
		return phaseFinished;
	}
	public void setPhaseFinished(Boolean phaseFinished) {
		this.phaseFinished = phaseFinished;
	}
	public String getMotherProperty() {
		return motherProperty;
	}
	public void setMotherProperty(String motherProperty) {
		this.motherProperty = motherProperty;
	}
	public String getOptionProperty() {
		return optionProperty;
	}
	public void setOptionProperty(String optionProperty) {
		this.optionProperty = optionProperty;
	}
	public String getNewProperty() {
		return newProperty;
	}
	public void setNewProperty(String newProperty) {
		this.newProperty = newProperty;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public Integer getHistoryLastIndex() {
		return historyLastIndex;
	}
	public void setHistoryLastIndex(Integer historyLastIndex) {
		this.historyLastIndex = historyLastIndex;
	}
	public boolean isReloadHistory() {
		return reloadHistory;
	}
	public void setReloadHistory(boolean reloadHistory) {
		this.reloadHistory = reloadHistory;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Integer getPointPoolModification() {
		return pointPoolModification;
	}
	public void setPointPoolModification(Integer pointPoolModification) {
		this.pointPoolModification = pointPoolModification;
	}
	public Integer getHistoryIndex() {
		return historyIndex;
	}
	public void setHistoryIndex(Integer historyIndex) {
		this.historyIndex = historyIndex;
	}
	public Integer getCost() {
		return cost;
	}
	public void setCost(Integer cost) {
		this.cost = cost;
	}
	public Set<Integer> getChangedHistory() {
		return changedHistory;
	}
	public ListIterator<HistoryItem> getNewHistoryReversedIterator() {
		return new ReversedListIterator<HistoryItem>(newHistory);
	}
	public Integer getPersonnageWorkId() {
		return personnageWorkId;
	}
	public void setPersonnageWorkId(Integer personnageWorkId) {
		this.personnageWorkId = personnageWorkId;
	}
	public PersonnageWorkBO getPersonnageWork() {
		return personnageWork;
	}
	public void setPersonnageWork(PersonnageWorkBO personnageWork) {
		this.personnageWork = personnageWork;
	}

	public IPersonnageBS getPersonnageBS() {
		return personnageBS;
	}

	public void setPersonnageBS(IPersonnageBS personnageBS) {
		this.personnageBS = personnageBS;
	}

	public IPersonnageComparatorBS getPersonnageComparatorBS() {
		return personnageComparatorBS;
	}

	public void setPersonnageComparatorBS(IPersonnageComparatorBS personnageComparatorBS) {
		this.personnageComparatorBS = personnageComparatorBS;
	}
	
	
}
