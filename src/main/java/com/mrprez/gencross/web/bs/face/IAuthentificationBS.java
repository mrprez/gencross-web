package com.mrprez.gencross.web.bs.face;

import java.util.List;

import com.mrprez.gencross.web.bo.RoleBO;
import com.mrprez.gencross.web.bo.UserBO;

public interface IAuthentificationBS {
	
	UserBO authentificateUser(String username, String password) throws Exception;
	
	UserBO authentificateUserDigest(String username, String digest) throws Exception;
	
	UserBO createUser(String username, String password, String mail) throws Exception;
	
	List<UserBO> getUserList() throws Exception;
	
	UserBO getUser(String username) throws Exception;

	RoleBO getRole(String roleName) throws Exception;
	
	boolean removeUser(String username) throws Exception;

	UserBO sendPassword(String username) throws Exception;

	void changePassword(UserBO user, String newPassword) throws Exception;

	void changeMail(UserBO user, String mail) throws Exception;
}
