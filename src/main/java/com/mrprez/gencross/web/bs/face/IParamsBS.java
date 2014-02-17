package com.mrprez.gencross.web.bs.face;

import java.util.Collection;
import java.util.Date;

import com.mrprez.gencross.web.bo.ParamBO;

public interface IParamsBS {
	
	
	ParamBO getParam(String key) throws Exception;
	
	Collection<ParamBO> getAllParms() throws Exception;

	ParamBO updateParam(String key, Date date) throws Exception;

	ParamBO updateParam(String key, Boolean b) throws Exception;

	ParamBO updateParam(String key, Integer integer) throws Exception;

	ParamBO updateParam(String key, Double d) throws Exception;

	ParamBO updateParam(String key, String newValue) throws Exception;

}
