package com.mrprez.gencross.web.bs;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.RoleBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.mrprez.gencross.web.dao.face.IMailResource;
import com.mrprez.gencross.web.dao.face.IPersonnageDAO;
import com.mrprez.gencross.web.dao.face.IRoleDAO;
import com.mrprez.gencross.web.dao.face.IUserDAO;

public class AuthentificationBS implements IAuthentificationBS{
	private static final String PASSWORD_CARACTERES = "azertyuiopqsdfghjklmwxcvbnAZERTYUIOPQSDFGHJKLMWXCVBN,;:!?.-1234567890";
	private IUserDAO userDAO;
	private IPersonnageDAO personnageDAO;
	private IRoleDAO roleDAO;
	private IMailResource mailResource;
	
	
	@Override
	public UserBO authentificateUser(String username, String password) throws Exception {
		if(password==null || username==null){
			return null;
		}
		
		// Récupération du MD5 du password
		String digest = buildMD5Digest(password);
		
		UserBO user = userDAO.getUser(username, digest);
		if(user==null){
			return null;
		}
		user.getRoles().iterator().next();
		
		return user;
	}
	
	
	private String buildMD5Digest(String string) throws NoSuchAlgorithmException{
		MessageDigest msgDigest = MessageDigest.getInstance("MD5");
		msgDigest.update(string.getBytes());
		byte digest[] = msgDigest.digest();
		StringBuilder digestStringBuffer = new StringBuilder(digest.length*2);
		for(int i=0; i<digest.length; i++){
			String element = String.format("%x", digest[i]);
			if(element.length()<2){
				digestStringBuffer.append("0");
			}
			digestStringBuffer.append(element);
		}
		return digestStringBuffer.toString();
	}
	
	@Override
	public UserBO createUser(String username, String password, String mail) throws Exception{
		if(userDAO.getUser(username)!=null){
			return null;
		}
		
		UserBO user = new UserBO();
		user.setUsername(username);
		user.setMail(mail);
		
		// Digest
		MessageDigest msgDigest = MessageDigest.getInstance("MD5");
		msgDigest.update(password.getBytes());
		byte digest[] = msgDigest.digest();
		StringBuilder digestStringBuffer = new StringBuilder(digest.length*2);
		for(int i=0; i<digest.length; i++){
			String element = String.format("%x", digest[i]);
			if(element.length()<2){
				digestStringBuffer.append("0");
			}
			digestStringBuffer.append(element);
		}
		user.setDigest(digestStringBuffer.toString());
		
		// Role user
		user.setRoles(new HashSet<RoleBO>());
		user.getRoles().add(roleDAO.getRole(RoleBO.USER));
		
		userDAO.saveUser(user);
		return user;
	}
	
	@Override
	public List<UserBO> getUserList() throws Exception{
		return userDAO.getUserList();
	}
	
	@Override
	public UserBO getUser(String username) throws Exception{
		return userDAO.getUser(username);
	}
	
	@Override
	public RoleBO getRole(String roleName) throws Exception{
		return roleDAO.getRole(roleName);
	}

	@Override
	public boolean removeUser(String username) throws Exception {
		UserBO user = userDAO.getUser(username);
		if(user==null){
			return false;
		}
		for(PersonnageWorkBO personnageWork : personnageDAO.getGameMasterPersonnageList(user)){
			if(personnageWork.getPlayer()==null || personnageWork.getPlayer().equals(user)){
				personnageDAO.deletePersonnage(personnageWork);
			}else{
				personnageWork.setGameMaster(null);
				personnageDAO.savePersonnageWork(personnageWork);
			}
		}
		for(PersonnageWorkBO personnageWork : personnageDAO.getPlayerPersonnageList(user)){
			if(personnageWork.getGameMaster()==null){
				personnageDAO.deletePersonnage(personnageWork);
			}else{
				personnageWork.setPlayer(null);
				personnageDAO.savePersonnageWork(personnageWork);
			}
		}
		
		userDAO.deleteUser(user);
		
		return true;
	}
	
	@Override
	public UserBO sendPassword(String username) throws Exception{
		UserBO user = userDAO.getUser(username);
		if(user==null){
			return null;
		}
		
		StringBuilder newPassword = new StringBuilder();
		for(int i=0; i<8; i++){
			int index = (int)(Math.random()*PASSWORD_CARACTERES.length());
			newPassword.append(PASSWORD_CARACTERES.charAt(index));
		}
		user.setDigest(buildMD5Digest(newPassword.toString()));
		
		mailResource.send(user.getMail(), "GenCrossWeb: Mot de passe oublié", "Votre nouveau mot de passe: "+newPassword);
		
		return user;
	}
	
	@Override
	public void changePassword(UserBO user, String newPassword) throws Exception{
		user = userDAO.getUser(user.getUsername());
		user.setDigest(buildMD5Digest(newPassword));
	}
	
	public void changeMail(UserBO user, String mail) throws Exception {
		user.setMail(mail);
		userDAO.saveUser(user);
	}

	public IUserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(IUserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public IPersonnageDAO getPersonnageDAO() {
		return personnageDAO;
	}

	public void setPersonnageDAO(IPersonnageDAO personnageDAO) {
		this.personnageDAO = personnageDAO;
	}

	public IRoleDAO getRoleDAO() {
		return roleDAO;
	}

	public void setRoleDAO(IRoleDAO roleDAO) {
		this.roleDAO = roleDAO;
	}

	public IMailResource getMailResource() {
		return mailResource;
	}

	public void setMailResource(IMailResource mailResource) {
		this.mailResource = mailResource;
	}
	

}
