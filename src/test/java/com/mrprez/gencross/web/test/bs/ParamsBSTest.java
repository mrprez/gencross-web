package com.mrprez.gencross.web.test.bs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.bo.ParamBO;
import com.mrprez.gencross.web.bs.ParamsBS;
import com.mrprez.gencross.web.bs.util.BusinessException;
import com.mrprez.gencross.web.dao.face.IParamDAO;

@RunWith(MockitoJUnitRunner.class)
public class ParamsBSTest {
	
	@InjectMocks
	private ParamsBS paramsBS;
	
	@Mock
	private IParamDAO paramDao;
	
	
	@Test
	public void testGetParam() throws Exception{
		// Prepare
		String paramKey = "TestKey";
		String paramValue = "TestValue";
		Mockito.when(paramDao.getParam(paramKey)).thenReturn(buildParamBO(paramKey, paramValue));
		
		// Execute
		ParamBO result = paramsBS.getParam(paramKey);
		
		// Check
		Assert.assertEquals(paramKey, result.getKey());
		Assert.assertEquals(paramValue, result.getValue());
		Assert.assertEquals(ParamBO.STRING_TYPE, result.getType());
	}
	
	
	@Test
	public void testGetAllParams() throws Exception{
		// Prepare
		Map<String, ParamBO> paramMap = new HashMap<String, ParamBO>();
		paramMap.put("key1", buildParamBO("key1", "stringValue"));
		paramMap.put("key2", buildParamBO("key2", 2));
		paramMap.put("key3", buildParamBO("key3", 5.65));
		paramMap.put("key4", buildParamBO("key4", new Date(3600000)));
		paramMap.put("key4", buildParamBO("key4", Boolean.TRUE));
		Mockito.when(paramDao.getAllParams()).thenReturn(new ArrayList<ParamBO>(paramMap.values()));
		
		// Execute
		Collection<ParamBO> result = paramsBS.getAllParams();
		
		// Check
		for(ParamBO param : result){
			ParamBO refParam = paramMap.get(param.getKey());
			Assert.assertNotNull(refParam);
			Assert.assertEquals(refParam.getValue(), param.getValue());
			Assert.assertEquals(refParam.getType(), param.getType());
			paramMap.remove(refParam.getKey());
		}
		Assert.assertTrue(paramMap.isEmpty());
	}
	
	
	@Test
	public void testUpdateBooleanParam_Success() throws Exception{
		// Prepare
		ParamBO param = buildParamBO("key", true);
		Mockito.when(paramDao.getParam("key")).thenReturn(param);
		
		// Execute
		ParamBO result = paramsBS.updateParam("key", false);
		
		// Check
		Assert.assertEquals(param.getKey(), result.getKey());
		Assert.assertEquals(param.getType(), result.getType());
		Assert.assertEquals(false, result.getValue());
		Mockito.verify(paramDao).getParam("key");
	}
	
	@Test
	public void testUpdateDoubleParam_Success() throws Exception{
		// Prepare
		ParamBO param = buildParamBO("key", 1.0);
		Mockito.when(paramDao.getParam("key")).thenReturn(param);
		
		// Execute
		ParamBO result = paramsBS.updateParam("key", 1.2);
		
		// Check
		Assert.assertEquals(param.getKey(), result.getKey());
		Assert.assertEquals(param.getType(), result.getType());
		Assert.assertEquals(1.2, result.getValue());
		Mockito.verify(paramDao).getParam("key");
	}
	
	@Test
	public void testUpdateIntegerParam_Success() throws Exception{
		// Prepare
		ParamBO param = buildParamBO("key", 3);
		Mockito.when(paramDao.getParam("key")).thenReturn(param);
		
		// Execute
		ParamBO result = paramsBS.updateParam("key", 4);
		
		// Check
		Assert.assertEquals(param.getKey(), result.getKey());
		Assert.assertEquals(param.getType(), result.getType());
		Assert.assertEquals(4, result.getValue());
		Mockito.verify(paramDao).getParam("key");
	}
	
	@Test
	public void testUpdateDateParam_Success() throws Exception{
		// Prepare
		ParamBO param = buildParamBO("key", new Date(3600000));
		Mockito.when(paramDao.getParam("key")).thenReturn(param);
		
		// Execute
		ParamBO result = paramsBS.updateParam("key", new Date(7200000));
		
		// Check
		Assert.assertEquals(param.getKey(), result.getKey());
		Assert.assertEquals(param.getType(), result.getType());
		Assert.assertEquals(new Date(7200000), result.getValue());
		Mockito.verify(paramDao).getParam("key");
	}
	
	@Test
	public void testUpdateStringParam_Success() throws Exception{
		// Prepare
		ParamBO param = buildParamBO("key", "stringValue");
		Mockito.when(paramDao.getParam("key")).thenReturn(param);
		
		// Execute
		ParamBO result = paramsBS.updateParam("key", "newStringValue");
		
		// Check
		Assert.assertEquals(param.getKey(), result.getKey());
		Assert.assertEquals(param.getType(), result.getType());
		Assert.assertEquals("newStringValue", result.getValue());
		Mockito.verify(paramDao).getParam("key");
	}
	
	
	@Test
	public void testUpdateBooleanParam_Fail() throws Exception{
		// Prepare
		ParamBO param = buildParamBO("key", true);
		Mockito.when(paramDao.getParam("key")).thenReturn(param);
		BusinessException businessException = null;
		
		// Execute
		try{
			paramsBS.updateParam("key", 1.2);
		}catch(BusinessException caughtException){
			businessException = caughtException;
		}
		
		// Check
		Assert.assertNotNull(businessException);
		Assert.assertEquals("Parameter key is not a real number", businessException.getMessage());
	}
	
	@Test
	public void testUpdateDoubleParam_Fail() throws Exception{
		// Prepare
		ParamBO param = buildParamBO("key", 1.0);
		Mockito.when(paramDao.getParam("key")).thenReturn(param);
		BusinessException businessException = null;
		
		// Execute
		try{
			paramsBS.updateParam("key", 4);
		}catch(BusinessException caughtException){
			businessException = caughtException;
		}
		
		// Check
		Assert.assertNotNull(businessException);
		Assert.assertEquals("Parameter key is not an integer", businessException.getMessage());
	}
	
	@Test
	public void testUpdateIntegerParam_Fail() throws Exception{
		// Prepare
		ParamBO param = buildParamBO("key", 3);
		Mockito.when(paramDao.getParam("key")).thenReturn(param);
		BusinessException businessException = null;
		
		// Execute
		try{
			paramsBS.updateParam("key", new Date(7200000));
		}catch(BusinessException caughtException){
			businessException = caughtException;
		}
		
		// Check
		Assert.assertNotNull(businessException);
		Assert.assertEquals("Parameter key is not a date", businessException.getMessage());
	}
	
	@Test
	public void testUpdateDateParam_Fail() throws Exception{
		// Prepare
		ParamBO param = buildParamBO("key", new Date(3600000));
		Mockito.when(paramDao.getParam("key")).thenReturn(param);
		BusinessException businessException = null;
		
		// Execute
		try{
			paramsBS.updateParam("key", "newStringValue");
		}catch(BusinessException caughtException){
			businessException = caughtException;
		}
		
		// Check
		Assert.assertNotNull(businessException);
		Assert.assertEquals("Parameter key is not a string", businessException.getMessage());
	}
	
	@Test
	public void testUpdateStringParam_Fail() throws Exception{
		// Prepare
		ParamBO param = buildParamBO("key", "stringValue");
		Mockito.when(paramDao.getParam("key")).thenReturn(param);
		BusinessException businessException = null;
		
		// Execute
		try{
			paramsBS.updateParam("key", true);
		}catch(BusinessException caughtException){
			businessException = caughtException;
		}
		
		// Check
		Assert.assertNotNull(businessException);
		Assert.assertEquals("Parameter key is not a boolean", businessException.getMessage());
	}
	
	
	public static ParamBO buildParamBO(String key, Object value){
		ParamBO param = new ParamBO();
		param.setKey(key);
		param.setValue(value);
		return param;
	}

}
