package com.mrprez.gencross.web.bs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.ee.servlet.QuartzInitializerServlet;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.web.context.ServletContextAware;

import com.mrprez.gencross.web.bs.face.IJobBS;
import com.mrprez.gencross.web.quartz.GencrossJob;

public class JobBS implements IJobBS, ServletContextAware {
	private ServletContext servletContext;
	private Scheduler scheduler;
	

	
	@Override
	public Collection<JobDetail> getJobList() throws Exception{
		List<JobDetail> result = new ArrayList<JobDetail>();
		
		Set<JobKey> keyList = getScheduler().getJobKeys(GroupMatcher.jobGroupEquals(JOB_GROUP_NAME));
		for(JobKey jobKey : keyList){
			result.add(getScheduler().getJobDetail(jobKey));
		}
		
		return result;
	}
	
	@Override
	public Map<JobKey,Date> getLastExecutionDates() throws Exception{
		Map<JobKey, Date> result = new HashMap<JobKey, Date>();
		
		Set<JobKey> keyList = getScheduler().getJobKeys(GroupMatcher.jobGroupEquals(JOB_GROUP_NAME));
		for(JobKey jobKey : keyList){
			@SuppressWarnings("unchecked")
			Date date = GencrossJob.getLastExecutionDate((Class<? extends GencrossJob>) getScheduler().getJobDetail(jobKey).getJobClass());
			result.put(jobKey, date);
		}
		
		return result;
	}
	
	
	@Override
	public Map<JobKey,Throwable> getExceptions() throws SchedulerException{
		Map<JobKey,Throwable> result = new HashMap<JobKey,Throwable>();
		
		Set<JobKey> keyList = getScheduler().getJobKeys(GroupMatcher.jobGroupEquals(JOB_GROUP_NAME));
		for(JobKey jobKey : keyList){
			@SuppressWarnings("unchecked")
			Throwable exception = GencrossJob.getException((Class<? extends GencrossJob>) getScheduler().getJobDetail(jobKey).getJobClass());
			result.put(jobKey, exception);
		}
		
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Set<JobKey> getRunningJobs() throws SchedulerException{
		Set<JobKey> result = new HashSet<JobKey>();
		
		Set<JobKey> keyList = getScheduler().getJobKeys(GroupMatcher.jobGroupEquals(JOB_GROUP_NAME));
		for(JobKey jobKey : keyList){
			if(GencrossJob.isRunning((Class<? extends GencrossJob>) getScheduler().getJobDetail(jobKey).getJobClass())){
				result.add(jobKey);
			}
		}
		
		return result;
	}
	
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	public synchronized Scheduler getScheduler() throws SchedulerException{
		if(scheduler == null){
			SchedulerFactory schedulerFactory = (SchedulerFactory) servletContext.getAttribute(QuartzInitializerServlet.QUARTZ_FACTORY_KEY);
			scheduler = schedulerFactory.getScheduler();
		}
		return scheduler;
	}
	
	
	public void schedule(String jobName) throws Exception{
		JobKey jobKey = new JobKey(jobName, JOB_GROUP_NAME);
		Date beforeDate = new Date();
		getScheduler().triggerJob(jobKey);
		int count = 0;
		while( (getLastExecutionDates().get(jobKey)==null || getLastExecutionDates().get(jobKey).before(beforeDate)) && count < 300){
			Thread.sleep(100);
			count++;
		}
	}
	
	

}
