package com.mrprez.gencross.web.dao;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

import com.mrprez.gencross.web.bo.ParamBO;
import com.mrprez.gencross.web.dao.face.IParamDAO;

public class ParamDAO extends AbstractDAO implements IParamDAO {

	@Override
	public ParamBO getParam(String key) throws Exception {
		return (ParamBO) getSession().get(ParamBO.class, key);
	}
	
	@Override
	public void save(ParamBO paramBO){
		getSession().saveOrUpdate(paramBO);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<ParamBO> getAllParams() throws Exception {
		Criteria criteria = getSession().createCriteria(ParamBO.class);
		criteria.addOrder(Order.asc("key"));
		return criteria.list();
	}

}
