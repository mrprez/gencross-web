package com.mrprez.gencross.web.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import com.mrprez.gencross.web.bo.PlannedGameBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IPlanGameBS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class PlanGameAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private static DateFormat dayFormat = new SimpleDateFormat("dd/MM/yyyy"); 
	private Integer tableId;
	private Collection<PlannedGameBO> plannedGamesList;
	private String title;
	private String startDay;
	private Integer startHour;
	private Integer startMinutes;
	private String endDay;
	private Integer endHour;
	private Integer endMinutes;
	private Long startTime;
	private Long endTime;
	private Integer dayDelta;
	private Integer minuteDelta;
	
	private IPlanGameBS planGameBS;
	
	
	public String execute() throws Exception {
		return INPUT;
	}
	
	public String createNewGame() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		
		PlannedGameBO plannedGame = new PlannedGameBO();
		plannedGame.setTitle(title);
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(dayFormat.parse(startDay));
		startCalendar.set(Calendar.HOUR_OF_DAY, startHour);
		startCalendar.set(Calendar.MINUTE, startMinutes);
		startCalendar.set(Calendar.SECOND, 0);
		startCalendar.set(Calendar.MILLISECOND, 0);
		plannedGame.setStartTime(startCalendar.getTime());
		
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(dayFormat.parse(endDay));
		endCalendar.set(Calendar.HOUR_OF_DAY, endHour);
		endCalendar.set(Calendar.MINUTE, endMinutes);
		endCalendar.set(Calendar.SECOND, 0);
		startCalendar.set(Calendar.MILLISECOND, 0);
		plannedGame.setEndTime(endCalendar.getTime());
		
		planGameBS.planGame(tableId, plannedGame, user );
		
		return SUCCESS;
	}
	
	public String loadEvents() throws Exception {
		plannedGamesList = planGameBS.getPlannedGames(tableId, new Date(startTime*1000),  new Date(endTime*1000));
		return "events";
	}
	
	public String updateGame() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		planGameBS.replanGame(tableId, dayDelta, minuteDelta, new Date(startTime),  new Date(endTime), user);
		
		return null;
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

	public void setTitle(String title) {
		this.title = title;
	}

	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}

	public void setStartHour(Integer startHour) {
		this.startHour = startHour;
	}

	public void setStartMinutes(Integer startMinutes) {
		this.startMinutes = startMinutes;
	}

	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}

	public void setEndHour(Integer endHour) {
		this.endHour = endHour;
	}

	public void setEndMinutes(Integer endMinutes) {
		this.endMinutes = endMinutes;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public void setDayDelta(Integer dayDelta) {
		this.dayDelta = dayDelta;
	}

	public void setMinuteDelta(Integer minuteDelta) {
		this.minuteDelta = minuteDelta;
	}

	public IPlanGameBS getPlanGameBS() {
		return planGameBS;
	}

	public void setPlanGameBS(IPlanGameBS planGameBS) {
		this.planGameBS = planGameBS;
	}

	
	
}
