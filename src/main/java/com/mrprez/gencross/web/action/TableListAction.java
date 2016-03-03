package com.mrprez.gencross.web.action;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import com.mrprez.gencross.disk.PluginDescriptor;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.web.bs.face.ITableBS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class TableListAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private String tableName;
	private String tableType;
	private Collection<PluginDescriptor> tableTypeList;
	private Collection<TableBO> tableList;
	private Integer tableId;
	private String personnageDeletion;
	
	private ITableBS tableBS;
	private IPersonnageBS personnageBS;
	
	
	public String addTable() throws Exception {
		if(StringUtils.isBlank(tableName)){
			super.addActionError("Le nom de la table à créer ne doit pas être vide");
			execute();
			return ERROR;
		}
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		tableBS.createTable(tableName, user, tableType);
		
		return SUCCESS;
	}
	
	public String execute() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		tableTypeList = personnageBS.getAvailablePersonnageTypes();
		tableList = tableBS.getTableListForUser(user);
		
		return INPUT;
	}
	
	public String removeTable() throws Exception{
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		PersonnageDeleteOption personnageDelete = PersonnageDeleteOption.valueOf(personnageDeletion);
		tableBS.removeTable(tableId, personnageDelete.deletePj, personnageDelete.deletePnj, user);
		
		return SUCCESS;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public Collection<PluginDescriptor> getTableTypeList() {
		return tableTypeList;
	}

	public void setTableTypeList(Collection<PluginDescriptor> tableTypeList) {
		this.tableTypeList = tableTypeList;
	}

	public Collection<TableBO> getTableList() {
		return tableList;
	}

	public void setTableList(Collection<TableBO> tableList) {
		this.tableList = tableList;
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

	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

	public String getPersonnageDeletion() {
		return personnageDeletion;
	}

	public void setPersonnageDeletion(String personnageDeletion) {
		this.personnageDeletion = personnageDeletion;
	}

	public static enum PersonnageDeleteOption{
		NONE(false, false, "Aucun personnage"),
		ONLY_PJ(true, false, "Seulement les PJ"),
		ONLY_PNJ(false, true, "Seulement les PNJ"),
		ALL(true, true, "Tous les personnages");
		
		private boolean deletePj;
		private boolean deletePnj;
		private String text;
		
		PersonnageDeleteOption(boolean deletePj, boolean deletePnj, String text){
			this.deletePj = deletePj;
			this.deletePnj = deletePnj;
			this.text = text;
		}

		public boolean isDeletePj() {
			return deletePj;
		}
		public boolean isDeletePnj() {
			return deletePnj;
		}
		public String getText() {
			return text;
		}
	}
	
}
