package com.mrprez.gencross.web.dao.face;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.PlannedGameBO;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.TableMessageBO;
import com.mrprez.gencross.web.bo.UserBO;

public interface ITableDAO {
	TableBO loadTable(Integer id) throws Exception;
	
	Collection<TableBO> getTableFromGM(UserBO gm) throws Exception;
	
	void saveTable(TableBO table) throws Exception;

	TableBO getPersonnageTable(PersonnageWorkBO personnageWork) throws Exception;
	
	TableBO findTableByName(String name) throws Exception;

	void deleteMessage(TableMessageBO message) throws Exception;
	
	void savePlannedGame(PlannedGameBO plannedGame) throws Exception;
	
	void deletePlannedGame(PlannedGameBO plannedGame) throws Exception;

	PlannedGameBO loadPlannedGame(Integer plannedGameId) throws Exception;

	List<PlannedGameBO> loadTablePlannedGames(Integer tableId) throws Exception;

	PlannedGameBO findPlannedGame(Integer tableId, Date startDate) throws Exception;

	void deleteTable(TableBO table) throws Exception;


}
