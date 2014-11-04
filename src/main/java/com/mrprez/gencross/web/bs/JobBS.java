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
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.ee.servlet.QuartzInitializerServlet;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.web.context.ServletContextAware;

import com.mrprez.gencross.web.bs.face.IJobBS;

public class JobBS implements IJobBS, ServletContextAware {
	private ServletContext servletContext;
	private Scheduler senderScheduler;
	

	@Override
	public synchronized void scheduleSender(String jobName) throws Exception {
		getSenderScheduler().triggerJob(new JobKey(jobName, JOB_GROUP_NAME));
	}
	
	
	@Override
	public Collection<JobDetail> getJobList() throws Exception{
		List<JobDetail> result = new ArrayList<JobDetail>();
		
		Set<JobKey> keyList = getSenderScheduler().getJobKeys(GroupMatcher.jobGroupEquals(JOB_GROUP_NAME));
		for(JobKey jobKey : keyList){
			result.add(getSenderScheduler().getJobDetail(jobKey));
		}
		
		return result;
	}
	
	@Override
	public Map<JobKey, Date> getLastExecutionDates() throws Exception{
		Map<JobKey, Date> result = new HashMap<JobKey, Date>();
		
		Set<JobKey> keyList = getSenderScheduler().getJobKeys(GroupMatcher.jobGroupEquals(JOB_GROUP_NAME));
		for(JobKey jobKey : keyList){
			Date lastDate = null;
			for(Trigger trigger : getSenderScheduler().getTriggersOfJob(jobKey)){
				Date previousFireTime = trigger.getPreviousFireTime();
				if(previousFireTime!=null){
					if(lastDate==null || lastDate.after(previousFireTime)){
						lastDate = previousFireTime;
					}
				}
			}
			result.put(jobKey, lastDate);
		}
		
		return result;
	}
	
	
	@Override
	public Set<JobKey> getRunningJobs() throws SchedulerException{
		Set<JobKey> result = new HashSet<JobKey>();
		
		for(JobExecutionContext context : getSenderScheduler().getCurrentlyExecutingJobs()){
			result.add(context.getJobDetail().getKey());
		}
		
		return result;
	}
	
	@Override
	public boolean isSenderRunning(String jobName) throws SchedulerException{
		return ! getSenderScheduler().getCurrentlyExecutingJobs().isEmpty();
	}
	

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	public synchronized Scheduler getSenderScheduler() throws SchedulerException{
		if(senderScheduler == null){
			SchedulerFactory schedulerFactory = (SchedulerFactory) servletContext.getAttribute(QuartzInitializerServlet.QUARTZ_FACTORY_KEY);
			senderScheduler = schedulerFactory.getScheduler();
		}
		return senderScheduler;
	}
	
	

}
