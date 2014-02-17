package com.mrprez.gencross.web.dao.util;

import org.aspectj.lang.ProceedingJoinPoint;

public class ExceptionManagerDAO {
	
	public Object catchException(ProceedingJoinPoint proceedingJoinPoint) throws DaoException{
		try{
			return proceedingJoinPoint.proceed();
		}catch(Throwable e){
			e.printStackTrace();
			throw new DaoException(e);
		}
	}

}
