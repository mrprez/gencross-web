package com.mrprez.gencross.web.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mrprez.gencross.web.action.util.InversableComparator;
import com.mrprez.gencross.web.action.util.PersonnageGMComparator;
import com.mrprez.gencross.web.action.util.PersonnageNameComparator;
import com.mrprez.gencross.web.action.util.PersonnagePlayerComparator;
import com.mrprez.gencross.web.action.util.PersonnageTableComparator;
import com.mrprez.gencross.web.action.util.PersonnageTypeComparator;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IGcrFileBS;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class PersonnageListAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private static final String PLAYER_PERONNAGE_SORT_KEY = "playerPersonnageSort";
	private static final String GM_PERONNAGE_SORT_KEY = "gameMasterPersonnageSort";
	private static Map<String, InversableComparator<PersonnageWorkBO>> ascComparators;
	private static Map<String, InversableComparator<PersonnageWorkBO>> decComparators;
	
	private IPersonnageBS personnageBS;
	private IGcrFileBS gcrFileBS;
	
	private List<PersonnageWorkBO> playerPersonnageList;
	private List<PersonnageWorkBO> gameMasterPersonnageList;
	
	private Integer personnageId;
	private String password;
	private String fileName;
	private Integer fileSize;
	private InputStream inputStream;
	private String playerPersonnageSort;
	private String gameMasterPersonnageSort;
	
	
	public String execute() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		
		playerPersonnageList = personnageBS.getPlayerPersonnageList(user);
		Collections.sort(playerPersonnageList, getPlayerPersonnageComparator());
		
		gameMasterPersonnageList = personnageBS.getGameMasterPersonnageList(user);
		Collections.sort(gameMasterPersonnageList, getGMPersonnageComparator());
		
		return INPUT;
	}
	
	public String sort() throws Exception{
		if(playerPersonnageSort != null){
			Comparator<PersonnageWorkBO> comparator = getAscComparator(playerPersonnageSort);
			if(comparator == ActionContext.getContext().getSession().get(PLAYER_PERONNAGE_SORT_KEY)){
				comparator = getDecComparator(playerPersonnageSort);
			}
			ActionContext.getContext().getSession().put(PLAYER_PERONNAGE_SORT_KEY, comparator);
		}
		if(gameMasterPersonnageSort != null){
			Comparator<PersonnageWorkBO> comparator = getAscComparator(gameMasterPersonnageSort);
			if(comparator == ActionContext.getContext().getSession().get(GM_PERONNAGE_SORT_KEY)){
				comparator = getDecComparator(gameMasterPersonnageSort);
			}
			ActionContext.getContext().getSession().put(GM_PERONNAGE_SORT_KEY, comparator);
		}
		return SUCCESS;
	}
	
	
	public String deletePersonnage() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		personnageBS.deletePersonnageFromUser(personnageId, user);
		return SUCCESS;
	}
	
	public String downloadAsPlayer() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		
		byte download[] = gcrFileBS.createPersonnageGcrAsPlayer(personnageId, user);
		if(download==null){
			addActionError("Impossible de trouver ce personnage");
			return ERROR;
		}
		fileSize = download.length;
		inputStream = new ByteArrayInputStream(download);
		
		PersonnageWorkBO personnageWork = personnageBS.loadPersonnage(personnageId, user);
		fileName = personnageWork.getName().replace(" ", "_")+".gcr";
		
		return "download";
	}
	
	public String downloadAsGameMaster() throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		
		byte download[] = gcrFileBS.createPersonnageGcrAsGameMaster(personnageId, user, password);
		if(download==null){
			addActionError("Impossible de trouver ce personnage");
			return ERROR;
		}
		fileSize = download.length;
		inputStream = new ByteArrayInputStream(download);
		
		PersonnageWorkBO personnageWork = personnageBS.loadPersonnage(personnageId, user);
		fileName = personnageWork.getName().replace(" ", "_")+".gcr";
		
		return "download";
	}

	public List<PersonnageWorkBO> getPlayerPersonnageList() {
		return playerPersonnageList;
	}
	public List<PersonnageWorkBO> getGameMasterPersonnageList() {
		return gameMasterPersonnageList;
	}
	public String getFileName() {
		return fileName;
	}
	public Integer getFileSize() {
		return fileSize;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setPersonnageId(Integer personnageId) {
		this.personnageId = personnageId;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setPlayerPersonnageSort(String playerPersonnageSort) {
		this.playerPersonnageSort = playerPersonnageSort;
	}
	public void setGameMasterPersonnageSort(String gameMasterPersonnageSort) {
		this.gameMasterPersonnageSort = gameMasterPersonnageSort;
	}
	public String getGameMasterSortDir() {
		switch(getGMPersonnageComparator().getDirection()){
		case 1:
			return "asc";
		case -1:
			return "dec";
		default:
			throw new IllegalArgumentException("Direction should be 1 or -1");
		}
	}
	public String getPlayerSortDir() {
		switch(getPlayerPersonnageComparator().getDirection()){
		case 1:
			return "asc";
		case -1:
			return "dec";
		default:
			throw new IllegalArgumentException("Direction should be 1 or -1");
		}
	}

	@SuppressWarnings("unchecked")
	public InversableComparator<PersonnageWorkBO> getPlayerPersonnageComparator(){
		InversableComparator<PersonnageWorkBO> comparator = (InversableComparator<PersonnageWorkBO>) ActionContext.getContext().getSession().get(PLAYER_PERONNAGE_SORT_KEY);
		if(comparator == null){
			comparator = getAscComparator("name");
			ActionContext.getContext().getSession().put(PLAYER_PERONNAGE_SORT_KEY, comparator);
		}
		return comparator;
	}
	
	@SuppressWarnings("unchecked")
	public InversableComparator<PersonnageWorkBO> getGMPersonnageComparator(){
		InversableComparator<PersonnageWorkBO> comparator = (InversableComparator<PersonnageWorkBO>) ActionContext.getContext().getSession().get(GM_PERONNAGE_SORT_KEY);
		if(comparator == null){
			comparator = getAscComparator("name");
			ActionContext.getContext().getSession().put(GM_PERONNAGE_SORT_KEY, comparator);
		}
		return comparator;
	}
	
	private static InversableComparator<PersonnageWorkBO> getAscComparator(String name){
		if(ascComparators == null){
			initComparators();
		}
		return ascComparators.get(name);
	}
	
	private static InversableComparator<PersonnageWorkBO> getDecComparator(String name){
		if(decComparators == null){
			initComparators();
		}
		return decComparators.get(name);
	}
	
	private static synchronized void initComparators(){
		if(ascComparators == null || decComparators == null){
			ascComparators = new HashMap<String, InversableComparator<PersonnageWorkBO>>();
			decComparators = new HashMap<String, InversableComparator<PersonnageWorkBO>>();
			
			addComparator(new PersonnageNameComparator(1));
			addComparator(new PersonnageTableComparator(1));
			addComparator(new PersonnageTypeComparator(1));
			addComparator(new PersonnagePlayerComparator(1));
			addComparator(new PersonnageGMComparator(1));
			
			addComparator(new PersonnageNameComparator(-1));
			addComparator(new PersonnageTableComparator(-1));
			addComparator(new PersonnageTypeComparator(-1));
			addComparator(new PersonnagePlayerComparator(-1));
			addComparator(new PersonnageGMComparator(-1));
		}
	}
	
	private static void addComparator(InversableComparator<PersonnageWorkBO> comparator){
		if(comparator.getDirection() == 1){
			ascComparators.put(comparator.getName(), comparator);
		}
		if(comparator.getDirection() == -1){
			decComparators.put(comparator.getName(), comparator);
		}
	}

	public IPersonnageBS getPersonnageBS() {
		return personnageBS;
	}

	public void setPersonnageBS(IPersonnageBS personnageBS) {
		this.personnageBS = personnageBS;
	}
	
	public IGcrFileBS getGcrFileBS() {
		return gcrFileBS;
	}

	public void setGcrFileBS(IGcrFileBS gcrFileBS) {
		this.gcrFileBS = gcrFileBS;
	}
	
	

}
