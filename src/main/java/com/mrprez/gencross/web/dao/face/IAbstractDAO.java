package com.mrprez.gencross.web.dao.face;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public interface IAbstractDAO {
	
	public Session getSession();
	public Transaction getTransaction();
	public SessionFactory getSessionFactory();

}
