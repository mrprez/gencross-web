package com.mrprez.gencross.web.bs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Iterator;

import org.springframework.web.context.ContextLoader;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.disk.PersonnageFactory;
import com.mrprez.gencross.disk.PersonnageSaver;
import com.mrprez.gencross.history.HistoryItem;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IGcrFileBS;
import com.mrprez.gencross.web.dao.face.IPersonnageDAO;

public class GcrFileBS implements IGcrFileBS {
	private static final String DEFAULT_PASSWORD = "Sa2gle2m.";
	private PersonnageFactory personnageFactory;
	
	
	@Override
	public byte[] createPersonnageGcrAsPlayer(Integer personnageWorkId, UserBO user) throws Exception{
		IPersonnageDAO personnageDAO = (IPersonnageDAO) ContextLoader.getCurrentWebApplicationContext().getBean("PersonnageDAO");
		PersonnageWorkBO personnageWork = personnageDAO.loadPersonnageWork(personnageWorkId);
		if(personnageWork==null){
			return null;
		}
		
		return createPersonnageGcrAsPlayer(personnageWork, user);
	}
	
	@Override
	public byte[] createPersonnageGcrAsPlayer(PersonnageWorkBO personnageWork, UserBO user) throws Exception{
		if(personnageWork.getPlayer()==null || !personnageWork.getPlayer().equals(user)){
			return null;
		}
		Personnage personnage = personnageWork.getPersonnage();
		personnage.setPassword(DEFAULT_PASSWORD);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PersonnageSaver.savePersonnageGcr(personnage, baos);
		return baos.toByteArray();
	}

	@Override
	public byte[] createPersonnageGcrAsGameMaster(Integer personnageWorkId, UserBO user, String password) throws Exception {
		IPersonnageDAO personnageDAO = (IPersonnageDAO) ContextLoader.getCurrentWebApplicationContext().getBean("PersonnageDAO");
		PersonnageWorkBO personnageWork = personnageDAO.loadPersonnageWork(personnageWorkId);
		if(personnageWork==null){
			return null;
		}
		
		return createPersonnageGcrAsGameMaster(personnageWork, user, password);
	}

	@Override
	public byte[] createPersonnageGcrAsGameMaster(PersonnageWorkBO personnageWork, UserBO user, String password) throws Exception {
		if(personnageWork.getGameMaster()==null || !personnageWork.getGameMaster().equals(user)){
			return null;
		}
		Personnage personnage = personnageWork.getPersonnage();
		personnage.setPassword(password);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PersonnageSaver.savePersonnageGcr(personnage, baos);
		return baos.toByteArray();
	}
	
	@Override
	public PersonnageWorkBO createPersonnageAsPlayer(File gcrFile, String personnageName, UserBO user) throws Exception {
		Personnage personnage = personnageFactory.loadPersonnageFromGcr(gcrFile);
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setName(personnageName);
		personnageWork.setPlayer(user);
		personnageWork.getPersonnageData().setPersonnage(personnage);
		personnageWork.getValidPersonnageData().setPersonnage(personnage.clone());
		personnageWork.setPluginName(personnage.getPluginDescriptor().getName());
		
		IPersonnageDAO personnageDAO = (IPersonnageDAO) ContextLoader.getCurrentWebApplicationContext().getBean("PersonnageDAO");
		personnageDAO.savePersonnage(personnageWork.getPersonnageData());
		personnageDAO.savePersonnage(personnageWork.getValidPersonnageData());
		personnageDAO.savePersonnageWork(personnageWork);
		return personnageWork;
	}

	@Override
	public PersonnageWorkBO createPersonnageAsGameMaster(File gcrFile, String personnageName, UserBO user, String password) throws Exception {
		Personnage personnage = personnageFactory.loadPersonnageFromGcr(gcrFile);
		if(personnage.getPassword()!=null && !personnage.getPassword().equals(password)){
			return null;
		}
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setName(personnageName);
		personnageWork.setGameMaster(user);
		personnageWork.getPersonnageData().setPersonnage(personnage);
		personnageWork.getValidPersonnageData().setPersonnage(personnage.clone());
		personnageWork.setPluginName(personnage.getPluginDescriptor().getName());
		
		IPersonnageDAO personnageDAO = (IPersonnageDAO) ContextLoader.getCurrentWebApplicationContext().getBean("PersonnageDAO");
		personnageDAO.savePersonnage(personnageWork.getPersonnageData());
		personnageDAO.savePersonnage(personnageWork.getValidPersonnageData());
		personnageDAO.savePersonnageWork(personnageWork);
		
		return personnageWork;
	}
	
	public String uploadGcrPersonnage(File gcrFile, int personnageId, UserBO user, String password) throws Exception{
		IPersonnageDAO personnageDAO = (IPersonnageDAO) ContextLoader.getCurrentWebApplicationContext().getBean("PersonnageDAO");
		PersonnageWorkBO personnageWork = personnageDAO.loadPersonnageWork(personnageId);
		
		if(personnageWork.getGameMaster()!=null && personnageWork.getGameMaster().equals(user)){
			return uploadGcrPersonnageAsGameMaster(gcrFile, personnageWork, password);
		}else if(personnageWork.getPlayer()!=null && personnageWork.getPlayer().equals(user)){
			return uploadGcrPersonnageAsPlayer(gcrFile, personnageWork);
		}else{
			return "Ce personnage ne vous appartient pas";
		}
	}
	
	private String uploadGcrPersonnageAsGameMaster(File gcrFile, PersonnageWorkBO personnageWork, String password) throws Exception{
		Personnage personnage = personnageFactory.loadPersonnageFromGcr(gcrFile);
		if(personnage.getPassword()!=null && !personnage.getPassword().equals(password)){
			return "Mot de passe incorrect";
		}
		
		if(!personnage.getClass().equals(personnageWork.getPersonnage().getClass())){
			return "Ce fichier n'est pas un personnage "+personnageWork.getPersonnage().getClass().getSimpleName();
		}
		
		personnageWork.getPersonnageData().setPersonnage(personnage);
		
		IPersonnageDAO personnageDAO = (IPersonnageDAO) ContextLoader.getCurrentWebApplicationContext().getBean("PersonnageDAO");
		personnageDAO.savePersonnage(personnageWork.getPersonnageData());
		
		return null;
	}
	
	private String uploadGcrPersonnageAsPlayer(File gcrFile, PersonnageWorkBO personnageWork) throws Exception {
		Personnage uploadedPersonnage = personnageFactory.loadPersonnageFromGcr(gcrFile);
		
		Personnage personnage = personnageWork.getPersonnage();
		if(!personnage.getClass().equals(personnageWork.getPersonnage().getClass())){
			return "Ce fichier n'est pas un personnage "+personnageWork.getPersonnage().getClass().getSimpleName();
		}
		
		Iterator<HistoryItem> uploadedHistoryIt = uploadedPersonnage.getHistory().iterator();
		Iterator<HistoryItem> historyIt = personnage.getHistory().iterator();
		
		// On controle que l'historique a les mêmes dates jusqu'à la dernière validation du MJ.
		boolean isValidationDatePassed = (personnageWork.getValidationDate()==null);
		while(historyIt.hasNext() && uploadedHistoryIt.hasNext() && !isValidationDatePassed){
			HistoryItem historyItem = historyIt.next();
			HistoryItem uploadedHistoryItem = uploadedHistoryIt.next();
			if(historyItem.getDate().after(personnageWork.getValidationDate())){
				isValidationDatePassed = true;
			}else if(!historyItem.getDate().equals(uploadedHistoryItem.getDate())){
				return "Il ne s'agit pas du personnage: "+personnageWork.getName();
			}
		}
		if(historyIt.hasNext() && !isValidationDatePassed){
			return "Ce fichier date d'avant la dernière validation du MJ, vous ne pouvez pas le charger en tant que joueur.";
		}
		
		personnageWork.getPersonnageData().setPersonnage(uploadedPersonnage);
		
		return null;
	}

	public PersonnageFactory getPersonnageFactory() {
		return personnageFactory;
	}

	public void setPersonnageFactory(PersonnageFactory personnageFactory) {
		this.personnageFactory = personnageFactory;
	}
	

}
