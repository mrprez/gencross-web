package com.mrprez.gencross.web.action;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.mrprez.gencross.disk.PluginDescriptor;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class CreatePersonnageAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private static final String GAME_MASTER = "Maître de jeux";
	private static final String PLAYER = "Joueur";
	private static final String BOTH = "Les deux";
	private static final String NO_ONE_KEY = "_no_one_";
	
	private IPersonnageBS personnageBS;
	private IAuthentificationBS authentificationBS;
	
	private String selectedPersonnageTypeName;
	private Collection<PluginDescriptor> personnageTypeList;
	private String personnageName;
	private Integer personnageId;
	private String role = PLAYER;
	private List<String> roleList = Arrays.asList(GAME_MASTER, PLAYER, BOTH);
	private List<UserBO> userList;
	private String gameMasterName;
	private String playerName;
	
	
	public CreatePersonnageAction(){
		super();
	}
	
	public String execute() throws Exception {
		personnageTypeList = personnageBS.getAvailablePersonnageTypes();
		
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		userList = authentificationBS.getUserList();
		userList.remove(user);
		
		return INPUT;
	}
	
	public String create() throws Exception {
		if(StringUtils.isBlank(personnageName)){
			this.addActionError("Veuillez renseigner un nom pour ce personnage.");
			execute();
			return ERROR;
		}
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		
		PersonnageWorkBO personnageWork;
		if(role.equals(GAME_MASTER)){
			playerName = playerName.equals(NO_ONE_KEY) ? null : playerName;
			personnageWork = personnageBS.createPersonnageAsGameMaster(selectedPersonnageTypeName, personnageName, playerName, user);
		}else if(role.equals(PLAYER)){
			gameMasterName = gameMasterName.equals(NO_ONE_KEY) ? null : gameMasterName;
			personnageWork = personnageBS.createPersonnageAsPlayer(selectedPersonnageTypeName, personnageName, gameMasterName, user);
		}else if(role.equals(BOTH)){
			personnageWork = personnageBS.createPersonnageAsGameMasterAndPlayer(selectedPersonnageTypeName, personnageName, user);
		}else{
			this.addActionError("Vous devez choisir un rôle par rapport à votre personnage.");
			execute();
			return ERROR;
		}
		personnageWork.setName(personnageName);
		personnageId = personnageWork.getId();
		return SUCCESS;
	}
	
	public Collection<PluginDescriptor> getPersonnageTypeList(){
		return personnageTypeList;
	}
	
	public List<UserBO> getUserList() throws Exception{
		return userList;
	}

	public String getSelectedPersonnageTypeName() {
		return selectedPersonnageTypeName;
	}
	public void setSelectedPersonnageTypeName(String selectedPersonnageTypeName) {
		this.selectedPersonnageTypeName = selectedPersonnageTypeName;
	}
	public String getPersonnageName() {
		return personnageName;
	}
	public void setPersonnageName(String personnageName) {
		this.personnageName = personnageName;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Integer getPersonnageId() {
		return personnageId;
	}
	public List<String> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}
	public String getGameMasterName() {
		return gameMasterName;
	}
	public void setGameMasterName(String gameMasterName) {
		this.gameMasterName = gameMasterName;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public String getNoGmKey(){
		return NO_ONE_KEY;
	}
	public IPersonnageBS getPersonnageBS() {
		return personnageBS;
	}
	public void setPersonnageBS(IPersonnageBS personnageBS) {
		this.personnageBS = personnageBS;
	}
	public IAuthentificationBS getAuthentificationBS() {
		return authentificationBS;
	}
	public void setAuthentificationBS(IAuthentificationBS authentificationBS) {
		this.authentificationBS = authentificationBS;
	}
	

}
