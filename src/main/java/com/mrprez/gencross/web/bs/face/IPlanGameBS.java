package com.mrprez.gencross.web.bs.face;

import java.util.Collection;
import java.util.Date;

import com.mrprez.gencross.web.bo.PlannedGameBO;
import com.mrprez.gencross.web.bo.UserBO;

public interface IPlanGameBS {
	
	
	Collection<PlannedGameBO> getPlannedGames(Integer tableId) throws Exception;

	void updateGame(Integer plannedGameId, String title, Date startDate, Date endDate, UserBO user) throws Exception;

	void createGame(Integer tableId, String title, Date startDate, Date endDate, UserBO user) throws Exception;
	
	void deleteGame(Integer plannedGameId, UserBO user) throws Exception;

}
