package com.mrprez.gencross.web.action;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import com.mrprez.gencross.web.bo.ParamBO;
import com.mrprez.gencross.web.bs.face.IParamsBS;
import com.opensymphony.xwork2.ActionSupport;

public class ParamsAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private Collection<ParamBO> paramList;
	
	private String key;
	private String newValue;
	private String day;
	private Integer hour;
	private Integer minutes;
	private Integer seconds;
	private Integer milliSeconds;
	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss,SSS");
	
	private IParamsBS paramsBS;
	
	
	public String execute() throws Exception {
		paramList = paramsBS.getAllParams();
		
		return INPUT;
	}
	
	public String edit() throws Exception {
		ParamBO param = paramsBS.getParam(key);
		if(param.getType()==ParamBO.DATE_TYPE){
			String dateString = day+" "+hour+":"+minutes+":"+seconds+","+milliSeconds;
			Date date;
			try{
				date = dateFormat.parse(dateString);
			}catch(ParseException pe){
				super.addActionError("Format de date/heure invalide");
				execute();
				return ERROR;
			}
			paramsBS.updateParam(key, date);
		} else if(param.getType()==ParamBO.BOOLEAN_TYPE){
			Boolean b = new Boolean(newValue);
			paramsBS.updateParam(key, b);
		} else if(param.getType()==ParamBO.INTEGER_TYPE){
			Integer integer;
			try{
				integer = new Integer(newValue);
			}catch (NumberFormatException nfe) {
				super.addActionError("Nombre entier incorrect");
				execute();
				return ERROR;
			}
			paramsBS.updateParam(key, integer);
		} else if(param.getType()==ParamBO.REAL_TYPE){
			Double d;
			try{
				d = new Double(newValue);
			}catch (NumberFormatException nfe) {
				super.addActionError("Nombre décimal incorrect");
				execute();
				return ERROR;
			}
			paramsBS.updateParam(key, d);
		}else{
			paramsBS.updateParam(key, newValue);
		}
		
		
		return SUCCESS;
	}


	public Collection<ParamBO> getParamList() {
		return paramList;
	}


	public void setParamList(Collection<ParamBO> paramList) {
		this.paramList = paramList;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public Integer getHour() {
		return hour;
	}

	public void setHour(Integer hour) {
		this.hour = hour;
	}

	public Integer getMinutes() {
		return minutes;
	}

	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
	}

	public Integer getSeconds() {
		return seconds;
	}

	public void setSeconds(Integer seconds) {
		this.seconds = seconds;
	}

	public Integer getMilliSeconds() {
		return milliSeconds;
	}

	public void setMilliSeconds(Integer milliSeconds) {
		this.milliSeconds = milliSeconds;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public IParamsBS getParamsBS() {
		return paramsBS;
	}

	public void setParamsBS(IParamsBS paramsBS) {
		this.paramsBS = paramsBS;
	}
	
	

}
