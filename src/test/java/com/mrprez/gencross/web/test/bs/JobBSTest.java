package com.mrprez.gencross.web.test.bs;

import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.mrprez.gencross.web.bs.JobBS;

public class JobBSTest {
	
	private SchedulerFactory schedulerFactory;
	
	
	@Test
	public void testGetJobList() throws Exception{
		JobBS jobBS = buildJobBS();
		
		Collection<JobDetail> result = jobBS.getJobList();
		
		Set<JobKey> expectedJobKeys = new HashSet<JobKey>();
		expectedJobKeys.add(new JobKey("send-personnage-job", "GENCROSS_GROUP"));
		expectedJobKeys.add(new JobKey("save-personnage-job", "GENCROSS_GROUP"));
		expectedJobKeys.add(new JobKey("get-table-mails-job", "GENCROSS_GROUP"));
		for(JobDetail jobDetail : result){
			Assert.assertTrue(expectedJobKeys.contains(jobDetail.getKey()));
			expectedJobKeys.remove(jobDetail.getKey());
		}
		Assert.assertTrue(expectedJobKeys.isEmpty());
	}
	
	
	
	private JobBS buildJobBS() throws SchedulerException{
		JobBS jobBS = new JobBS();
		ServletContext servletContext = Mockito.mock(ServletContext.class);
		Mockito.when(servletContext.getAttribute("org.quartz.impl.StdSchedulerFactory.KEY")).thenReturn(schedulerFactory);
		jobBS.setServletContext(servletContext);
		return jobBS;
	}
	
	@Before
	public void startScheduler() throws SchedulerException{
		Properties properties = new Properties();
		properties.put( "org.quartz.scheduler.instanceName", "GenCrossScheduler" );
		properties.put( "org.quartz.scheduler.instanceId", "1" );
		properties.put( "org.quartz.scheduler.rmi.export", "false" );
		properties.put( "org.quartz.scheduler.rmi.proxy", "false" );
		properties.put( "org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool" );
		properties.put( "org.quartz.threadPool.threadCount", "3" );
		properties.put( "org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore" );
		properties.put( "org.quartz.plugin.jobInitializer.class", "org.quartz.plugins.xml.XMLSchedulingDataProcessorPlugin" );
		properties.put( "org.quartz.plugin.jobInitializer.fileNames", "jobsTest.xml" );
		properties.put( "org.quartz.plugin.jobInitializer.failOnFileNotFound", "true" );
		properties.put( "org.quartz.plugin.jobInitializer.scanInterval", "3600" );
		properties.put( "org.quartz.plugin.jobInitializer.wrapInUserTransaction", "false" );
		
		schedulerFactory = new StdSchedulerFactory(properties);
		schedulerFactory.getScheduler().start();
	}
	
	@After
	public void stopScheduler() throws SchedulerException{
		schedulerFactory.getScheduler().shutdown();
	}

}
