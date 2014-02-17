package com.mrprez.gencross.web.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.mrprez.gencross.web.bo.RoleBO;
import com.mrprez.gencross.web.dao.face.IRoleDAO;

public class RoleDAO extends AbstractDAO implements IRoleDAO {
	
	public RoleBO getRole(String name) throws Exception{
		Criteria criteria = getSession().createCriteria(RoleBO.class);
		criteria.add(Restrictions.eq("name", name));
		
		RoleBO result = (RoleBO) criteria.uniqueResult();
		
		return result;
		
	}

}
