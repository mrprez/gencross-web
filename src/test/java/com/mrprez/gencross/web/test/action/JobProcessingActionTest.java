package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.JobProcessingAction;
import com.mrprez.gencross.web.bs.face.IJobBS;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class JobProcessingActionTest extends AbstractActionTest {

	@Mock
	private IJobBS jobBS;

	@InjectMocks
	private JobProcessingAction jobProcessingAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare
		jobProcessingAction.setJobName("string_1");

		// Execute
		String result = jobProcessingAction.execute();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals("failTest", jobProcessingAction.getJobList());
		Assert.assertEquals("failTest", jobProcessingAction.getJobName());
		Assert.assertEquals("failTest", jobProcessingAction.getExceptions());
		Assert.assertEquals("failTest", jobProcessingAction.getJobLastDates());
		Assert.assertEquals("failTest", jobProcessingAction.getRunningJobs());
	}


	@Test
	public void testSchedule() throws Exception {
		// Prepare
		jobProcessingAction.setJobName("string_1");

		// Execute
		String result = jobProcessingAction.schedule();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals("failTest", jobProcessingAction.getJobList());
		Assert.assertEquals("failTest", jobProcessingAction.getJobName());
		Assert.assertEquals("failTest", jobProcessingAction.getExceptions());
		Assert.assertEquals("failTest", jobProcessingAction.getJobLastDates());
		Assert.assertEquals("failTest", jobProcessingAction.getRunningJobs());
	}
}
