package com.mrprez.gencross.web.dao.face;

import java.util.Collection;
import java.util.Date;

import com.mrprez.gencross.web.bo.PlannedGameBO;

public interface IGoogleCalendarResource {
	
	
	Collection<PlannedGameBO> getEntries(String tableName, Date startDate, Date enDate) throws Exception;
	
	void planGame(String tableName, String title, Date startDate, Date endDate, Collection<String> participants) throws Exception;

	boolean isCalendarExist(String tableName) throws Exception;

	void createCalendar(String name, String mail, Collection<String> participantsMail) throws Exception;

	void updateEvent(String name, Date oldStartDate,Date startDate, Date endDate) throws Exception;


	
}
