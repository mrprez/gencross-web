package com.mrprez.gencross.web.dao.face;

import java.util.Collection;

import com.mrprez.gencross.web.bo.ParamBO;

public interface IParamDAO extends IAbstractDAO {
	
	public ParamBO getParam(String key) throws Exception;
	
	public Collection<ParamBO> getAllParams() throws Exception;

	public void save(ParamBO paramBO);

}
