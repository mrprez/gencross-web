package com.mrprez.gencross.web.bo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.mrprez.gencross.Personnage;

public class PersonnageWorkBO {
	private Integer id;
	private UserBO player;
	private UserBO gameMaster;
	private String name;
	private PersonnageDataBO personnageData = new PersonnageDataBO();
	private PersonnageDataBO validPersonnageData = new PersonnageDataBO();
	private Map<String, String> propertiesExpanding = new HashMap<String, String>();
	private Date validationDate;
	private Date lastUpdateDate;
	private TableBO table;
	private String pluginName;
	private String background;
	
	
	public UserBO getPlayer() {
		return player;
	}
	public void setPlayer(UserBO player) {
		this.player = player;
	}
	public UserBO getGameMaster() {
		return gameMaster;
	}
	public void setGameMaster(UserBO gameMaster) {
		this.gameMaster = gameMaster;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Map<String, String> getPropertiesExpanding() {
		return propertiesExpanding;
	}
	public void setPropertiesExpanding(Map<String, String> propertiesExpanding) {
		this.propertiesExpanding = propertiesExpanding;
	}
	public PersonnageDataBO getPersonnageData() {
		return personnageData;
	}
	public void setPersonnageData(PersonnageDataBO personnageData) {
		this.personnageData = personnageData;
	}
	public PersonnageDataBO getValidPersonnageData() {
		return validPersonnageData;
	}
	public void setValidPersonnageData(PersonnageDataBO validPersonnageData) {
		this.validPersonnageData = validPersonnageData;
	}
	public Personnage getPersonnage(){
		return personnageData.getPersonnage();
	}
	public Personnage getValidPersonnage(){
		return validPersonnageData.getPersonnage();
	}
	public Date getValidationDate() {
		return validationDate;
	}
	public void setValidationDate(Date validationDate) {
		this.validationDate = validationDate;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public TableBO getTable() {
		return table;
	}
	public void setTable(TableBO table) {
		this.table = table;
	}
	public String getPluginName() {
		return pluginName;
	}
	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}
	public String getBackground() {
		return background;
	}
	public void setBackground(String background) {
		this.background = background;
	}
	

}
