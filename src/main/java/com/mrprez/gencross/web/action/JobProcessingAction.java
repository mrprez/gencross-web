package com.mrprez.gencross.web.action;

import java.util.Date;

import org.springframework.web.context.ContextLoader;

import com.mrprez.gencross.web.bs.face.IJobBS;
import com.mrprez.gencross.web.quartz.PersonnageSenderJob;
import com.opensymphony.xwork2.ActionSupport;

public class JobProcessingAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	
	
	@Override
	public String execute() throws Exception {
		return INPUT;
	}


	public String scheduleSender() throws Exception {
		IJobBS jobBS = (IJobBS)ContextLoader.getCurrentWebApplicationContext().getBean("jobBS");
		jobBS.scheduleSender();
		return SUCCESS;
	}
	
	
	public boolean getSenderRunning() throws Exception{
		IJobBS jobBS = (IJobBS)ContextLoader.getCurrentWebApplicationContext().getBean("jobBS");
		return jobBS.isSenderRunning();
	}

	public Date getSenderLastExecutionDate() {
		return PersonnageSenderJob.getLastExecutionDate();
	}
	

}
