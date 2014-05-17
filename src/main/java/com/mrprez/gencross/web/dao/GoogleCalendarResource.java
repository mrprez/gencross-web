package com.mrprez.gencross.web.dao;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.mrprez.gencross.web.bo.PlannedGameBO;
import com.mrprez.gencross.web.dao.face.IGoogleCalendarResource;

public class GoogleCalendarResource implements IGoogleCalendarResource {
	private static final String GMAIL_DOMAIN = "gmail.com";
	private static final String APPLICATION_NAME = "com.mrprez-gencross.web";
	private static final String GOOGLE_CALENDAR_URL = "https://www.google.com/calendar/feeds/default/allcalendars/full";
	private static final String OWN_CALENDARS_URL = "https://www.google.com/calendar/feeds/default/owncalendars/full";
	private com.google.api.services.calendar.Calendar calendarService;
	
	
	public GoogleCalendarResource() throws IOException, GeneralSecurityException {
		super();
		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
		Credential credential = authorize(httpTransport, jsonFactory);
		calendarService = new com.google.api.services.calendar.Calendar.Builder(httpTransport, jsonFactory, credential).build();
	}
	
	private Credential authorize(HttpTransport httpTransport, JsonFactory jsonFactory) throws IOException{
		Reader secretReader = new InputStreamReader(getClass().getResourceAsStream("client_secrets.json"));
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, secretReader);
		
		GoogleAuthorizationCodeFlow.Builder flowBuilder = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientSecrets,
				Collections.singleton(CalendarScopes.CALENDAR));
		flowBuilder.setDataStoreFactory(new FileDataStoreFactory(new File("M:/")));
		GoogleAuthorizationCodeFlow flow = flowBuilder.build();
		
		return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	}
	
	
	@Override
	public boolean isCalendarExist(String tableName) throws IOException{
		return calendarService.calendarList().get(tableName).execute() != null;
	}

	
	@Override
	public Collection<PlannedGameBO> getEntries(String tableName, Date startDate, Date endDate) throws Exception {
//		Collection<PlannedGameBO> plannedGamesList = new ArrayList<PlannedGameBO>();
//		CalendarEntry calendarEntry = getCalendarEntry(tableName);
//		if(calendarEntry==null){
//			return plannedGamesList;
//		}
//		OutOfLineContent content = (OutOfLineContent)calendarEntry.getContent();
//		URL eventFeedUrl = new URL(content.getUri());
//		
//		CalendarQuery eventQuery = new CalendarQuery(eventFeedUrl);
//		eventQuery.setMinimumStartTime(new DateTime(startDate));
//		eventQuery.setMaximumStartTime(new DateTime(endDate));
//		CalendarEventFeed eventFeed = calendarService.query(eventQuery, CalendarEventFeed.class);
//		for(CalendarEventEntry eventEntry : eventFeed.getEntries()){
//			plannedGamesList.addAll(buildPlannedGame(eventEntry));
//		}
//		
//		return plannedGamesList;
		return null;
	}
	
	
//	private CalendarEntry getCalendarEntry(String title) throws IOException, ServiceException{
//		URL calendarFeedUrl = new URL(GOOGLE_CALENDAR_URL);
//		CalendarQuery calendarQuery = new CalendarQuery(calendarFeedUrl);
//		CalendarFeed calendarFeed = calendarService.query(calendarQuery, CalendarFeed.class);
//		for(CalendarEntry calendarEntry : calendarFeed.getEntries()){
//			if(calendarEntry.getTitle().getPlainText().equals(title)){
//				return calendarEntry;
//			}
//		}
//		return null;
//	}
	
	
//	private Collection<PlannedGameBO> buildPlannedGame(CalendarEventEntry eventEntry){
//		Collection<PlannedGameBO> plannedGamesList = new ArrayList<PlannedGameBO>();
//		for(When when : eventEntry.getTimes()){
//			PlannedGameBO plannedGame = new PlannedGameBO();
//			plannedGame.setTitle(eventEntry.getTitle().getPlainText());
//			plannedGame.setStartTime(new Date(when.getStartTime().getValue()));
//			plannedGame.setEndTime(new Date(when.getEndTime().getValue()));
//			plannedGamesList.add(plannedGame);
//		}
//		return plannedGamesList;
//	}
	
//	private CalendarEventEntry getEvent(CalendarEntry calendarEntry, String title, Date startTime) throws IOException, ServiceException{
//		OutOfLineContent content = (OutOfLineContent)calendarEntry.getContent();
//		URL eventFeedUrl = new URL(content.getUri());
//		
//		CalendarQuery eventQuery = new CalendarQuery(eventFeedUrl);
//		DateTime startDateTime = new DateTime(startTime, TimeZone.getDefault());
//		eventQuery.setMinimumStartTime(startDateTime);
//		CalendarEventFeed eventFeed = calendarService.query(eventQuery, CalendarEventFeed.class);
//		for(CalendarEventEntry eventEntry : eventFeed.getEntries()){
//			for(When when : eventEntry.getTimes()){
//				if(when.getStartTime().equals(startDateTime)){
//					return eventEntry;
//				}
//			}
//		}
//		return null;
//	}

	@Override
	public void planGame(String tableName, String title, Date startDate, Date endDate, Collection<String> participantsMail) throws Exception {
//		CalendarEntry calendarEntry = getCalendarEntry(tableName);
//		String calendarUri = ((OutOfLineContent)calendarEntry.getContent()).getUri();
//		CalendarEventEntry eventEntry = new CalendarEventEntry();
//		When when = new When();
//		when.setStartTime(new DateTime(startDate));
//		when.setEndTime(new DateTime(endDate));
//		eventEntry.addTime(when);
//		eventEntry.setTitle(new PlainTextConstruct(title));
//		calendarService.insert(new URL(calendarUri), eventEntry);
	}


	@Override
	public void createCalendar(String tableName, String ownerMail, Collection<String> participantsMail) throws Exception {
//		CalendarEntry calendarEntry = new CalendarEntry();
//		calendarEntry.setTitle(new PlainTextConstruct(tableName));
//		URL postUrl = new URL(OWN_CALENDARS_URL);
//		calendarEntry = calendarService.insert(postUrl, calendarEntry);
//		OutOfLineContent content = (OutOfLineContent)calendarEntry.getContent();
//		URL calendarUrl = new URL(content.getUri());
//		URL aclUrl = new URL(calendarUrl.toString().replace("/private/full", "/acl/full"));
//		
//		AclEntry aclEntry = new AclEntry();
//		aclEntry.setScope(new AclScope(AclScope.Type.USER, ownerMail));
//		aclEntry.setRole(CalendarAclRole.OWNER);
//		aclEntry = calendarService.insert(aclUrl, aclEntry);
//
//		for(String participantMail : participantsMail){
//			if(! participantMail.equals(ownerMail)){
//				aclEntry = new AclEntry();
//				aclEntry.setScope(new AclScope(AclScope.Type.USER, participantMail));
//				aclEntry.setRole(CalendarAclRole.READ);
//				aclEntry = calendarService.insert(aclUrl, aclEntry);
//			}
//		}
	}


	@Override
	public void updateEvent(String tableName, Date oldStartDate, Date startDate, Date endDate) throws IOException {
//		CalendarEntry calendarEntry = getCalendarEntry(tableName);
//		CalendarEventEntry eventEntry = getEvent(calendarEntry, tableName, oldStartDate);
//		for(When when : eventEntry.getTimes()){
//			if(when.getStartTime().equals(new DateTime(oldStartDate, TimeZone.getDefault()))){
//				when.setStartTime(new DateTime(startDate, TimeZone.getDefault()));
//				when.setEndTime(new DateTime(endDate, TimeZone.getDefault()));
//			}
//		}
//		
//		eventEntry = calendarService.update(new URL(eventEntry.getEditLink().getHref()), eventEntry);
		
		
		
	}
	
	
	

}
