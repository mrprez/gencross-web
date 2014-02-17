package com.mrprez.gencross.web.bs.face;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.disk.PluginDescriptor;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;

public interface IPersonnageBS {
	
	Collection<PluginDescriptor> getAvailablePersonnageTypes() throws Exception;
	
	PersonnageWorkBO createPersonnageAsPlayer(String pluginName, String name, String gmName, UserBO user) throws Exception;
	
	PersonnageWorkBO createPersonnageAsGameMaster(String pluginName, String name, String playerName, UserBO user) throws Exception;
	
	PersonnageWorkBO createPersonnageAsGameMasterAndPlayer(String pluginName, String name, UserBO user) throws Exception;
	
	boolean setNewValue(PersonnageWorkBO personnageWorkBO, String newValueString, String absoluteName) throws Exception;
	
	void savePersonnage(PersonnageWorkBO personnageWorkBO) throws Exception;
	
	void savePersonnageWork(PersonnageWorkBO personnageWorkBO) throws Exception;
	
	List<PersonnageWorkBO> getPlayerPersonnageList(UserBO player) throws Exception;
	
	List<PersonnageWorkBO> getGameMasterPersonnageList(UserBO player) throws Exception;
	
	PersonnageWorkBO loadPersonnage(Integer personnageId, UserBO user) throws Exception;
	
	PersonnageWorkBO loadPersonnage(Integer personnageId) throws Exception;
	
	PersonnageWorkBO loadPersonnageAsPlayer(Integer personnageId, UserBO player) throws Exception;
	
	PersonnageWorkBO loadPersonnageAsGameMaster(Integer personnageId, UserBO gameMaster) throws Exception;
	
	boolean addPropertyFromOption(PersonnageWorkBO personnageWorkBO, String motherPropertyName, String optionName, String specification) throws Exception;

	boolean addFreeProperty(PersonnageWorkBO personnageWorkBO, String motherPropertyName, String newPropertyName) throws Exception;
	
	boolean removeProperty(PersonnageWorkBO personnageWorkBO, String absolutePropertyName) throws Exception;
	
	void nextPhase(PersonnageWorkBO personnageWorkBO) throws Exception;
	
	void changeComment(PersonnageWorkBO personnageWorkBO, String propertyAbsoluteName, String newComment) throws Exception;
	
	void modifyPointPool(PersonnageWorkBO personnageWorkBO, String pointPoolName, Integer modification) throws Exception;
	
	void modifyHistory(PersonnageWorkBO personnageWorkBO, String pointPoolName, Integer cost, int index) throws Exception;

	void validatePersonnage(PersonnageWorkBO personnageWorkBO) throws Exception;

	void unvalidatePersonnage(PersonnageWorkBO personnageWorkBO) throws Exception;
	
	void attribute(PersonnageWorkBO personnageWork, UserBO player, UserBO gameMaster) throws Exception;

	void deletePersonnageFromUser(int personnageWorkId, UserBO user) throws Exception;

	Map<Integer, Double> getLastMigrationResult() throws Exception;

	Collection<PersonnageWorkBO> getPersonnageListFromClass(Class<? extends Personnage> clazz) throws Exception;

	List<PersonnageWorkBO> getGameMasterPersonnageList(UserBO gameMaster, String type) throws Exception;

	Collection<PersonnageWorkBO> getPersonnageListFromType(String type) throws Exception;

	void migrate() throws Exception;

	void savePersonnageBackground(PersonnageWorkBO personnageWork, String background) throws Exception;

	
}
