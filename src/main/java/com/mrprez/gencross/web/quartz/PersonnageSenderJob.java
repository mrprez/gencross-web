package com.mrprez.gencross.web.quartz;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.context.ContextLoader;

import com.mrprez.gencross.disk.PersonnageSaver;
import com.mrprez.gencross.web.bo.ParamBO;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.dao.ParamDAO;
import com.mrprez.gencross.web.dao.PersonnageDAO;
import com.mrprez.gencross.web.dao.face.IMailResource;

public class PersonnageSenderJob implements Job {
	private SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
	private static Date lastExecutionDate;
	

	@Override
	public synchronized void execute(JobExecutionContext context) throws JobExecutionException {
		Logger.getLogger("senderJob").info("START PersonnageSenderJob");
		IMailResource mailResource = (IMailResource) ContextLoader.getCurrentWebApplicationContext().getBean("MailResource");
		String toAdresse = context.getJobDetail().getJobDataMap().getString("toAdress");
		PersonnageDAO personnageDAO = new PersonnageDAO();
		personnageDAO.setSessionFactory(sessionFactory);
		ParamDAO paramDAO = new ParamDAO();
		paramDAO.setSessionFactory(sessionFactory);
		
		try{
			Date startDate = new Date();
			
			ParamBO lastSendDateParam = paramDAO.getParam(ParamBO.LAST_SEND_DATE_KEY);
			if(lastSendDateParam==null){
				lastSendDateParam = new ParamBO();
				lastSendDateParam.setKey(ParamBO.LAST_SEND_DATE_KEY);
			}
			Logger.getLogger("senderJob").info(ParamBO.LAST_SEND_DATE_KEY+"="+lastSendDateParam.getValue());
			
			Collection<PersonnageWorkBO> personnageList;
			if(lastSendDateParam.getValue()==null){
				personnageList = personnageDAO.getAllPersonnages();
			}else{
				personnageList = personnageDAO.getLastModified((Date) lastSendDateParam.getValue());
			}
			Logger.getLogger("senderJob").info(""+personnageList.size()+" personnages modifiés");
			
			for(PersonnageWorkBO personnageWork : personnageList){
				try{
					Logger.getLogger("senderJob").info("ID du personnage="+personnageWork.getId());
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					PersonnageSaver.savePersonnageGcr(personnageWork.getPersonnage(), baos);
					StringBuilder text = new StringBuilder("Personnage: ");
					text.append(personnageWork.getName());
					if(personnageWork.getGameMaster()!=null){
						text.append("\nMJ: ").append(personnageWork.getGameMaster().getUsername());
					}
					if(personnageWork.getPlayer()!=null){
						text.append("\nJoueur: ").append(personnageWork.getPlayer().getUsername());
					}
					mailResource.send(toAdresse, personnageWork.getName(), text.toString(), personnageWork.getName()+".gcr", baos.toByteArray());
					Thread.sleep(20000);
				}catch (Exception e) {
					Logger.getLogger("senderJob").error(e.getMessage(), e);
					mailResource.sendError(e);
				}
			}
			personnageDAO.getTransaction().rollback();
			
			lastSendDateParam.setValue(startDate);
			paramDAO.save(lastSendDateParam);
			
			paramDAO.getTransaction().commit();
			
			lastExecutionDate = new Date();
			Logger.getLogger("senderJob").info("END PersonnageSenderJob");
			
		}catch (Exception e) {
			Logger.getLogger("senderJob").error(e.getMessage(), e);
			try {
				mailResource.sendError(e);
			} catch (Exception e1) {
				e1.printStackTrace();
				throw new JobExecutionException(e1);
			}
			paramDAO.getTransaction().rollback();
		}
	}


	public static Date getLastExecutionDate() {
		return lastExecutionDate;
	}
	
	
	

}
