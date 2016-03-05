package com.mrprez.gencross.web.test.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.ParamsAction;
import com.mrprez.gencross.web.bo.ParamBO;
import com.mrprez.gencross.web.bs.face.IParamsBS;

@RunWith(MockitoJUnitRunner.class)
public class ParamsActionTest extends AbstractActionTest {

	@Mock
	private IParamsBS paramsBS;

	@InjectMocks
	private ParamsAction paramsAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare
		Collection<ParamBO> paramList = new ArrayList<ParamBO>();
		Mockito.when(paramsBS.getAllParams()).thenReturn(paramList);

		// Execute
		String result = paramsAction.execute();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals(paramList, paramsAction.getParamList());
	}

	
	@Test
	public void testEdit_Success_Date() throws Exception {
		// Prepare
		String key = "batSignal";
		ParamBO param = new ParamBO();
		param.setType(ParamBO.DATE_TYPE);
		Mockito.when(paramsBS.getParam(key)).thenReturn(param);

		// Execute
		paramsAction.setKey(key);
		paramsAction.setDay("19/08/1983");
		paramsAction.setHour(1);
		paramsAction.setMinutes(12);
		paramsAction.setSeconds(10);
		paramsAction.setMilliSeconds(500);
		String result = paramsAction.edit();

		// Check
		Assert.assertEquals("success", result);
		Mockito.verify(paramsBS).updateParam(key, new SimpleDateFormat("dd/MM/yyyy hh:mm:ss:SSS").parse("19/08/1983 01:12:10:500"));
	}
	
	
	@Test
	public void testEdit_Fail_Date() throws Exception {
		// Prepare
		String key = "batSignal";
		ParamBO param = new ParamBO();
		param.setType(ParamBO.DATE_TYPE);
		Mockito.when(paramsBS.getParam(key)).thenReturn(param);

		// Execute
		paramsAction.setKey(key);
		paramsAction.setDay("unparsable");
		paramsAction.setHour(1);
		paramsAction.setMinutes(12);
		paramsAction.setSeconds(10);
		paramsAction.setMilliSeconds(500);
		String result = paramsAction.edit();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(paramsAction.getActionErrors().contains("Format de date/heure invalide"));
		Mockito.verify(paramsBS, Mockito.never()).updateParam(Mockito.eq(key), Mockito.any(Date.class));
	}
	
	
	@Test
	public void testEdit_Success_Boolean() throws Exception {
		// Prepare
		String key = "batSignal";
		ParamBO param = new ParamBO();
		param.setType(ParamBO.BOOLEAN_TYPE);
		Mockito.when(paramsBS.getParam(key)).thenReturn(param);

		// Execute
		paramsAction.setKey(key);
		paramsAction.setNewValue("true");
		String result = paramsAction.edit();

		// Check
		Assert.assertEquals("success", result);
		Mockito.verify(paramsBS).updateParam(key, Boolean.TRUE);
	}
	
	
	@Test
	public void testEdit_Success_Integer() throws Exception {
		// Prepare
		String key = "batSignal";
		ParamBO param = new ParamBO();
		param.setType(ParamBO.INTEGER_TYPE);
		Mockito.when(paramsBS.getParam(key)).thenReturn(param);

		// Execute
		paramsAction.setKey(key);
		paramsAction.setNewValue("015");
		String result = paramsAction.edit();

		// Check
		Assert.assertEquals("success", result);
		Mockito.verify(paramsBS).updateParam(key, new Integer(15));
	}
	
	
	@Test
	public void testEdit_Fail_Integer() throws Exception {
		// Prepare
		String key = "batSignal";
		ParamBO param = new ParamBO();
		param.setType(ParamBO.INTEGER_TYPE);
		Mockito.when(paramsBS.getParam(key)).thenReturn(param);

		// Execute
		paramsAction.setKey(key);
		paramsAction.setNewValue("unparsable");
		String result = paramsAction.edit();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(paramsAction.getActionErrors().contains("Nombre entier incorrect"));
		Mockito.verify(paramsBS, Mockito.never()).updateParam(Mockito.eq(key), Mockito.any(Integer.class));
	}
	
	
	@Test
	public void testEdit_Success_Real() throws Exception {
		// Prepare
		String key = "batSignal";
		ParamBO param = new ParamBO();
		param.setType(ParamBO.REAL_TYPE);
		Mockito.when(paramsBS.getParam(key)).thenReturn(param);

		// Execute
		paramsAction.setKey(key);
		paramsAction.setNewValue("123.456");
		String result = paramsAction.edit();

		// Check
		Assert.assertEquals("success", result);
		Mockito.verify(paramsBS).updateParam(key, new Double(123.456));
	}
	
	
	@Test
	public void testEdit_Fail_Real() throws Exception {
		// Prepare
		String key = "batSignal";
		ParamBO param = new ParamBO();
		param.setType(ParamBO.REAL_TYPE);
		Mockito.when(paramsBS.getParam(key)).thenReturn(param);

		// Execute
		paramsAction.setKey(key);
		paramsAction.setNewValue("unparsable");
		String result = paramsAction.edit();

		// Check
		Assert.assertEquals("error", result);
		Assert.assertTrue(paramsAction.getActionErrors().contains("Nombre décimal incorrect"));
		Mockito.verify(paramsBS, Mockito.never()).updateParam(Mockito.eq(key), Mockito.any(Double.class));
	}
	

	@Test
	public void testEdit_Success_String() throws Exception {
		// Prepare
		String key = "batSignal";
		ParamBO param = new ParamBO();
		param.setType(ParamBO.STRING_TYPE);
		String newValue = "newValue";
		Mockito.when(paramsBS.getParam(key)).thenReturn(param);

		// Execute
		paramsAction.setKey(key);
		paramsAction.setNewValue(newValue);
		String result = paramsAction.edit();

		// Check
		Assert.assertEquals("success", result);
		Mockito.verify(paramsBS).updateParam(key, newValue);
	}
	
}
