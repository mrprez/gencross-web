package com.mrprez.gencross.web.quartz;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.context.ContextLoader;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.dao.PersonnageDAO;
import com.mrprez.gencross.web.dao.face.IMailResource;
import com.mrprez.gencross.web.dao.face.ISaveRepositoryResource;

@DisallowConcurrentExecution
public class PersonnageSaverJob extends GencrossJob {
	
	
	@Override
	public void process(JobExecutionContext context) throws JobExecutionException {
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		try{
			ISaveRepositoryResource saveRepositoryResource = (ISaveRepositoryResource)ContextLoader.getCurrentWebApplicationContext().getBean("SaveRepositoryResource");
			saveRepositoryResource.cleanOldRepository();
			saveRepositoryResource.moveFilesToOldRepository();
			
			PersonnageDAO personnageDAO = new PersonnageDAO();
			personnageDAO.setSessionFactory(sessionFactory);
			
			for(PersonnageWorkBO personnageWork : personnageDAO.getAllPersonnages()){
				saveRepositoryResource.savePersonnage(personnageWork.getPersonnage(), personnageWork.getId().toString());
			}
			
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
		}finally{
			sessionFactory.getCurrentSession().getTransaction().rollback();
		}
		
		
	}

}
