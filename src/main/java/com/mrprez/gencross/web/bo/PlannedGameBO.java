package com.mrprez.gencross.web.bo;

import java.util.Date;

public class PlannedGameBO {
	private Integer id;
	private TableBO table;
	private String title;
	private Date startTime;
	private Date endTime;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public TableBO getTable() {
		return table;
	}
	public void setTable(TableBO table) {
		this.table = table;
	}
	

}
