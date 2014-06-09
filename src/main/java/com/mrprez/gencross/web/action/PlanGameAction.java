package com.mrprez.gencross.web.action;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import com.mrprez.gencross.web.bo.PlannedGameBO;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IPlanGameBS;
import com.mrprez.gencross.web.bs.face.ITableBS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class PlanGameAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
	private Integer tableId;
	private Integer plannedGameId;
	private TableBO table;
	private Collection<PlannedGameBO> plannedGamesList;
	private String title;
	private Date startDate;
	private Date endDate;
	
	private IPlanGameBS planGameBS;
	private ITableBS tableBS;
	
	
	public String execute() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		table = tableBS.getTableForGM(tableId, user);
		return INPUT;
	}
	
	public String updatePlannedGame() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		
		planGameBS.updateGame(plannedGameId, title, startDate, endDate, user);
		
		return SUCCESS;
	}
	
	public String createPlannedGame() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		
		planGameBS.createGame(tableId, title, startDate, endDate, user);
		
		return SUCCESS;
	}
	
	public String deletePlannedGame() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
	
		planGameBS.deleteGame(plannedGameId, user);
		
		return SUCCESS;
	}
	
	public String loadPlannedGames() throws Exception {
		plannedGamesList = planGameBS.getPlannedGames(tableId);
		return "jsonPlannedGames";
	}
	
	
	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}
	
	public Integer getPlannedGameId() {
		return plannedGameId;
	}

	public void setPlannedGameId(String plannedGameId) {
		this.plannedGameId = Integer.parseInt(plannedGameId);
	}

	public Collection<PlannedGameBO> getPlannedGamesList(){
		return plannedGamesList;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) throws ParseException {
		this.startDate = dateFormat.parse(startDate);
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) throws ParseException {
		this.endDate = dateFormat.parse(endDate);
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
