package com.mrprez.gencross.web.bs.face;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.quartz.JobDetail;
import org.quartz.JobKey;




public interface IJobBS {
	public static final String TRIGGER_GROUP_NAME = "GENCROSS_GROUP";
	public static final String SEND_JOB_NAME = "send-personnage-job";
	public static final String JOB_GROUP_NAME = "GENCROSS_GROUP";
	
	
	
	Collection<JobDetail> getJobList() throws Exception;

	Map<JobKey, Date> getLastExecutionDates() throws Exception;

	Set<JobKey> getRunningJobs() throws Exception;
	
	void schedule(String jobName) throws Exception;

	Map<JobKey, Throwable> getExceptions() throws Exception;


}
