package com.mrprez.gencross.web.action;

import java.util.Collection;

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
	
	private ITableBS tableBS;
	private IPersonnageBS personnageBS;
	
	
	public String addTable() throws Exception {
		if(tableName==null || tableName.isEmpty()){
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

	
}
