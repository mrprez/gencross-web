package com.mrprez.gencross.web.action;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.quartz.JobDetail;
import org.quartz.JobKey;

import com.mrprez.gencross.web.bs.face.IJobBS;
import com.mrprez.gencross.web.quartz.PersonnageSenderJob;
import com.opensymphony.xwork2.ActionSupport;

public class JobProcessingAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private IJobBS jobBS;
	
	private String jobName;
	private Collection<JobDetail> jobList;
	private Map<JobKey, Date> jobLastDates;
	private Set<JobKey> runningJobs;
	
	
	@Override
	public String execute() throws Exception {
		setJobList(jobBS.getJobList());
		setJobLastDates(jobBS.getLastExecutionDates());
		setRunnningJobs(jobBS.getRunningJobs());
		return INPUT;
	}


	public String scheduleSender() throws Exception {
		jobBS.scheduleSender(jobName);
		return SUCCESS;
	}
	
	
	public boolean getSenderRunning() throws Exception{
		return jobBS.isSenderRunning(jobName);
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


	public String getJobName() {
		return jobName;
	}


	public void setJobName(String jobName) {
		this.jobName = jobName;
	}


	public Collection<JobDetail> getJobList() {
		return jobList;
	}


	public void setJobList(Collection<JobDetail> jobList) {
		this.jobList = jobList;
	}


	public Map<JobKey, Date> getJobLastDates() {
		return jobLastDates;
	}


	public void setJobLastDates(Map<JobKey, Date> jobLastDates) {
		this.jobLastDates = jobLastDates;
	}


	public Set<JobKey> getRunningJobs() {
		return runningJobs;
	}


	public void setRunnningJobs(Set<JobKey> runningJobs) {
		this.runningJobs = runningJobs;
	}
	
	
	

}
