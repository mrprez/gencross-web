package com.mrprez.gencross.web.bs;

import java.util.Collection;
import java.util.Date;

import org.springframework.web.context.ContextLoader;

import com.mrprez.gencross.web.bo.ParamBO;
import com.mrprez.gencross.web.bs.face.IParamsBS;
import com.mrprez.gencross.web.bs.util.BusinessException;
import com.mrprez.gencross.web.dao.face.IParamDAO;

public class ParamsBS implements IParamsBS {

	@Override
	public ParamBO getParam(String key) throws Exception {
		IParamDAO paramDAO = (IParamDAO) ContextLoader.getCurrentWebApplicationContext().getBean("ParamDAO");
		return paramDAO.getParam(key);
	}

	@Override
	public Collection<ParamBO> getAllParms() throws Exception {
		IParamDAO paramDAO = (IParamDAO) ContextLoader.getCurrentWebApplicationContext().getBean("ParamDAO");
		return paramDAO.getAllParams();
	}

	@Override
	public ParamBO updateParam(String key, Date date) throws Exception {
		IParamDAO paramDAO = (IParamDAO) ContextLoader.getCurrentWebApplicationContext().getBean("ParamDAO");
		ParamBO param = paramDAO.getParam(key);
		if(param.getType()!=ParamBO.DATE_TYPE){
			throw new BusinessException("Parameter "+key+" is not a date");
		}
		param.setValue(date);
		return param;
	}

	@Override
	public ParamBO updateParam(String key, Boolean b) throws Exception {
		IParamDAO paramDAO = (IParamDAO) ContextLoader.getCurrentWebApplicationContext().getBean("ParamDAO");
		ParamBO param = paramDAO.getParam(key);
		if(param.getType()!=ParamBO.BOOLEAN_TYPE){
			throw new BusinessException("Parameter "+key+" is not a boolean");
		}
		param.setValue(b);
		return param;
	}

	@Override
	public ParamBO updateParam(String key, Integer integer) throws Exception {
		IParamDAO paramDAO = (IParamDAO) ContextLoader.getCurrentWebApplicationContext().getBean("ParamDAO");
		ParamBO param = paramDAO.getParam(key);
		if(param.getType()!=ParamBO.INTEGER_TYPE){
			throw new BusinessException("Parameter "+key+" is not an integer");
		}
		param.setValue(integer);
		return param;
	}

	@Override
	public ParamBO updateParam(String key, Double d) throws Exception {
		IParamDAO paramDAO = (IParamDAO) ContextLoader.getCurrentWebApplicationContext().getBean("ParamDAO");
		ParamBO param = paramDAO.getParam(key);
		if(param.getType()!=ParamBO.REAL_TYPE){
			throw new BusinessException("Parameter "+key+" is not a real number");
		}
		param.setValue(d);
		return param;
	}

	@Override
	public ParamBO updateParam(String key, String newValue) throws Exception {
		IParamDAO paramDAO = (IParamDAO) ContextLoader.getCurrentWebApplicationContext().getBean("ParamDAO");
		ParamBO param = paramDAO.getParam(key);
		if(param.getType()!=ParamBO.STRING_TYPE){
			throw new BusinessException("Parameter "+key+" is not a string");
		}
		param.setValue(newValue);
		return param;
	}
	
	

}
