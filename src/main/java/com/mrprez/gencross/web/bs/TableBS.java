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

import org.springframework.web.context.ContextLoader;

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

	@Override
	public TableBO createTable(String name, UserBO gm, String type) throws Exception {
		ITableDAO tableDAO = (ITableDAO) ContextLoader.getCurrentWebApplicationContext().getBean("TableDAO");
		TableBO table = new TableBO();
		table.setName(name);
		table.setGameMaster(gm);
		table.setType(type);
		tableDAO.saveTable(table);
		
		return table;
	}
	
	@Override
	public Set<TableBO> getTableListForUser(UserBO user) throws Exception {
		Set<TableBO> result = new TreeSet<TableBO>();
		ITableDAO tableDAO = (ITableDAO) ContextLoader.getCurrentWebApplicationContext().getBean("TableDAO");
		result.addAll(tableDAO.getTableFromGM(user));
		
		return result;
	}

	@Override
	public TableBO getTableForGM(Integer id, UserBO gameMaster) throws Exception {
		ITableDAO tableDAO = (ITableDAO) ContextLoader.getCurrentWebApplicationContext().getBean("TableDAO");
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
		ITableDAO tableDAO = (ITableDAO) ContextLoader.getCurrentWebApplicationContext().getBean("TableDAO");
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
		ITableDAO tableDAO = (ITableDAO) ContextLoader.getCurrentWebApplicationContext().getBean("TableDAO");
		TableBO table = tableDAO.loadTable(tableId);
		Personnage personnage = personnageFactory.buildNewPersonnage(table.getType());
		
		return personnage.getPointPools().keySet();
	}
	
	@Override
	public TableBO addNewPersonnageToTable(Integer tableId, String personnageName, UserBO gameMaster) throws Exception {
		ITableDAO tableDAO = (ITableDAO) ContextLoader.getCurrentWebApplicationContext().getBean("TableDAO");
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
		
		IPersonnageDAO personnageDAO = (IPersonnageDAO) ContextLoader.getCurrentWebApplicationContext().getBean("PersonnageDAO");
		personnageDAO.savePersonnage(personnageWork.getPersonnageData());
		personnageDAO.savePersonnage(personnageWork.getValidPersonnageData());
		personnageDAO.savePersonnageWork(personnageWork);
		
		table.getPersonnages().add(personnageWork);
		
		return table;
	}
	
	@Override
	public PersonnageWorkBO addPersonnageToTable(Integer tableId, Integer personnageId, UserBO gameMaster) throws Exception {
		ITableDAO tableDAO = (ITableDAO) ContextLoader.getCurrentWebApplicationContext().getBean("TableDAO");
		TableBO table = tableDAO.loadTable(tableId);
		if(table==null || !gameMaster.equals(table.getGameMaster())){
			return null;
		}
		IPersonnageDAO personnageDAO = (IPersonnageDAO) ContextLoader.getCurrentWebApplicationContext().getBean("PersonnageDAO");
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
		ITableDAO tableDAO = (ITableDAO) ContextLoader.getCurrentWebApplicationContext().getBean("TableDAO");
		TableBO table = tableDAO.loadTable(tableId);
		if(table==null || !gameMaster.equals(table.getGameMaster())){
			return null;
		}
		IPersonnageDAO personnageDAO = (IPersonnageDAO) ContextLoader.getCurrentWebApplicationContext().getBean("PersonnageDAO");
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
		ITableDAO tableDAO = (ITableDAO) ContextLoader.getCurrentWebApplicationContext().getBean("TableDAO");
		TableBO result = tableDAO.getPersonnageTable(personnageWork);
		if(result==null){
			return null;
		}
		result.getName();
		return result;
	}

	@Override
	public String addPointsToPj(Integer tableId, UserBO gamaMaster, String pointPoolName, Integer points) throws Exception {
		ITableDAO tableDAO = (ITableDAO) ContextLoader.getCurrentWebApplicationContext().getBean("TableDAO");
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
		IPersonnageDAO personnageDAO = (IPersonnageDAO) ContextLoader.getCurrentWebApplicationContext().getBean("PersonnageDAO");
		return personnageDAO.getAddablePersonnages(table);
	}

	@Override
	public void addMessageToTable(String message, Integer tableId, UserBO author) throws Exception {
		ITableDAO tableDAO = (ITableDAO) ContextLoader.getCurrentWebApplicationContext().getBean("TableDAO");
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
	}
	
	@Override
	public void addSendMessage(String message, Integer tableId, UserBO author) throws Exception {
		IMailResource mailResource = (IMailResource)ContextLoader.getCurrentWebApplicationContext().getBean("MailResource");
		ITableDAO tableDAO = (ITableDAO) ContextLoader.getCurrentWebApplicationContext().getBean("TableDAO");
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
		mailResource.send(toAdresses, table.getGameMaster().getMail(), "["+table.getName()+"]", message);
	}
	
	@Override
	public void removeMessageFromTable(Integer messageId, Integer tableId, UserBO user) throws Exception {
		ITableDAO tableDAO = (ITableDAO) ContextLoader.getCurrentWebApplicationContext().getBean("TableDAO");
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
	public void connectTableMailBox() throws Exception {
		IMailResource mailResource = (IMailResource)ContextLoader.getCurrentWebApplicationContext().getBean("MailResource");
		ITableDAO tableDAO = (ITableDAO) ContextLoader.getCurrentWebApplicationContext().getBean("TableDAO");
		IUserDAO userDao = (IUserDAO) ContextLoader.getCurrentWebApplicationContext().getBean("UserDAO");
		for(TableMessageBO message : mailResource.getMails()){
			TableBO table = null;
			if(message.getTableName() != null){
				table = tableDAO.findTableByName(message.getTableName());
				if(table != null){
					UserBO author = null;
					if(message.getSenderMail() != null){
						author = userDao.getUserFromMail(message.getSenderMail());
					}
					message.setAuthor(author);
					if(message.getData().getBytes("UTF-8").length >= Math.pow(2, 16)){
						
					}
			
					table.getMessages().add(message);
					tableDAO.saveTable(table);
				}
			}
			if(table == null){
				IParamDAO paramDao = (IParamDAO) ContextLoader.getCurrentWebApplicationContext().getBean("ParamDAO");
				String tableAddress = (String) paramDao.getParam(ParamBO.TABLE_ADRESS).getValue();
				mailResource.send(message.getSenderMail(), tableAddress, "Invalid subject: " + message.getSubject(), 
						"Votre message n'a pas pu être associé à une table. Il faut que l'objet du mail contienne le nom de la table entre crochet ('[<nom_table>]')." + "\n\n\n\n" + message.getData());
			}
		}
	}
	
	
	
	
	public PersonnageFactory getPersonnageFactory() {
		return personnageFactory;
	}
	public void setPersonnageFactory(PersonnageFactory personnageFactory) {
		this.personnageFactory = personnageFactory;
	}

	@Override
	public Collection<PlannedGameBO> getPlannedGames(Integer tableId, Date startDate, Date endDate) throws Exception {
		ITableDAO tableDAO = (ITableDAO) ContextLoader.getCurrentWebApplicationContext().getBean("TableDAO");
		TableBO table = tableDAO.loadTable(tableId);
		IGoogleCalendarResource calendarResource = (IGoogleCalendarResource)ContextLoader.getCurrentWebApplicationContext().getBean("GoogleCalendarResource");
		return calendarResource.getEntries(table.getName(), startDate, endDate);
	}

	@Override
	public void planGame(Integer tableId, PlannedGameBO plannedGame, UserBO user) throws Exception {
		ITableDAO tableDAO = (ITableDAO) ContextLoader.getCurrentWebApplicationContext().getBean("TableDAO");
		TableBO table = tableDAO.loadTable(tableId);
		if(! table.getGameMaster().equals(user)){
			throw new BusinessException("User "+user+" not GM on table "+table.getName());
		}
		IGoogleCalendarResource calendarResource = (IGoogleCalendarResource)ContextLoader.getCurrentWebApplicationContext().getBean("GoogleCalendarResource");
		Collection<String> participantsMail = new HashSet<String>();
		for(PersonnageWorkBO personnageWork : table.getPersonnages()){
			if(personnageWork.getPlayer() != null){
				participantsMail.add(personnageWork.getPlayer().getMail());
			}
		}
		participantsMail.add(user.getMail());
		
		if( ! calendarResource.isCalendarExist(table.getName()) ){
			calendarResource.createCalendar(table.getName(), table.getGameMaster().getMail(), participantsMail);
		}
		calendarResource.planGame(table.getName(), plannedGame.getTitle(), plannedGame.getStartTime(), plannedGame.getEndTime(), participantsMail);
		
	}

	@Override
	public void replanGame(Integer tableId, Integer dayDelta, Integer minuteDelta, Date startDate, Date endDate, UserBO user) throws Exception {
		ITableDAO tableDAO = (ITableDAO) ContextLoader.getCurrentWebApplicationContext().getBean("TableDAO");
		TableBO table = tableDAO.loadTable(tableId);
		if(! table.getGameMaster().equals(user)){
			throw new BusinessException("User "+user+" not GM on table "+table.getName());
		}
		IGoogleCalendarResource calendarResource = (IGoogleCalendarResource)ContextLoader.getCurrentWebApplicationContext().getBean("GoogleCalendarResource");
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
	
		
	
	
}
