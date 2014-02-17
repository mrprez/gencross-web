package com.mrprez.gencross.web.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.dao.face.IUserDAO;

public class UserDAO extends AbstractDAO implements IUserDAO{
	
	public UserBO getUser(String username, String digest){
		Criteria criteria = getSession().createCriteria(UserBO.class);
		criteria.add(Restrictions.eq("username", username));
		criteria.add(Restrictions.eq("digest", digest));
		return (UserBO) criteria.uniqueResult();
	}

	@Override
	public UserBO saveUser(UserBO user) throws Exception {
		getSession().saveOrUpdate(user);
		return user;
	}

	@Override
	public UserBO getUser(String username) throws Exception {
		Criteria criteria = getSession().createCriteria(UserBO.class);
		criteria.add(Restrictions.eq("username", username));
		return (UserBO) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public UserBO getUserFromMail(String mail) throws Exception {
		Criteria criteria = getSession().createCriteria(UserBO.class);
		criteria.add(Restrictions.eq("mail", mail));
		List<UserBO> result = criteria.list();
		if(result.isEmpty()){
			return null;
		}
		return result.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserBO> getUserList() throws Exception {
		Criteria criteria = getSession().createCriteria(UserBO.class);
		criteria.setReadOnly(true);
		criteria.addOrder(Order.asc("username"));
		
		return criteria.list();
	}
	
	@Override
	public void deleteUser(UserBO user) throws Exception {
		getSession().delete(user);
	}

	

}
