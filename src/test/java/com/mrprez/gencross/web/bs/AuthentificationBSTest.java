package com.mrprez.gencross.web.bs;

import com.mrprez.gencross.web.bo.UserBO;

public class AuthentificationBSTest {
	
	public static UserBO buildUser(String name){
		UserBO user = new UserBO();
		user.setUsername(name);
		user.setMail(name+"@gmail.com");
		return user;
	}

}
