package com.mrprez.gencross.web.action;

import java.util.Date;

import com.mrprez.gencross.web.bs.face.IJobBS;
import com.mrprez.gencross.web.quartz.PersonnageSenderJob;
import com.opensymphony.xwork2.ActionSupport;

public class JobProcessingAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private IJobBS jobBS;
	
	
	@Override
	public String execute() throws Exception {
		return INPUT;
	}


	public String scheduleSender() throws Exception {
		jobBS.scheduleSender();
		return SUCCESS;
	}
	
	
	public boolean getSenderRunning() throws Exception{
		return jobBS.isSenderRunning();
	}

	public Date getSenderLastExecutionDate() {
		return PersonnageSenderJob.getLastExecutionDate();
	}

	public IJobBS getJobBS() {
		return jobBS;
	}

	public void setJobBS(IJobBS jobBS) {
		this.jobBS = jobBS;
	}
	
	
	

}
