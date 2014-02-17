package com.mrprez.gencross.web.bs;

import javax.servlet.ServletContext;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.ee.servlet.QuartzInitializerServlet;
import org.springframework.web.context.ServletContextAware;

import com.mrprez.gencross.web.bs.face.IJobBS;

public class JobBS implements IJobBS, ServletContextAware {
	private ServletContext servletContext;
	private Scheduler senderScheduler;
	

	@Override
	public synchronized void scheduleSender() throws Exception {
		getSenderScheduler().triggerJob(new JobKey(SEND_JOB_NAME, JOB_GROUP_NAME));
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
	
	@Override
	public boolean isSenderRunning() throws SchedulerException{
		return ! getSenderScheduler().getCurrentlyExecutingJobs().isEmpty();
	}
	

}
