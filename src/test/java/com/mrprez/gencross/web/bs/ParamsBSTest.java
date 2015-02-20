package com.mrprez.gencross.web.bs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import com.mrprez.gencross.web.bo.ParamBO;
import com.mrprez.gencross.web.bs.util.BusinessException;
import com.mrprez.gencross.web.dao.face.IParamDAO;

public class ParamsBSTest {
	
	
	@Test
	public void testGetParam() throws Exception{
		ParamsBS paramsBS = buildParamsBS();
		String paramKey = "TestKey";
		String paramValue = "TestValue";
		Mockito.when(paramsBS.getParamDAO().getParam(paramKey)).thenReturn(buildParamBO(paramKey, paramValue));
		
		ParamBO result = paramsBS.getParam(paramKey);
		
		Assert.assertEquals(paramKey, result.getKey());
		Assert.assertEquals(paramValue, result.getValue());
		Assert.assertEquals(ParamBO.STRING_TYPE, result.getType());
	}
	
	
	@Test
	public void testGetAllParams() throws Exception{
		ParamsBS paramsBS = buildParamsBS();
		Map<String, ParamBO> paramMap = new HashMap<String, ParamBO>();
		paramMap.put("key1", buildParamBO("key1", "stringValue"));
		paramMap.put("key2", buildParamBO("key2", 2));
		paramMap.put("key3", buildParamBO("key3", 5.65));
		paramMap.put("key4", buildParamBO("key4", new Date(3600000)));
		paramMap.put("key4", buildParamBO("key4", Boolean.TRUE));
		Mockito.when(paramsBS.getParamDAO().getAllParams()).thenReturn(new ArrayList<ParamBO>(paramMap.values()));
		
		Collection<ParamBO> result = paramsBS.getAllParams();
		
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
		ParamsBS paramsBS = buildParamsBS();
		ParamBO param = buildParamBO("key", true);
		Mockito.when(paramsBS.getParamDAO().getParam("key")).thenReturn(param);
		
		ParamBO result = paramsBS.updateParam("key", false);
		
		Assert.assertEquals(param.getKey(), result.getKey());
		Assert.assertEquals(param.getType(), result.getType());
		Assert.assertEquals(false, result.getValue());
		Mockito.verify(paramsBS.getParamDAO()).getParam("key");
	}
	
	@Test
	public void testUpdateDoubleParam_Success() throws Exception{
		ParamsBS paramsBS = buildParamsBS();
		ParamBO param = buildParamBO("key", 1.0);
		Mockito.when(paramsBS.getParamDAO().getParam("key")).thenReturn(param);
		
		ParamBO result = paramsBS.updateParam("key", 1.2);
		
		Assert.assertEquals(param.getKey(), result.getKey());
		Assert.assertEquals(param.getType(), result.getType());
		Assert.assertEquals(1.2, result.getValue());
		Mockito.verify(paramsBS.getParamDAO()).getParam("key");
	}
	
	@Test
	public void testUpdateIntegerParam_Success() throws Exception{
		ParamsBS paramsBS = buildParamsBS();
		ParamBO param = buildParamBO("key", 3);
		Mockito.when(paramsBS.getParamDAO().getParam("key")).thenReturn(param);
		
		ParamBO result = paramsBS.updateParam("key", 4);
		
		Assert.assertEquals(param.getKey(), result.getKey());
		Assert.assertEquals(param.getType(), result.getType());
		Assert.assertEquals(4, result.getValue());
		Mockito.verify(paramsBS.getParamDAO()).getParam("key");
	}
	
	@Test
	public void testUpdateDateParam_Success() throws Exception{
		ParamsBS paramsBS = buildParamsBS();
		ParamBO param = buildParamBO("key", new Date(3600000));
		Mockito.when(paramsBS.getParamDAO().getParam("key")).thenReturn(param);
		
		ParamBO result = paramsBS.updateParam("key", new Date(7200000));
		
		Assert.assertEquals(param.getKey(), result.getKey());
		Assert.assertEquals(param.getType(), result.getType());
		Assert.assertEquals(new Date(7200000), result.getValue());
		Mockito.verify(paramsBS.getParamDAO()).getParam("key");
	}
	
	@Test
	public void testUpdateStringParam_Success() throws Exception{
		ParamsBS paramsBS = buildParamsBS();
		ParamBO param = buildParamBO("key", "stringValue");
		Mockito.when(paramsBS.getParamDAO().getParam("key")).thenReturn(param);
		
		ParamBO result = paramsBS.updateParam("key", "newStringValue");
		
		Assert.assertEquals(param.getKey(), result.getKey());
		Assert.assertEquals(param.getType(), result.getType());
		Assert.assertEquals("newStringValue", result.getValue());
		Mockito.verify(paramsBS.getParamDAO()).getParam("key");
	}
	
	
	@Test
	public void testUpdateBooleanParam_Fail() throws Exception{
		ParamsBS paramsBS = buildParamsBS();
		ParamBO param = buildParamBO("key", true);
		Mockito.when(paramsBS.getParamDAO().getParam("key")).thenReturn(param);
		BusinessException businessException = null;
		
		try{
			paramsBS.updateParam("key", 1.2);
		}catch(BusinessException caughtException){
			businessException = caughtException;
		}
		
		Assert.assertNotNull(businessException);
		Assert.assertEquals("Parameter key is not a real number", businessException.getMessage());
	}
	
	@Test
	public void testUpdateDoubleParam_Fail() throws Exception{
		ParamsBS paramsBS = buildParamsBS();
		ParamBO param = buildParamBO("key", 1.0);
		Mockito.when(paramsBS.getParamDAO().getParam("key")).thenReturn(param);
		BusinessException businessException = null;
		
		try{
			paramsBS.updateParam("key", 4);
		}catch(BusinessException caughtException){
			businessException = caughtException;
		}
		
		Assert.assertNotNull(businessException);
		Assert.assertEquals("Parameter key is not an integer", businessException.getMessage());
	}
	
	@Test
	public void testUpdateIntegerParam_Fail() throws Exception{
		ParamsBS paramsBS = buildParamsBS();
		ParamBO param = buildParamBO("key", 3);
		Mockito.when(paramsBS.getParamDAO().getParam("key")).thenReturn(param);
		BusinessException businessException = null;
		
		try{
			paramsBS.updateParam("key", new Date(7200000));
		}catch(BusinessException caughtException){
			businessException = caughtException;
		}
		
		Assert.assertNotNull(businessException);
		Assert.assertEquals("Parameter key is not a date", businessException.getMessage());
	}
	
	@Test
	public void testUpdateDateParam_Fail() throws Exception{
		ParamsBS paramsBS = buildParamsBS();
		ParamBO param = buildParamBO("key", new Date(3600000));
		Mockito.when(paramsBS.getParamDAO().getParam("key")).thenReturn(param);
		BusinessException businessException = null;
		
		try{
			paramsBS.updateParam("key", "newStringValue");
		}catch(BusinessException caughtException){
			businessException = caughtException;
		}
		
		Assert.assertNotNull(businessException);
		Assert.assertEquals("Parameter key is not a string", businessException.getMessage());
	}
	
	@Test
	public void testUpdateStringParam_Fail() throws Exception{
		ParamsBS paramsBS = buildParamsBS();
		ParamBO param = buildParamBO("key", "stringValue");
		Mockito.when(paramsBS.getParamDAO().getParam("key")).thenReturn(param);
		BusinessException businessException = null;
		
		try{
			paramsBS.updateParam("key", true);
		}catch(BusinessException caughtException){
			businessException = caughtException;
		}
		
		Assert.assertNotNull(businessException);
		Assert.assertEquals("Parameter key is not a boolean", businessException.getMessage());
	}
	
	
	private ParamBO buildParamBO(String key, Object value){
		ParamBO param = new ParamBO();
		param.setKey(key);
		param.setValue(value);
		return param;
	}
	
	public static ParamsBS buildParamsBS(){
		ParamsBS paramsBS = new ParamsBS();
		paramsBS.setParamDAO(Mockito.mock(IParamDAO.class));
		return paramsBS;
	}

}
