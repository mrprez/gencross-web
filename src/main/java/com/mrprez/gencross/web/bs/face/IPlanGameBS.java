package com.mrprez.gencross.web.bs.face;

import java.util.Collection;
import java.util.Date;

import com.mrprez.gencross.web.bo.PlannedGameBO;
import com.mrprez.gencross.web.bo.UserBO;

public interface IPlanGameBS {
	
	
	void planGame(Integer tableId, PlannedGameBO plannedGame, UserBO user) throws Exception;

	Collection<PlannedGameBO> getPlannedGames(Integer tableId, Date date, Date date2);

	void replanGame(Integer tableId, Integer dayDelta, Integer minuteDelta, Date date, Date date2, UserBO user);

}
