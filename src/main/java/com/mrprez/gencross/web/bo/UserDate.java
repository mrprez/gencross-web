package com.mrprez.gencross.web.bo;

import java.util.Date;

/**
 * Used to store last web service action date from a user.
 * Used by AuthentificationService to check user token validity
 */
public class UserDate {
	private UserBO user;
	private Date date;
	
	public UserDate(UserBO user) {
		super();
		this.user = user;
		this.date = new Date();
	}

	public UserBO getUser() {
		return user;
	}

	public Date getDate() {
		return date;
	}
	
	public void resetTime(){
		this.date = new Date();
	}
}
