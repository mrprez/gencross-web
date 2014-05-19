package com.mrprez.gencross.web.bs;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Version;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.PlannedGameBO;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IPlanGameBS;
import com.mrprez.gencross.web.dao.face.IMailResource;
import com.mrprez.gencross.web.dao.face.ITableDAO;

public class PlanGameBS implements IPlanGameBS {
	private IMailResource mailResource;
	private ITableDAO tableDao;
	
	
	@Override
	public Collection<PlannedGameBO> getPlannedGames(Integer tableId, Date date, Date date2) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void replanGame(Integer tableId, Integer dayDelta, Integer minuteDelta, Date date, Date date2, UserBO user) {
		// TODO Auto-generated method stub
	}

	@Override
	public void planGame(Integer tableId, PlannedGameBO plannedGame, UserBO user) throws Exception {
		TableBO table = tableDao.loadTable(tableId);
		plannedGame.setTable(table);
		tableDao.savePlannedGame(plannedGame);
		
		Set<String> toAddress = new HashSet<String>();
		for(PersonnageWorkBO personnageWork : table.getPersonnages()){
			if(personnageWork.getPlayer()!=null){
				toAddress.add(personnageWork.getPlayer().getMail());
			}
		}
		
		Calendar calendar = buildCalendarToSend(plannedGame);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		CalendarOutputter outputter = new CalendarOutputter();
		outputter.output(calendar, baos);
		
		mailResource.send(toAddress, table.getGameMaster().getMail(), table.getName()+": nouvelle partie", "Une nouvelle partie a été planifiée", table.getName(), baos.toByteArray());
		
	}
	
	private Calendar buildCalendarToSend(PlannedGameBO plannedGameBO){
		Calendar calendar = new Calendar();
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);
		
		DateTime start = new DateTime(plannedGameBO.getStartTime());
		DateTime end = new DateTime(plannedGameBO.getEndTime());
		VEvent event = new VEvent(start, end, plannedGameBO.getTitle());
		
		calendar.getComponents().add(event);
		
		return calendar;
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
