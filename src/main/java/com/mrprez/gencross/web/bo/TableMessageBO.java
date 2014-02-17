package com.mrprez.gencross.web.bo;

import java.util.Date;

/**
 * Note: this class has a natural ordering that is inconsistent with equals.
 * 
 */
public class TableMessageBO implements Comparable<TableMessageBO>{
	private Integer id;
	private String title;
	private String tableName;
	private String senderMail;
	private UserBO author;
	private String data;
	private Date date;
	
	
	public UserBO getAuthor() {
		return author;
	}
	public void setAuthor(UserBO author) {
		this.author = author;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
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
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getSenderMail() {
		return senderMail;
	}
	public void setSenderMail(String senderMail) {
		this.senderMail = senderMail;
	}
	
	public void setSubject(String subject){
		if(subject.contains("[") && subject.indexOf("]", subject.indexOf("[")) >= 0){
			int start = subject.indexOf("[")+1;
			int end = subject.indexOf("]", start);
			tableName = subject.substring(start, end).trim();
		}
		title = subject;
	}
	
	public String getSubject(){
		return title;
	}
	
	@Override
	public int compareTo(TableMessageBO o) {
		if(o==null){
			return 1;
		}
		if(this.getDate().equals(o.getDate())){
			if(o.getId() == null && getId() == null){
				return 0;
			}
			if(o.getId() == null){
				return 0;
			}
			if(getId() == null){
				return -1;
			}
			return this.getId().compareTo(o.getId());
		}
		return this.getDate().compareTo(o.getDate());
	}
	

}
