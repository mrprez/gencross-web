package com.mrprez.gencross.web.action;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.quartz.JobDetail;
import org.quartz.JobKey;

import com.mrprez.gencross.web.bs.face.IJobBS;
import com.opensymphony.xwork2.ActionSupport;

public class JobProcessingAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private IJobBS jobBS;
	
	private String jobName;
	private Collection<JobDetail> jobList;
	private Map<JobKey, Date> jobLastDates;
	private Map<JobKey, Throwable> exceptions;
	private Set<JobKey> runningJobs;
	
	
	@Override
	public String execute() throws Exception {
		jobList = new TreeSet<JobDetail>(new JobKeyComparator());
		jobList.addAll(jobBS.getJobList());
		setJobLastDates(jobBS.getLastExecutionDates());
		setRunnningJobs(jobBS.getRunningJobs());
		setExceptions(jobBS.getExceptions());
		return INPUT;
	}
	
	
	public String schedule() throws Exception{
		jobBS.schedule(jobName);
		return SUCCESS;
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


	public Map<JobKey, Throwable> getExceptions() {
		return exceptions;
	}


	public void setExceptions(Map<JobKey, Throwable> exceptions) {
		this.exceptions = exceptions;
	}
	
	
	public class JobKeyComparator implements Comparator<JobDetail>{

		@Override
		public int compare(JobDetail o1, JobDetail o2) {
			return o1.getKey().compareTo(o2.getKey());
		}
		
	}
	

}
