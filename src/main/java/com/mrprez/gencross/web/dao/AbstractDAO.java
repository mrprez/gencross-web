package com.mrprez.gencross.web.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.mrprez.gencross.web.dao.face.IAbstractDAO;

public abstract class AbstractDAO implements IAbstractDAO {
	private SessionFactory sessionFactory;
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		if(transactionManager instanceof HibernateTransactionManager){
			sessionFactory = ((HibernateTransactionManager)transactionManager).getSessionFactory();
		}
	}
	
	public Session getSession(){
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.getTransaction();
		if(!transaction.isActive()){
			transaction.begin();
		}
		return session;
	}
	
	public Transaction getTransaction(){
		return getSession().getTransaction();
	}

	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
}
