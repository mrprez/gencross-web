package com.mrprez.gencross.web.dao.face;

import java.util.List;

import com.mrprez.gencross.web.bo.UserBO;

public interface IUserDAO {
	
	public UserBO getUser(String username, String digest) throws Exception;
	
	public UserBO saveUser(UserBO user) throws Exception;
	
	public UserBO getUser(String username) throws Exception;
	
	public UserBO getUserFromMail(String mail) throws Exception;
	
	public List<UserBO> getUserList() throws Exception;

	public void deleteUser(UserBO user) throws Exception;

}
