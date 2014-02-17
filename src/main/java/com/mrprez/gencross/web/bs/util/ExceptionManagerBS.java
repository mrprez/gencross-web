package com.mrprez.gencross.web.bs.util;

import org.aspectj.lang.ProceedingJoinPoint;

import com.mrprez.gencross.web.dao.util.DaoException;

public class ExceptionManagerBS {
	
	
	public Object catchException(ProceedingJoinPoint proceedingJoinPoint) throws BusinessException{
		try{
			return proceedingJoinPoint.proceed();
		}catch(DaoException e){
			throw new BusinessException(e);
		}catch(Throwable e){
			e.printStackTrace();
			throw new BusinessException(e);
		}
	}

}
