package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.ParamsAction;
import com.mrprez.gencross.web.bs.face.IParamsBS;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class ParamsActionTest extends AbstractActionTest {

	@Mock
	private IParamsBS paramsBS;

	@InjectMocks
	private ParamsAction paramsAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare
		paramsAction.setKey("string_1");
		paramsAction.setMinutes(2);
		paramsAction.setMilliSeconds(3);
		paramsAction.setSeconds(4);
		paramsAction.setNewValue("string_5");
		paramsAction.setHour(6);
		paramsAction.setDay("string_7");

		// Execute
		String result = paramsAction.execute();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals("failTest", paramsAction.getMilliSeconds());
		Assert.assertEquals("failTest", paramsAction.getMinutes());
		Assert.assertEquals("failTest", paramsAction.getHour());
		Assert.assertEquals("failTest", paramsAction.getDay());
		Assert.assertEquals("failTest", paramsAction.getKey());
		Assert.assertEquals("failTest", paramsAction.getSeconds());
		Assert.assertEquals("failTest", paramsAction.getParamList());
		Assert.assertEquals("failTest", paramsAction.getNewValue());
	}


	@Test
	public void testEdit() throws Exception {
		// Prepare
		paramsAction.setKey("string_1");
		paramsAction.setMinutes(2);
		paramsAction.setMilliSeconds(3);
		paramsAction.setSeconds(4);
		paramsAction.setNewValue("string_5");
		paramsAction.setHour(6);
		paramsAction.setDay("string_7");

		// Execute
		String result = paramsAction.edit();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals("failTest", paramsAction.getMilliSeconds());
		Assert.assertEquals("failTest", paramsAction.getMinutes());
		Assert.assertEquals("failTest", paramsAction.getHour());
		Assert.assertEquals("failTest", paramsAction.getDay());
		Assert.assertEquals("failTest", paramsAction.getKey());
		Assert.assertEquals("failTest", paramsAction.getSeconds());
		Assert.assertEquals("failTest", paramsAction.getParamList());
		Assert.assertEquals("failTest", paramsAction.getNewValue());
	}
}
