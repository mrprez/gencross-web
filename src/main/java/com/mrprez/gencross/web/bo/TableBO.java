package com.mrprez.gencross.web.bo;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class TableBO implements Comparable<TableBO>{
	private Integer id;
	private String type;
	private String name;
	private UserBO gameMaster;
	private Set<PersonnageWorkBO> personnages;
	private SortedSet<TableMessageBO> messages = new TreeSet<TableMessageBO>();
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public UserBO getGameMaster() {
		return gameMaster;
	}
	public void setGameMaster(UserBO gameMaster) {
		this.gameMaster = gameMaster;
	}
	public Set<PersonnageWorkBO> getPersonnages() {
		return personnages;
	}
	public void setPersonnages(Set<PersonnageWorkBO> personnages) {
		this.personnages = personnages;
	}
	public SortedSet<TableMessageBO> getMessages() {
		return messages;
	}
	public void setMessages(SortedSet<TableMessageBO> messages) {
		this.messages = messages;
	}
	@Override
	public int compareTo(TableBO otherTable) {
		return name.compareTo(otherTable.getName());
	}
	
	
}
