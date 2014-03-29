package com.mrprez.gencross.web.action;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import com.mrprez.gencross.web.action.util.PersonnageWorkComparator;
import com.mrprez.gencross.web.action.util.SessionUtil;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.web.bs.face.ITableBS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class EditTableAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private TableBO table;
	private String personnageName;
	private Integer personnageId;
	private Collection<PersonnageWorkBO> pjList;
	private Collection<PersonnageWorkBO> pnjList;
	private Collection<PersonnageWorkBO> addablePersonnage;
	private Map<String, Integer> minPjPoints;
	private Map<String, Integer> maxPjPoints;
	private Map<String, Integer> minPnjPoints;
	private Map<String, Integer> maxPnjPoints;
	private Set<String> poolPointList;
	private String pointPoolName;
	private Integer pointPoolModification;
	private PersonnageWorkComparator personnageWorkComparator = new PersonnageWorkComparator();
	private String message;
	private Integer messageId;
	private String sendMessage;
	private String addMessage;
	
	private ITableBS tableBS;
	private IPersonnageBS personnageBS;
	
	
	@Override
	public String execute() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		table = tableBS.getTableForGM(id, user);
		if(table==null){
			addActionError("Impossible de charger cette table");
			return ERROR;
		}
		
		pjList = new TreeSet<PersonnageWorkBO>(personnageWorkComparator);
		pnjList = new TreeSet<PersonnageWorkBO>(personnageWorkComparator);
		for(PersonnageWorkBO personnageWork : table.getPersonnages()){
			if(personnageWork.getPlayer()!=null){
				pjList.add(personnageWork);
			}else{
				pnjList.add(personnageWork);
			}
		}

		addablePersonnage = tableBS.getAddablePersonnages(table);
		
		return INPUT;
	}
	
	public String addPersonnage() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		if(StringUtils.isBlank(personnageName)){
			addActionError("Veuillez renseigner un nom pour ce personnage.");
			return execute();
		}
		
		table = tableBS.addNewPersonnageToTable(id, personnageName, user);
		if(table==null){
			addActionError("Impossible de charger cette table");
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	public String addPoints() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		String error = tableBS.addPointsToPj(id, user, pointPoolName, pointPoolModification);
		if(error!=null){
			addActionError(error);
			return execute();
		}
		return SUCCESS;
	}
	
	public String transformInPNJ() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		PersonnageWorkBO personnageWork = personnageBS.loadPersonnageAsGameMaster(personnageId, user);
		if(personnageWork==null){
			addActionError("Vous n'êtes pas MJ de ce personnage.");
			return ERROR;
		}
		personnageBS.attribute(personnageWork, null, user);
		
		return SUCCESS;
	}
	
	public String bindPersonnage() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		if(personnageId == null){
			addActionError("Veillez selectionner un personnage à ajouter à la table.");
			return execute();
		}
		PersonnageWorkBO personnageWork = tableBS.addPersonnageToTable(id, personnageId, user);
		if(personnageWork==null){
			addActionError("Vous n'êtes pas MJ de ce personnage ou de cette table.");
			return ERROR;
		}
		if(SessionUtil.getPersonnageInSession(personnageId) != null){
			personnageWork.setPropertiesExpanding(SessionUtil.getPersonnageInSession(personnageId).getPropertiesExpanding());
		}
		SessionUtil.putPersonnageWorkInSession(personnageWork);
		return SUCCESS;
	}

	public String unbindPersonnage() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		PersonnageWorkBO personnageWork = tableBS.removePersonnageFromTable(id, personnageId, user);
		if(personnageWork==null){
			addActionError("Vous n'êtes pas MJ de ce personnage ou de cette table.");
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	public String newMessage() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		message = message.replaceAll("<.[^>]*>", "").trim();
		if(addMessage!=null){
			tableBS.addMessageToTable(message, id, user);
		}else if(sendMessage!=null){
			tableBS.addSendMessage(message, id, user);
		}
		
		return SUCCESS;
	}
	
	public String removeMessage() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		tableBS.removeMessageFromTable(messageId, id, user);
		
		return SUCCESS;
	}
	
	public String refreshMessages() throws Exception{
		tableBS.connectTableMailBox();
		return SUCCESS;
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public TableBO getTable() {
		return table;
	}
	public void setTable(TableBO table) {
		this.table = table;
	}
	public String getPersonnageName() {
		return personnageName;
	}
	public void setPersonnageName(String personnageName) {
		this.personnageName = personnageName;
	}
	public Collection<PersonnageWorkBO> getPjList() {
		return pjList;
	}
	public void setPjList(Collection<PersonnageWorkBO> pjList) {
		this.pjList = pjList;
	}
	public Collection<PersonnageWorkBO> getPnjList() {
		return pnjList;
	}
	public void setPnjList(Collection<PersonnageWorkBO> pnjList) {
		this.pnjList = pnjList;
	}
	public Map<String, Integer> getMinPjPoints() {
		return minPjPoints;
	}
	public void setMinPjPoints(Map<String, Integer> minPjPoints) {
		this.minPjPoints = minPjPoints;
	}
	public Map<String, Integer> getMaxPjPoints() {
		return maxPjPoints;
	}
	public void setMaxPjPoints(Map<String, Integer> maxPjPoints) {
		this.maxPjPoints = maxPjPoints;
	}
	public Map<String, Integer> getMinPnjPoints() {
		return minPnjPoints;
	}
	public void setMinPnjPoints(Map<String, Integer> minPnjPoints) {
		this.minPnjPoints = minPnjPoints;
	}
	public Map<String, Integer> getMaxPnjPoints() {
		return maxPnjPoints;
	}
	public void setMaxPnjPoints(Map<String, Integer> maxPnjPoints) {
		this.maxPnjPoints = maxPnjPoints;
	}
	public String getPointPoolName() {
		return pointPoolName;
	}
	public void setPointPoolName(String pointPoolName) {
		this.pointPoolName = pointPoolName;
	}
	public Integer getPointPoolModification() {
		return pointPoolModification;
	}
	public void setPointPoolModification(Integer pointPoolModification) {
		this.pointPoolModification = pointPoolModification;
	}
	public Set<String> getPoolPointList() {
		return poolPointList;
	}
	public void setPoolPointList(Set<String> poolPointList) {
		this.poolPointList = poolPointList;
	}
	public Integer getPersonnageId() {
		return personnageId;
	}
	public void setPersonnageId(Integer personnageId) {
		this.personnageId = personnageId;
	}
	public Collection<PersonnageWorkBO> getAddablePersonnage() {
		return addablePersonnage;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getMessageId() {
		return messageId;
	}
	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}
	public String getSendMessage() {
		return sendMessage;
	}
	public void setSendMessage(String sendMessage) {
		this.sendMessage = sendMessage;
	}
	public String getAddMessage() {
		return addMessage;
	}
	public void setAddMessage(String addMessage) {
		this.addMessage = addMessage;
	}

	public ITableBS getTableBS() {
		return tableBS;
	}

	public void setTableBS(ITableBS tableBS) {
		this.tableBS = tableBS;
	}

	public IPersonnageBS getPersonnageBS() {
		return personnageBS;
	}

	public void setPersonnageBS(IPersonnageBS personnageBS) {
		this.personnageBS = personnageBS;
	}


}
