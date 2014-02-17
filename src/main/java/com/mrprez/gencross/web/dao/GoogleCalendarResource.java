package com.mrprez.gencross.web.dao;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;

import com.google.gdata.client.calendar.CalendarQuery;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.OutOfLineContent;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.acl.AclEntry;
import com.google.gdata.data.acl.AclScope;
import com.google.gdata.data.calendar.CalendarAclRole;
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.calendar.CalendarFeed;
import com.google.gdata.data.extensions.When;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.mrprez.gencross.web.bo.PlannedGameBO;
import com.mrprez.gencross.web.dao.face.IGoogleCalendarResource;

public class GoogleCalendarResource implements IGoogleCalendarResource {
	private static final String GMAIL_DOMAIN = "gmail.com";
	private static final String APPLICATION_NAME = "com.mrprez-gencross.web";
	private static final String GOOGLE_CALENDAR_URL = "https://www.google.com/calendar/feeds/default/allcalendars/full";
	private static final String OWN_CALENDARS_URL = "https://www.google.com/calendar/feeds/default/owncalendars/full";
	private CalendarService calendarService;
	
	
	public GoogleCalendarResource(String login, String password) throws IOException, AuthenticationException {
		super();
		calendarService = new CalendarService(APPLICATION_NAME);
		calendarService.setUserCredentials(login + "@" + GMAIL_DOMAIN, password);
	}
	
	
	@Override
	public boolean isCalendarExist(String tableName) throws IOException, ServiceException{
		return getCalendarEntry(tableName) != null;
	}

	
	@Override
	public Collection<PlannedGameBO> getEntries(String tableName, Date startDate, Date endDate) throws Exception {
		Collection<PlannedGameBO> plannedGamesList = new ArrayList<PlannedGameBO>();
		CalendarEntry calendarEntry = getCalendarEntry(tableName);
		if(calendarEntry==null){
			return plannedGamesList;
		}
		OutOfLineContent content = (OutOfLineContent)calendarEntry.getContent();
		URL eventFeedUrl = new URL(content.getUri());
		
		CalendarQuery eventQuery = new CalendarQuery(eventFeedUrl);
		eventQuery.setMinimumStartTime(new DateTime(startDate));
		eventQuery.setMaximumStartTime(new DateTime(endDate));
		CalendarEventFeed eventFeed = calendarService.query(eventQuery, CalendarEventFeed.class);
		for(CalendarEventEntry eventEntry : eventFeed.getEntries()){
			plannedGamesList.addAll(buildPlannedGame(eventEntry));
		}
		
		return plannedGamesList;
	}
	
	
	private CalendarEntry getCalendarEntry(String title) throws IOException, ServiceException{
		URL calendarFeedUrl = new URL(GOOGLE_CALENDAR_URL);
		CalendarQuery calendarQuery = new CalendarQuery(calendarFeedUrl);
		CalendarFeed calendarFeed = calendarService.query(calendarQuery, CalendarFeed.class);
		for(CalendarEntry calendarEntry : calendarFeed.getEntries()){
			if(calendarEntry.getTitle().getPlainText().equals(title)){
				return calendarEntry;
			}
		}
		return null;
	}
	
	
	private Collection<PlannedGameBO> buildPlannedGame(CalendarEventEntry eventEntry){
		Collection<PlannedGameBO> plannedGamesList = new ArrayList<PlannedGameBO>();
		for(When when : eventEntry.getTimes()){
			PlannedGameBO plannedGame = new PlannedGameBO();
			plannedGame.setTitle(eventEntry.getTitle().getPlainText());
			plannedGame.setStartTime(new Date(when.getStartTime().getValue()));
			plannedGame.setEndTime(new Date(when.getEndTime().getValue()));
			plannedGamesList.add(plannedGame);
		}
		return plannedGamesList;
	}
	
	private CalendarEventEntry getEvent(CalendarEntry calendarEntry, String title, Date startTime) throws IOException, ServiceException{
		OutOfLineContent content = (OutOfLineContent)calendarEntry.getContent();
		URL eventFeedUrl = new URL(content.getUri());
		
		CalendarQuery eventQuery = new CalendarQuery(eventFeedUrl);
		DateTime startDateTime = new DateTime(startTime, TimeZone.getDefault());
		eventQuery.setMinimumStartTime(startDateTime);
		CalendarEventFeed eventFeed = calendarService.query(eventQuery, CalendarEventFeed.class);
		for(CalendarEventEntry eventEntry : eventFeed.getEntries()){
			for(When when : eventEntry.getTimes()){
				if(when.getStartTime().equals(startDateTime)){
					return eventEntry;
				}
			}
		}
		return null;
	}

	@Override
	public void planGame(String tableName, String title, Date startDate, Date endDate, Collection<String> participantsMail) throws Exception {
		CalendarEntry calendarEntry = getCalendarEntry(tableName);
		String calendarUri = ((OutOfLineContent)calendarEntry.getContent()).getUri();
		CalendarEventEntry eventEntry = new CalendarEventEntry();
		When when = new When();
		when.setStartTime(new DateTime(startDate));
		when.setEndTime(new DateTime(endDate));
		eventEntry.addTime(when);
		eventEntry.setTitle(new PlainTextConstruct(title));
		calendarService.insert(new URL(calendarUri), eventEntry);
	}


	@Override
	public void createCalendar(String tableName, String ownerMail, Collection<String> participantsMail) throws Exception {
		CalendarEntry calendarEntry = new CalendarEntry();
		calendarEntry.setTitle(new PlainTextConstruct(tableName));
		URL postUrl = new URL(OWN_CALENDARS_URL);
		calendarEntry = calendarService.insert(postUrl, calendarEntry);
		OutOfLineContent content = (OutOfLineContent)calendarEntry.getContent();
		URL calendarUrl = new URL(content.getUri());
		URL aclUrl = new URL(calendarUrl.toString().replace("/private/full", "/acl/full"));
		
		AclEntry aclEntry = new AclEntry();
		aclEntry.setScope(new AclScope(AclScope.Type.USER, ownerMail));
		aclEntry.setRole(CalendarAclRole.OWNER);
		aclEntry = calendarService.insert(aclUrl, aclEntry);

		for(String participantMail : participantsMail){
			if(! participantMail.equals(ownerMail)){
				aclEntry = new AclEntry();
				aclEntry.setScope(new AclScope(AclScope.Type.USER, participantMail));
				aclEntry.setRole(CalendarAclRole.READ);
				aclEntry = calendarService.insert(aclUrl, aclEntry);
			}
		}
	}


	@Override
	public void updateEvent(String tableName, Date oldStartDate, Date startDate, Date endDate) throws IOException, ServiceException {
		CalendarEntry calendarEntry = getCalendarEntry(tableName);
		CalendarEventEntry eventEntry = getEvent(calendarEntry, tableName, oldStartDate);
		for(When when : eventEntry.getTimes()){
			if(when.getStartTime().equals(new DateTime(oldStartDate, TimeZone.getDefault()))){
				when.setStartTime(new DateTime(startDate, TimeZone.getDefault()));
				when.setEndTime(new DateTime(endDate, TimeZone.getDefault()));
			}
		}
		
		eventEntry = calendarService.update(new URL(eventEntry.getEditLink().getHref()), eventEntry);
		
		
		
	}
	
	
	

}
