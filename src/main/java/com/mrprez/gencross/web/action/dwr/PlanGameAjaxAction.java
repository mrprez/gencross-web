package com.mrprez.gencross.web.action.dwr;

import java.util.Date;

import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IPlanGameBS;
import com.opensymphony.xwork2.ActionContext;


public class PlanGameAjaxAction {
	
	private IPlanGameBS planGameBS;
	
	
	public void updatePlannedGame(Integer plannedGameId, String title, Date startDate, Date endDate) throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		
		planGameBS.updateGame(plannedGameId, title, startDate, endDate, user);
	}
	
	public void createPlannedGame(Integer tableId, String title, Date startDate, Date endDate) throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		
		planGameBS.createGame(tableId, title, startDate, endDate, user);
	}
	
	public void deletePlannedGame(Integer plannedGameId) throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
	
		planGameBS.deleteGame(plannedGameId, user);
	}

	public IPlanGameBS getPlanGameBS() {
		return planGameBS;
	}

	public void setPlanGameBS(IPlanGameBS planGameBS) {
		this.planGameBS = planGameBS;
	}

}
