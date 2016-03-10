package com.mrprez.gencross.web.test.action;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.impl.JobDetailImpl;

import com.mrprez.gencross.web.action.JobProcessingAction;
import com.mrprez.gencross.web.bs.face.IJobBS;

@RunWith(MockitoJUnitRunner.class)
public class JobProcessingActionTest extends AbstractActionTest {

	@Mock
	private IJobBS jobBS;

	@InjectMocks
	private JobProcessingAction jobProcessingAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare
		Mockito.when(jobBS.getJobList()).thenReturn(Arrays.asList(buildJobDetail("Job D"), buildJobDetail("Job A"), buildJobDetail("Job E")));
		Map<JobKey, Date> lastExecutionDates = new HashMap<JobKey, Date>();
		lastExecutionDates.put(new JobKey("Job D"), new Date());
		Mockito.when(jobBS.getLastExecutionDates()).thenReturn(lastExecutionDates);
		Set<JobKey> runningJobs = new HashSet<JobKey>();
		runningJobs.add(new JobKey("Job B"));
		Mockito.when(jobBS.getRunningJobs()).thenReturn(runningJobs);
		Map<JobKey, Throwable> exceptions = new HashMap<JobKey, Throwable>();
		exceptions.put(new JobKey("Job E"), new RuntimeException());
		Mockito.when(jobBS.getExceptions()).thenReturn(exceptions);
		
		// Execute
		String result = jobProcessingAction.execute();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals(lastExecutionDates, jobProcessingAction.getJobLastDates());
		Assert.assertEquals(runningJobs, jobProcessingAction.getRunningJobs());
		Assert.assertEquals(exceptions, jobProcessingAction.getExceptions());
	}
	
	
	private JobDetail buildJobDetail(String keyName){
		JobDetailImpl jobDetail = new JobDetailImpl();
		jobDetail.setKey(new JobKey(keyName));
		return jobDetail;
	}


	@Test
	public void testSchedule() throws Exception {
		// Prepare
		String jobName = "Job J";

		// Execute
		jobProcessingAction.setJobName(jobName);
		String result = jobProcessingAction.schedule();

		// Check
		Assert.assertEquals("success", result);
		Mockito.verify(jobBS).schedule(jobName);
	}
}
