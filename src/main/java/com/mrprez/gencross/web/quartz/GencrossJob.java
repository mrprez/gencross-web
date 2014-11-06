package com.mrprez.gencross.web.quartz;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public abstract class GencrossJob implements Job {
	
	public static final String LAST_EXECUTION_DATE = "lastExecutionDate";
	public static final String IS_RUNNING = "isRunning";

	private static Map<Class<? extends GencrossJob>, Date> lastExecutionDates = new HashMap<Class<? extends GencrossJob>, Date>();
	private static Set<Class<? extends GencrossJob>> runningJobs = new HashSet<Class<? extends GencrossJob>>();
	private static Map<Class<? extends GencrossJob>, Throwable> exceptions = new HashMap<Class<? extends GencrossJob>,Throwable>();
	
	
	@Override
	public synchronized void execute(JobExecutionContext context) throws JobExecutionException {
		lastExecutionDates.put(this.getClass(), new Date());
		exceptions.remove(this.getClass());
		runningJobs.add(this.getClass());
		try{
			process(context);
		}catch(Throwable throwable){
			exceptions.put(this.getClass(), throwable);
			Logger.getLogger("quartz").error(throwable.getMessage(), throwable);
			throw new JobExecutionException(throwable);
		}finally{
			runningJobs.remove(this.getClass());
		}
	}
	
	
	public abstract void process(JobExecutionContext context) throws JobExecutionException;
	
	public static Date getLastExecutionDate(Class<? extends GencrossJob> clazz){
		return lastExecutionDates.get(clazz);
	}
	
	public static boolean isRunning(Class<? extends GencrossJob> clazz){
		return runningJobs.contains(clazz);
	}


	public static Throwable getException(Class<? extends GencrossJob> clazz) {
		return exceptions.get(clazz);
	}

}
