package com.mrprez.gencross.web.action;

import java.util.Collection;

import org.apache.struts2.ServletActionContext;

import com.mrprez.gencross.web.bo.PlannedGameBO;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IPlanGameBS;
import com.mrprez.gencross.web.bs.face.ITableBS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class PlanGameAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private Integer tableId;
	private TableBO table;
	private Collection<PlannedGameBO> plannedGamesList;
	
	private IPlanGameBS planGameBS;
	private ITableBS tableBS;
	
	
	public String execute() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		table = tableBS.getTableForGM(tableId, user);
		return INPUT;
	}
	
	public String loadPlannedGames() throws Exception {
		plannedGamesList = planGameBS.getPlannedGames(tableId);
		ServletActionContext.getResponse().setHeader("Cache-Control", "no-cache, no-store");
		return "jsonPlannedGames";
	}
	
	
	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}
	
	public Collection<PlannedGameBO> getPlannedGamesList(){
		return plannedGamesList;
	}

	public TableBO getTable() {
		return table;
	}

	public void setTableBS(TableBO table) {
		this.table = table;
	}
	
	public IPlanGameBS getPlanGameBS() {
		return planGameBS;
	}

	public void setPlanGameBS(IPlanGameBS planGameBS) {
		this.planGameBS = planGameBS;
	}

	public ITableBS getTableBS() {
		return tableBS;
	}

	public void setTableBS(ITableBS tableBS) {
		this.tableBS = tableBS;
	}
	
}
