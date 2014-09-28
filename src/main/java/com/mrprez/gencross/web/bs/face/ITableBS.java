package com.mrprez.gencross.web.bs.face;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.PlannedGameBO;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.TableMessageBO;
import com.mrprez.gencross.web.bo.UserBO;

public interface ITableBS {
	
	TableBO createTable(String name, UserBO gm, String type) throws Exception;

	Set<TableBO> getTableListForUser(UserBO user) throws Exception;
	
	TableBO getTableForGM(Integer id, UserBO gameMaster) throws Exception;

	TableBO addNewPersonnageToTable(Integer tableId, String personnageName, UserBO gm) throws Exception;

	TableBO getPersonnageTable(PersonnageWorkBO personnageWork) throws Exception;

	Collection<PersonnageWorkBO> getPjList(Integer tableId) throws Exception;

	String addPointsToPj(Integer tableId, UserBO gameMaster, String pointPoolName, Integer points) throws Exception;

	Collection<PersonnageWorkBO> getAddablePersonnages(TableBO table) throws Exception;

	PersonnageWorkBO addPersonnageToTable(Integer tableId, Integer personnageId, UserBO gameMaster) throws Exception;

	PersonnageWorkBO removePersonnageFromTable(Integer id, Integer personnageId, UserBO gameMaster) throws Exception;
	
	void addMessageToTable(String message, Integer tableId, UserBO author) throws Exception;
	
	void addSendMessage(String message, Integer tableId, UserBO author) throws Exception;

	Collection<TableMessageBO> connectTableMailBox() throws Exception;

	void removeMessageFromTable(Integer messageId, Integer id, UserBO user) throws Exception;

	Collection<String> getPointPoolList(Integer tableId) throws Exception;
	
	Collection<PlannedGameBO> getPlannedGames(Integer tableId, Date startDate, Date endDate) throws Exception;

	void replanGame(Integer tableId, Integer dayDelta, Integer minuteDelta, Date startDate, Date endDate, UserBO user) throws Exception;

	
}
