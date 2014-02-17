package com.mrprez.gencross.web.bo;

import java.util.Set;

public class UserBO {
	private String username;
	private String digest;
	private String mail;
	private Set<RoleBO> roles;
	private Set<PersonnageWorkBO> personnagesAsPlayer;
	private Set<PersonnageWorkBO> personnagesAsGameMaster;
	
	
	
	@Override
	public boolean equals(Object object) {
		if(object instanceof UserBO){
			return username.equals(((UserBO) object).getUsername());
		}
		return false;
	}
	@Override
	public int hashCode() {
		return username.hashCode();
	}
	@Override
	public String toString() {
		return "UserBO("+username+")";
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDigest() {
		return digest;
	}
	public void setDigest(String digest) {
		this.digest = digest;
	}
	public Set<RoleBO> getRoles() {
		return roles;
	}
	public void setRoles(Set<RoleBO> roles) {
		this.roles = roles;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public Set<PersonnageWorkBO> getPersonnagesAsPlayer() {
		return personnagesAsPlayer;
	}
	public void setPersonnagesAsPlayer(Set<PersonnageWorkBO> personnagesAsPlayer) {
		this.personnagesAsPlayer = personnagesAsPlayer;
	}
	public Set<PersonnageWorkBO> getPersonnagesAsGameMaster() {
		return personnagesAsGameMaster;
	}
	public void setPersonnagesAsGameMaster(Set<PersonnageWorkBO> personnagesAsGameMaster) {
		this.personnagesAsGameMaster = personnagesAsGameMaster;
	}
	

}
