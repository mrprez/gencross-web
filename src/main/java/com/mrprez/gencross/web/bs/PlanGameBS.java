package com.mrprez.gencross.web.bs;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Status;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.PlannedGameBO;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IPlanGameBS;
import com.mrprez.gencross.web.bs.util.BusinessException;
import com.mrprez.gencross.web.dao.face.IMailResource;
import com.mrprez.gencross.web.dao.face.ITableDAO;

public class PlanGameBS implements IPlanGameBS {
	private IMailResource mailResource;
	private ITableDAO tableDao;
	
	
	@Override
	public Collection<PlannedGameBO> getPlannedGames(Integer tableId) throws Exception {
		return tableDao.loadTablePlannedGames(tableId);
	}
	
	@Override
	public PlannedGameBO createGame(Integer tableId, String title, Date startDate, Date endDate, UserBO user) throws Exception {
		TableBO table = tableDao.loadTable(tableId);
		if(! table.getGameMaster().equals(user)){
			throw new BusinessException("L'utilisateur n'est pas le MJ de la table");
		}
		PlannedGameBO plannedGame = new PlannedGameBO();
		plannedGame.setTable(table);
		plannedGame.setEndTime(endDate);
		plannedGame.setStartTime(startDate);
		plannedGame.setTitle(title);
		tableDao.savePlannedGame(plannedGame);
		
		Set<String> toAddresses = new HashSet<String>();
		toAddresses.add(table.getGameMaster().getMail());
		for(PersonnageWorkBO personnageWork : table.getPersonnages()){
			if(personnageWork.getPlayer()!=null){
				toAddresses.add(personnageWork.getPlayer().getMail());
			}
		}
		Calendar calendar = buildCalendarToSend(plannedGame);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		CalendarOutputter outputter = new CalendarOutputter();
		outputter.output(calendar, baos);
		mailResource.send(toAddresses, table.getGameMaster().getMail(), table.getName()+": nouvelle partie", "Une nouvelle partie a été planifiée", table.getName()+".ics", baos.toByteArray());
		
		return plannedGame;
	}
	
	@Override
	public void updateGame(Integer plannedGameId, String title, Date startDate, Date endDate, UserBO user) throws Exception {
		PlannedGameBO plannedGame = tableDao.loadPlannedGame(plannedGameId);
		if(! plannedGame.getTable().getGameMaster().equals(user)){
			throw new BusinessException("L'utilisateur n'est pas le MJ de la table");
		}
		plannedGame.setEndTime(endDate);
		plannedGame.setStartTime(startDate);
		plannedGame.setTitle(title);
		
		TableBO table = plannedGame.getTable();
		Set<String> toAddresses = new HashSet<String>();
		toAddresses.add(table.getGameMaster().getMail());
		for(PersonnageWorkBO personnageWork : table.getPersonnages()){
			if(personnageWork.getPlayer()!=null){
				toAddresses.add(personnageWork.getPlayer().getMail());
			}
		}
		Calendar calendar = buildCalendarToSend(plannedGame);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		CalendarOutputter outputter = new CalendarOutputter();
		outputter.output(calendar, baos);
		mailResource.send(toAddresses, table.getGameMaster().getMail(), table.getName()+": nouvelle partie", "Une nouvelle partie a été planifiée", table.getName()+".ics", baos.toByteArray());
	}
	
	@Override
	public void deleteGame(Integer plannedGameId, UserBO user) throws Exception{
		PlannedGameBO plannedGame = tableDao.loadPlannedGame(plannedGameId);
		if(! plannedGame.getTable().getGameMaster().equals(user)){
			throw new BusinessException("L'utilisateur n'est pas le MJ de la table");
		}
		tableDao.deletePlannedGame(plannedGame);
		
		TableBO table = plannedGame.getTable();
		Set<String> toAddresses = new HashSet<String>();
		toAddresses.add(table.getGameMaster().getMail());
		for(PersonnageWorkBO personnageWork : table.getPersonnages()){
			if(personnageWork.getPlayer()!=null){
				toAddresses.add(personnageWork.getPlayer().getMail());
			}
		}
		Calendar calendar = buildCalendarForCancel(plannedGame);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		CalendarOutputter outputter = new CalendarOutputter();
		outputter.output(calendar, baos);
		mailResource.send(toAddresses, table.getGameMaster().getMail(), table.getName()+": nouvelle partie", "Une nouvelle partie a été planifiée", table.getName()+".ics", baos.toByteArray());
	}

	private Calendar buildCalendarToSend(PlannedGameBO plannedGame){
		Calendar calendar = new Calendar();
		calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);
		
		DateTime start = new DateTime(plannedGame.getStartTime());
		DateTime end = new DateTime(plannedGame.getEndTime());
		
		VEvent event = new VEvent(start, end, plannedGame.getTitle());
		event.getProperties().add(new Uid(plannedGame.getId().toString()));
		
		event.getProperties().add(buildAttendee(plannedGame.getTable().getGameMaster()));
		for(PersonnageWorkBO personnageWork : plannedGame.getTable().getPersonnages()){
			UserBO player = personnageWork.getPlayer();
			if(player!=null){
				event.getProperties().add(buildAttendee(player));
			}
		}
		
		calendar.getComponents().add(event);
		
		return calendar;
	}
	
	private Calendar buildCalendarForCancel(PlannedGameBO plannedGame){
		Calendar calendar = new Calendar();
		calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);
		
		DateTime start = new DateTime(plannedGame.getStartTime());
		DateTime end = new DateTime(plannedGame.getEndTime());
		
		VEvent event = new VEvent(start, end, plannedGame.getTitle());
		event.getProperties().add(new Uid(plannedGame.getId().toString()));
		event.getProperties().add(Status.VEVENT_CANCELLED);
		
		event.getProperties().add(buildAttendee(plannedGame.getTable().getGameMaster()));
		for(PersonnageWorkBO personnageWork : plannedGame.getTable().getPersonnages()){
			UserBO player = personnageWork.getPlayer();
			if(player!=null){
				event.getProperties().add(buildAttendee(player));
			}
		}
		
		calendar.getComponents().add(event);
		
		return calendar;
	}
	
	private Attendee buildAttendee(UserBO user){
		Attendee attendee = new Attendee(URI.create("mailto:"+user.getMail()));
		attendee.getParameters().add(Role.REQ_PARTICIPANT);
		attendee.getParameters().add(new Cn(user.getUsername()));
		return attendee;
	}
	

	public ITableDAO getTableDao() {
		return tableDao;
	}

	public void setTableDao(ITableDAO tableDao) {
		this.tableDao = tableDao;
	}

	public IMailResource getMailResource() {
		return mailResource;
	}

	public void setMailResource(IMailResource mailResource) {
		this.mailResource = mailResource;
	}

	
	
}
