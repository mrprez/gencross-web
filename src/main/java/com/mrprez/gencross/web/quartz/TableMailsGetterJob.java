package com.mrprez.gencross.web.quartz;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.context.ContextLoader;

import com.mrprez.gencross.web.bs.face.ITableBS;
import com.mrprez.gencross.web.dao.face.IMailResource;

@DisallowConcurrentExecution
public class TableMailsGetterJob implements Job {
	
	
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			ITableBS tableBS = (ITableBS)ContextLoader.getCurrentWebApplicationContext().getBean("tableBS");
			tableBS.connectTableMailBox();
		} catch (Exception e) {
			Logger.getLogger("senderJob").error(e.getMessage(), e);
			try {
				IMailResource mailResource = (IMailResource)ContextLoader.getCurrentWebApplicationContext().getBean("MailResource");
				mailResource.sendError(e);
			} catch (Exception e1) {
				e1.printStackTrace();
				throw new JobExecutionException(e1);
			}
			throw new JobExecutionException(e);
		}
		
	}

}
