package com.mrprez.gencross.web.action.util;

import java.util.HashMap;
import java.util.Map;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.opensymphony.xwork2.ActionContext;

public class SessionUtil {
	private static String PERSONNAGES_WORKS_KEY = "personnagesWorks";
	

	@SuppressWarnings("unchecked")
	public static void putPersonnageWorkInSession(PersonnageWorkBO personnageWorkBO){
		if(!ActionContext.getContext().getSession().containsKey(PERSONNAGES_WORKS_KEY)){
			ActionContext.getContext().getSession().put(PERSONNAGES_WORKS_KEY, new HashMap<Integer, PersonnageWorkBO>());
		}
		((Map<Integer, PersonnageWorkBO>)ActionContext.getContext().getSession().get(PERSONNAGES_WORKS_KEY)).put(personnageWorkBO.getId(), personnageWorkBO);
	}
	
	@SuppressWarnings("unchecked")
	public static PersonnageWorkBO getPersonnageInSession(Integer personnageWorkId){
		if(!ActionContext.getContext().getSession().containsKey(PERSONNAGES_WORKS_KEY)){
			return null;
		}
		return ((Map<Integer, PersonnageWorkBO>)ActionContext.getContext().getSession().get(PERSONNAGES_WORKS_KEY)).get(personnageWorkId);
	}
	
	@SuppressWarnings("unchecked")
	public static void removePersonnageWorkFromSession(int personnageWorkId){
		if(ActionContext.getContext().getSession().containsKey(PERSONNAGES_WORKS_KEY)){
			((Map<Integer, PersonnageWorkBO>)ActionContext.getContext().getSession().get(PERSONNAGES_WORKS_KEY)).remove(Integer.valueOf(personnageWorkId));
		}
	}
	
}
