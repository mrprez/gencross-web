package com.mrprez.gencross.web.bs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.PoolPoint;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.disk.PersonnageFactory;
import com.mrprez.gencross.disk.PersonnageSaver;
import com.mrprez.gencross.disk.PluginDescriptor;
import com.mrprez.gencross.exception.PersonnageVersionException;
import com.mrprez.gencross.history.HistoryItem;
import com.mrprez.gencross.value.DoubleValue;
import com.mrprez.gencross.value.IntValue;
import com.mrprez.gencross.value.StringValue;
import com.mrprez.gencross.value.Value;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.PersonnageXmlBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.web.dao.face.IMailResource;
import com.mrprez.gencross.web.dao.face.IPersonnageDAO;
import com.mrprez.gencross.web.dao.face.IUserDAO;

public class PersonnageBS implements IPersonnageBS {
	private PersonnageFactory personnageFactory;
	private IPersonnageDAO personnageDAO;
	private IMailResource mailResource;
	private IUserDAO userDAO;
	
	
	@Override
	public PersonnageWorkBO createPersonnageAsPlayer(String pluginName, String name, String gmName, UserBO user) throws Exception {
		PluginDescriptor pluginDescriptor = personnageFactory.getPluginDescriptor(pluginName);
		Personnage personnage = personnageFactory.buildNewPersonnage(pluginDescriptor);
		
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setPlayer(user);
		personnageWork.setName(name);
		personnageWork.getPersonnageData().setPersonnage(personnage);
		personnageWork.getValidPersonnageData().setPersonnage(personnage.clone());
		personnageWork.setPluginName(personnage.getPluginDescriptor().getName());
		
		personnageDAO.savePersonnage(personnageWork.getPersonnageData());
		personnageDAO.savePersonnage(personnageWork.getValidPersonnageData());
		personnageDAO.savePersonnageWork(personnageWork);
		
		if(gmName!=null){
			UserBO gm = userDAO.getUser(gmName);
			attribute(personnageWork, user, gm);
		}
		
		return personnageWork;
	}
	
	@Override
	public PersonnageWorkBO createPersonnageAsGameMaster(String pluginName, String name, String playerName, UserBO user) throws Exception {
		PluginDescriptor pluginDescriptor = personnageFactory.getPluginDescriptor(pluginName);
		Personnage personnage = personnageFactory.buildNewPersonnage(pluginDescriptor);
		
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(user);
		personnageWork.setName(name);
		personnageWork.getPersonnageData().setPersonnage(personnage);
		personnageWork.getValidPersonnageData().setPersonnage(personnage.clone());
		personnageWork.setPluginName(personnage.getPluginDescriptor().getName());
		
		personnageDAO.savePersonnage(personnageWork.getPersonnageData());
		personnageDAO.savePersonnage(personnageWork.getValidPersonnageData());
		personnageDAO.savePersonnageWork(personnageWork);
		
		if(playerName!=null){
			UserBO player = userDAO.getUser(playerName);
			attribute(personnageWork, player, user);
		}
		
		return personnageWork;
	}
	
	@Override
	public PersonnageWorkBO createPersonnageAsGameMasterAndPlayer(String pluginName, String name, UserBO user) throws Exception{
		PluginDescriptor pluginDescriptor = personnageFactory.getPluginDescriptor(pluginName);
		Personnage personnage = personnageFactory.buildNewPersonnage(pluginDescriptor);
		
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(user);
		personnageWork.setPlayer(user);
		personnageWork.setName(name);
		personnageWork.getPersonnageData().setPersonnage(personnage);
		personnageWork.getValidPersonnageData().setPersonnage(personnage.clone());
		personnageWork.setPluginName(personnage.getPluginDescriptor().getName());
		
		personnageDAO.savePersonnage(personnageWork.getPersonnageData());
		personnageDAO.savePersonnage(personnageWork.getValidPersonnageData());
		personnageDAO.savePersonnageWork(personnageWork);
		
		return personnageWork;
	}
	
	@Override
	public void deletePersonnageFromUser(int personnageWorkId, UserBO user) throws Exception{
		PersonnageWorkBO personnageWork = personnageDAO.loadPersonnageWork(personnageWorkId);
		
		if(personnageWork.getGameMaster()!=null && personnageWork.getGameMaster().getUsername().equals(user.getUsername())){
			if(personnageWork.getPlayer()!=null){
				personnageWork.setGameMaster(null);
			}else{
				personnageDAO.deletePersonnage(personnageWork);
			}
		}
		if(personnageWork.getPlayer()!=null && personnageWork.getPlayer().getUsername().equals(user.getUsername())){
			if(personnageWork.getGameMaster()!=null){
				personnageWork.setPlayer(null);
			}else{
				personnageDAO.deletePersonnage(personnageWork);
			}
		}
	}

	@Override
	public Collection<PluginDescriptor> getAvailablePersonnageTypes() throws Exception {
		Collection<PluginDescriptor> result = new TreeSet<PluginDescriptor>(new PluginDescriptor.PluginComparator());
		result.addAll(personnageFactory.getPluginList());
		return result;
	}
	
	@Override
	public boolean setNewValue(PersonnageWorkBO personnageWork, String newValueString, String absoluteName) throws Exception {
		Personnage personnage = personnageWork.getPersonnage();
		Property property = personnage.getProperty(absoluteName);
		Value newValue = property.getValue().clone();
		newValue.setValue(newValueString);
		if(property.getOptions()!=null){
			if(!property.getOptions().contains(newValue)){
				return false;
			}
		}
		if(property.getValue().getOffset()!=null){// Si il y a un offset de définit, on vérifie que la différence entre l'ancienne et la nouvelle valeur est un nombre entier d'offset
			if(property.getValue() instanceof DoubleValue){
				if((newValue.getDouble()-property.getValue().getDouble()) % ((DoubleValue)property.getValue()).getOffset()!=0){
					return false;
				}
			}
			if(property.getValue() instanceof IntValue){
				if((newValue.getInt()-property.getValue().getInt()) % ((IntValue)property.getValue()).getOffset()!=0){
					return false;
				}
			}
			if(property.getValue() instanceof StringValue){
				if(!newValue.getString().matches("["+newValue.getOffset().toString()+"]*")){
					return false;
				}
			}
		}
		boolean success = personnage.setNewValue(property, newValue);
		if(success){
			personnageDAO.savePersonnage(personnageWork.getPersonnageData());
		}
		return success;
	}

	@Override
	public void savePersonnage(PersonnageWorkBO personnageWork)throws Exception {
		personnageDAO.savePersonnage(personnageWork.getPersonnageData());
	}
	
	@Override
	public void savePersonnageWork(PersonnageWorkBO personnageWork)throws Exception {
		personnageDAO.savePersonnageWork(personnageWork);
	}
	
	@Override
	public void validatePersonnage(PersonnageWorkBO personnageWork)throws Exception {
		personnageDAO.savePersonnageWork(personnageWork);
		personnageWork.getValidPersonnageData().setPersonnage(personnageWork.getPersonnage().clone());
		personnageWork.setValidationDate(new Date(System.currentTimeMillis()));
	}
	
	@Override
	public void unvalidatePersonnage(PersonnageWorkBO personnageWork)throws Exception {
		personnageDAO.savePersonnage(personnageWork.getPersonnageData());
		personnageDAO.savePersonnage(personnageWork.getValidPersonnageData());
		personnageDAO.savePersonnageWork(personnageWork);
		personnageWork.getPersonnageData().setPersonnage(personnageWork.getValidPersonnage().clone());
	}

	@Override
	public List<PersonnageWorkBO> getPlayerPersonnageList(UserBO player) throws Exception {
		return personnageDAO.getPlayerPersonnageList(player);
	}
	
	@Override
	public List<PersonnageWorkBO> getGameMasterPersonnageList(UserBO gameMaster) throws Exception {
		return personnageDAO.getGameMasterPersonnageList(gameMaster);
	}
	
	@Override
	public PersonnageWorkBO loadPersonnage(Integer personnageId) throws Exception {
		return personnageDAO.loadPersonnageWork(personnageId);
	}

	@Override
	public PersonnageWorkBO loadPersonnage(Integer personnageId, UserBO user) throws Exception {
		if(personnageId==null){
			return null;
		}
		PersonnageWorkBO personnageWork = personnageDAO.loadPersonnageWork(personnageId);
		if(personnageWork.getPlayer()!=null && personnageWork.getPlayer().equals(user)){
			return personnageWork;
		}
		if(personnageWork.getGameMaster()!=null && personnageWork.getGameMaster().equals(user)){
			return personnageWork;
		}
		return null;
	}
	
	@Override
	public PersonnageWorkBO loadPersonnageAsPlayer(Integer personnageId, UserBO player) throws Exception {
		if(personnageId==null){
			return null;
		}
		PersonnageWorkBO personnageWork = personnageDAO.loadPersonnageWork(personnageId);
		if(personnageWork.getPlayer()==null){
			return null;
		}
		if(!personnageWork.getPlayer().getUsername().equals(player.getUsername())){
			return null;
		}
		return personnageWork;
	}
	
	@Override
	public PersonnageWorkBO loadPersonnageAsGameMaster(Integer personnageId, UserBO gameMaster) throws Exception {
		if(personnageId==null){
			return null;
		}
		PersonnageWorkBO personnageWork = personnageDAO.loadPersonnageWork(personnageId);
		if(personnageWork.getGameMaster()==null){
			return null;
		}
		if(!personnageWork.getGameMaster().getUsername().equals(gameMaster.getUsername())){
			return null;
		}
		return personnageWork;
	}

	@Override
	public boolean addPropertyFromOption(PersonnageWorkBO personnageWork, String motherPropertyName, String optionName, String specification) throws Exception {
		Personnage personnage = personnageWork.getPersonnage();
		Property motherProperty = personnage.getProperty(motherPropertyName);
		if(motherProperty==null){
			return false;
		}
		if(motherProperty.getSubProperties()==null){
			return false;
		}
		Property newProperty = motherProperty.getSubProperties().getOptions().get(optionName);
		if(newProperty==null){
			return false;
		}
		newProperty = newProperty.clone();
		if(newProperty.getSpecification()!=null){
			if(specification==null || specification.trim().isEmpty()){
				personnage.setActionMessage("Specifiez la propriété.");
				return false;
			}
			newProperty.setSpecification(specification);
		}
		boolean success = personnage.addPropertyToMotherProperty(newProperty);
		if(success){
			personnageDAO.savePersonnage(personnageWork.getPersonnageData());
		}
		return success;
	}

	@Override
	public boolean addFreeProperty(PersonnageWorkBO personnageWork, String motherPropertyName, String newPropertyName) throws Exception {
		Personnage personnage = personnageWork.getPersonnage();
		Property motherProperty = personnage.getProperty(motherPropertyName);
		if(motherProperty==null){
			return false;
		}
		if(motherProperty.getSubProperties()==null){
			return false;
		}
		if(motherProperty.getSubProperties().getDefaultProperty()==null){
			return false;
		}
		Property newProperty = motherProperty.getSubProperties().getDefaultProperty().clone();
		newProperty.setName(newPropertyName);
		boolean success = personnage.addPropertyToMotherProperty(newProperty);
		if(success){
			personnageDAO.savePersonnage(personnageWork.getPersonnageData());
		}
		return success;
	}

	@Override
	public void nextPhase(PersonnageWorkBO personnageWork) throws Exception {
		personnageWork.getPersonnage().passToNextPhase();
		personnageDAO.savePersonnage(personnageWork.getPersonnageData());
	}

	@Override
	public boolean removeProperty(PersonnageWorkBO personnageWork, String propertyAbsoluteName) throws Exception {
		Personnage personnage = personnageWork.getPersonnage();
		Property property = personnage.getProperty(propertyAbsoluteName);
		boolean success = personnageWork.getPersonnage().removePropertyFromMotherProperty(property);
		if(success){
			personnageDAO.savePersonnage(personnageWork.getPersonnageData());
		}
		return success;
	}

	@Override
	public void modifyPointPool(PersonnageWorkBO personnageWork, String pointPoolName, Integer modification) throws Exception {
		personnageDAO.savePersonnage(personnageWork.getPersonnageData());
		
		PoolPoint pointPool = personnageWork.getPersonnage().getPointPools().get(pointPoolName);
		if(pointPool==null){
			return;
		}
		pointPool.add(modification.intValue());
		personnageWork.getPersonnage().calculate();
	}

	@Override
	public void modifyHistory(PersonnageWorkBO personnageWork,String pointPoolName, Integer cost, int index) throws Exception {
		Personnage personnage = personnageWork.getPersonnage();
		HistoryItem historyItem = personnage.getHistory().get(index);
		
		// On recrédite
		PoolPoint poolPoint = personnage.getPointPools().get(historyItem.getPointPool());
		if(poolPoint!=null){
			poolPoint.spend(-historyItem.getCost());
		}
		
		// On débite
		PoolPoint newPoolPoint = personnage.getPointPools().get(pointPoolName);
		if(newPoolPoint!=null){
			newPoolPoint.spend(cost);
		}
		
		historyItem.setPointPool(pointPoolName);
		historyItem.setCost(cost);
		
		personnageDAO.savePersonnage(personnageWork.getPersonnageData());
	}
	
	@Override
	public void attribute(PersonnageWorkBO personnageWork, UserBO player, UserBO gameMaster) throws Exception{
		personnageDAO.savePersonnageWork(personnageWork);
		if(personnageWork.getGameMaster()!=null && !personnageWork.getGameMaster().equals(gameMaster)){
			mailResource.send(personnageWork.getGameMaster().getMail(), "GenCrossWeb - "+personnageWork.getName(), "Le joueur "+player.getUsername()+" vous a retiré les droits de Maître de Jeu pour le personnage "+personnageWork.getName()+".");
			personnageWork.setGameMaster(null);
		}
		if(gameMaster!=null && !gameMaster.equals(personnageWork.getGameMaster())){
			mailResource.send(gameMaster.getMail(), "GenCrossWeb - "+personnageWork.getName(), "Le joueur "+player.getUsername()+" vous a attribué les droits de Maître de Jeu pour le personnage "+personnageWork.getName()+".");
			personnageWork.setGameMaster(gameMaster);
		}
		if(personnageWork.getPlayer()!=null && !personnageWork.getPlayer().equals(player)){
			mailResource.send(personnageWork.getPlayer().getMail(), "GenCrossWeb - "+personnageWork.getName(), "Le Maître de Jeu "+gameMaster.getUsername()+" vous a retiré le personnage "+personnageWork.getName()+".");
			personnageWork.setPlayer(null);
		}
		if(player!=null && !player.equals(personnageWork.getPlayer())){
			mailResource.send(player.getMail(), "GenCrossWeb - "+personnageWork.getName(), "Le Maître de Jeu "+gameMaster.getUsername()+" vous a attribué le personnage "+personnageWork.getName()+".");
			personnageWork.setPlayer(player);
		}
	}
	
	@Override
	public void migrate() throws Exception{
		PersonnageFactory migrationPersonnageFactory = new PersonnageFactory(true);
		for(PersonnageXmlBO personnageXml : personnageDAO.getAllXml()){
			InputStream is = new ByteArrayInputStream(personnageXml.getXml());
			try{
				personnageFactory.loadPersonnage(is);
			}catch(PersonnageVersionException pve){
				Logger.getLogger(getClass()).info("Try to migrate personnage "+personnageXml.getId());
				is = new ByteArrayInputStream(personnageXml.getXml());
				Personnage personnage = migrationPersonnageFactory.loadPersonnage(is);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				PersonnageSaver.savePersonnage(personnage, baos);
				personnageXml.setXml(baos.toByteArray());
				personnageDAO.savePersonnageXml(personnageXml);
			}
			
		}
	}
	
	@Override
	public Collection<PersonnageWorkBO> getPersonnageListFromType(String type) throws Exception {
		return personnageDAO.getPersonnageListFromType(type);
	}

	public PersonnageFactory getPersonnageFactory() {
		return personnageFactory;
	}
	public void setPersonnageFactory(PersonnageFactory personnageFactory) {
		this.personnageFactory = personnageFactory;
	}

	@Override
	public void savePersonnageBackground(PersonnageWorkBO personnageWork, String background) throws Exception {
		personnageDAO.savePersonnageWork(personnageWork);
		personnageWork.setBackground(background);
	}

	public IPersonnageDAO getPersonnageDAO() {
		return personnageDAO;
	}

	public void setPersonnageDAO(IPersonnageDAO personnageDAO) {
		this.personnageDAO = personnageDAO;
	}

	public IMailResource getMailResource() {
		return mailResource;
	}

	public void setMailResource(IMailResource mailResource) {
		this.mailResource = mailResource;
	}

	public IUserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(IUserDAO userDAO) {
		this.userDAO = userDAO;
	}

	

}
