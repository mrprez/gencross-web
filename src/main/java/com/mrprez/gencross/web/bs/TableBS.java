package com.mrprez.gencross.web.bs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.disk.PersonnageFactory;
import com.mrprez.gencross.web.bo.ParamBO;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.PlannedGameBO;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.TableMessageBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.ITableBS;
import com.mrprez.gencross.web.bs.util.BusinessException;
import com.mrprez.gencross.web.dao.face.IGoogleCalendarResource;
import com.mrprez.gencross.web.dao.face.IMailResource;
import com.mrprez.gencross.web.dao.face.IParamDAO;
import com.mrprez.gencross.web.dao.face.IPersonnageDAO;
import com.mrprez.gencross.web.dao.face.ITableDAO;
import com.mrprez.gencross.web.dao.face.IUserDAO;

public class TableBS implements ITableBS {
	private PersonnageFactory personnageFactory;
	private ITableDAO tableDAO;
	private IPersonnageDAO personnageDAO;
	private IMailResource mailResource;
	private IUserDAO userDAO;
	private IParamDAO paramDAO;
	private IGoogleCalendarResource calendarResource;
	

	@Override
	public TableBO createTable(String name, UserBO gm, String type) throws Exception {
		TableBO table = new TableBO();
		table.setName(name);
		table.setGameMaster(gm);
		table.setType(type);
		tableDAO.saveTable(table);
		
		return table;
	}
	
	@Override
	public void removeTable(Integer tableId, boolean deletePj, boolean deletePnj, UserBO user) throws Exception {
		TableBO table = tableDAO.loadTable(tableId);
		if( ! table.getGameMaster().equals(user)){
			throw new BusinessException("User is not table game master");
		}
		
		// TODO delete PJ and PNJ if necessary
		

		tableDAO.deleteTable(table);
	}
	
	@Override
	public Set<TableBO> getTableListForUser(UserBO user) throws Exception {
		Set<TableBO> result = new TreeSet<TableBO>();
		result.addAll(tableDAO.getTableFromGM(user));
		
		return result;
	}

	@Override
	public TableBO getTableForGM(Integer id, UserBO gameMaster) throws Exception {
		TableBO table = tableDAO.loadTable(id);
		if(!gameMaster.equals(table.getGameMaster())){
			return null;
		}
		table.getPersonnages().size();
		table.getMessages().size();
		return table;
	}
	
	@Override
	public Collection<PersonnageWorkBO> getPjList(Integer tableId) throws Exception {
		Collection<PersonnageWorkBO> result = new HashSet<PersonnageWorkBO>();
		TableBO table = tableDAO.loadTable(tableId);
		for(PersonnageWorkBO personnageWork : table.getPersonnages()){
			if(personnageWork.getPlayer() != null){
				personnageWork.getPersonnage();
				result.add(personnageWork);
			}
		}
		return result;
	}
	
	@Override
	public Collection<String> getPointPoolList(Integer tableId) throws Exception {
		TableBO table = tableDAO.loadTable(tableId);
		Personnage personnage = personnageFactory.buildNewPersonnage(table.getType());
		
		return personnage.getPointPools().keySet();
	}
	
	@Override
	public TableBO addNewPersonnageToTable(Integer tableId, String personnageName, UserBO gameMaster) throws Exception {
		TableBO table = tableDAO.loadTable(tableId);
		if(table==null || !gameMaster.equals(table.getGameMaster())){
			return null;
		}
		Personnage personnage = personnageFactory.buildNewPersonnage(table.getType());
		
		PersonnageWorkBO personnageWork = new PersonnageWorkBO();
		personnageWork.setGameMaster(table.getGameMaster());
		personnageWork.setName(personnageName);
		personnageWork.setPluginName(personnage.getPluginDescriptor().getName());
		personnageWork.getPersonnageData().setPersonnage(personnage);
		personnageWork.getValidPersonnageData().setPersonnage(personnage.clone());
		
		personnageDAO.savePersonnage(personnageWork.getPersonnageData());
		personnageDAO.savePersonnage(personnageWork.getValidPersonnageData());
		personnageDAO.savePersonnageWork(personnageWork);
		
		table.getPersonnages().add(personnageWork);
		
		return table;
	}
	
	@Override
	public PersonnageWorkBO addPersonnageToTable(Integer tableId, Integer personnageId, UserBO gameMaster) throws Exception {
		TableBO table = tableDAO.loadTable(tableId);
		if(table==null || !gameMaster.equals(table.getGameMaster())){
			return null;
		}
		PersonnageWorkBO personnageWork = personnageDAO.loadPersonnageWork(personnageId.intValue());
		if(personnageWork==null || !gameMaster.equals(personnageWork.getGameMaster())){
			return null;
		}
		table.getPersonnages().add(personnageWork);
		personnageWork.setTable(table);
		
		return personnageWork;
	}
	
	@Override
	public PersonnageWorkBO removePersonnageFromTable(Integer tableId, Integer personnageId, UserBO gameMaster) throws Exception {
		TableBO table = tableDAO.loadTable(tableId);
		if(table==null || !gameMaster.equals(table.getGameMaster())){
			return null;
		}
		PersonnageWorkBO personnageWork = personnageDAO.loadPersonnageWork(personnageId);
		if(personnageWork==null || !table.getId().equals(personnageWork.getTable().getId())){
			return null;
		}
		table.getPersonnages().remove(personnageWork);
		personnageWork.setTable(null);
		
		return personnageWork;
	}
	
	@Override
	public TableBO getPersonnageTable(PersonnageWorkBO personnageWork) throws Exception {
		TableBO result = tableDAO.getPersonnageTable(personnageWork);
		if(result==null){
			return null;
		}
		result.getName();
		return result;
	}

	@Override
	public String addPointsToPj(Integer tableId, UserBO gamaMaster, String pointPoolName, Integer points) throws Exception {
		TableBO table = tableDAO.loadTable(tableId);
		if(!table.getGameMaster().equals(gamaMaster)){
			return "Vous n'ête pas propriétaire de cette table";
		}
		
		for(PersonnageWorkBO personnageWork : table.getPersonnages()){
			if(personnageWork.getPlayer()!=null){
				personnageWork.getPersonnage().getPointPools().get(pointPoolName).add(points);
			}
		}
		return null;
	}
	
	@Override
	public Collection<PersonnageWorkBO> getAddablePersonnages(TableBO table) throws Exception {
		return personnageDAO.getAddablePersonnages(table);
	}

	@Override
	public void addMessageToTable(String message, Integer tableId, UserBO author) throws Exception {
		TableBO table = tableDAO.loadTable(tableId);
		if(!table.getGameMaster().equals(author)){
			throw new BusinessException("The author is not the table game master");
		}
		TableMessageBO tableMessage = new TableMessageBO();
		tableMessage.setAuthor(author);
		tableMessage.setDate(new Date());
		tableMessage.setData(message);
		table.getMessages().add(tableMessage);
		tableDAO.saveTable(table);
	}
	
	@Override
	public void addSendMessage(String message, Integer tableId, UserBO author) throws Exception {
		TableBO table = tableDAO.loadTable(tableId);
		if(!table.getGameMaster().equals(author)){
			throw new Exception("The author is not the table game master");
		}
		TableMessageBO tableMessage = new TableMessageBO();
		tableMessage.setAuthor(author);
		tableMessage.setDate(new Date());
		tableMessage.setData(message);
		table.getMessages().add(tableMessage);
		tableDAO.saveTable(table);
		
		List<String> toAdresses = new ArrayList<String>();
		for(PersonnageWorkBO personnageWork : table.getPersonnages()){
			if(personnageWork.getPlayer()!=null){
				toAdresses.add(personnageWork.getPlayer().getMail());
			}
		}
		toAdresses.add(table.getGameMaster().getMail());
		String tableMailAddress = (String) paramDAO.getParam(ParamBO.TABLE_ADRESS).getValue();
		mailResource.send(toAdresses, tableMailAddress, "["+table.getId()+"] "+table.getName(), message);
	}
	
	@Override
	public void removeMessageFromTable(Integer messageId, Integer tableId, UserBO user) throws Exception {
		TableBO table = tableDAO.loadTable(tableId);
		if(!table.getGameMaster().equals(user)){
			throw new Exception("User is not the table game master");
		}
		Iterator<TableMessageBO> messageIt = table.getMessages().iterator();
		while(messageIt.hasNext()){
			if(messageIt.next().getId().equals(messageId)){
				messageIt.remove();
			}
		}
	}
	
	@Override
	public Collection<TableMessageBO> connectTableMailBox() throws Exception {
		Collection<TableMessageBO> messageList = mailResource.getMails();
		for(TableMessageBO message : messageList){
			TableBO table = null;
			if(message.getTableId()!=null){
				table = tableDAO.loadTable(message.getTableId());
				if(table != null){
					UserBO author = null;
					if(message.getSenderMail() != null){
						author = userDAO.getUserFromMail(message.getSenderMail());
					}
					message.setAuthor(author);
			
					table.getMessages().add(message);
					tableDAO.saveTable(table);
				}
			}
			if(table == null){
				String tableAddress = (String) paramDAO.getParam(ParamBO.TABLE_ADRESS).getValue();
				mailResource.send(message.getSenderMail(), tableAddress, "Invalid subject: " + message.getSubject(), 
						"Votre message n'a pas pu être associé à une table. Il faut que l'objet du mail contienne le numéro de la table entre crochet ('[<numero_table>]')." + "\n\n\n\n" + message.getData());
			}
		}
		
		return messageList;
	}
	
	
	
	
	public PersonnageFactory getPersonnageFactory() {
		return personnageFactory;
	}
	public void setPersonnageFactory(PersonnageFactory personnageFactory) {
		this.personnageFactory = personnageFactory;
	}

	@Override
	public Collection<PlannedGameBO> getPlannedGames(Integer tableId, Date startDate, Date endDate) throws Exception {
		TableBO table = tableDAO.loadTable(tableId);
		return calendarResource.getEntries(table.getName(), startDate, endDate);
	}

	@Override
	public void replanGame(Integer tableId, Integer dayDelta, Integer minuteDelta, Date startDate, Date endDate, UserBO user) throws Exception {
		TableBO table = tableDAO.loadTable(tableId);
		if(! table.getGameMaster().equals(user)){
			throw new BusinessException("User "+user+" not GM on table "+table.getName());
		}
		Calendar oldStart = Calendar.getInstance();
		oldStart.setTime(startDate);
		if(dayDelta!=null){
			oldStart.add(Calendar.DATE, -dayDelta);
		}
		if(minuteDelta!=null){
			oldStart.add(Calendar.MINUTE, -minuteDelta);
		}
		
		calendarResource.updateEvent(table.getName(), oldStart.getTime(), startDate, endDate);
	}

	public ITableDAO getTableDAO() {
		return tableDAO;
	}

	public void setTableDAO(ITableDAO tableDAO) {
		this.tableDAO = tableDAO;
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

	public IParamDAO getParamDAO() {
		return paramDAO;
	}

	public void setParamDAO(IParamDAO paramDAO) {
		this.paramDAO = paramDAO;
	}

	public IGoogleCalendarResource getCalendarResource() {
		return calendarResource;
	}

	public void setCalendarResource(IGoogleCalendarResource calendarResource) {
		this.calendarResource = calendarResource;
	}	
	
}
