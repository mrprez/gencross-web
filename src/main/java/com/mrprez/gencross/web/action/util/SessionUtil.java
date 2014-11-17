package com.mrprez.gencross.web.action.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.opensymphony.xwork2.ActionContext;

public class SessionUtil {
	private static String PERSONNAGES_WORKS_KEY = "personnagesWorks";
	

	@SuppressWarnings("unchecked")
	public static void addExpandingInSession(int personnageWorkId, String expandedProperty){
		if( ! ActionContext.getContext().getSession().containsKey(PERSONNAGES_WORKS_KEY+personnageWorkId)){
			ActionContext.getContext().getSession().put(PERSONNAGES_WORKS_KEY+personnageWorkId, new HashSet<String>());
		}
		Set<String> expandedProperties = (Set<String>) ActionContext.getContext().getSession().get(PERSONNAGES_WORKS_KEY+personnageWorkId);
		expandedProperties.add(expandedProperty);
	}
	
	
	@SuppressWarnings("unchecked")
	public static void removeExpandingFromSession(int personnageWorkId, String expandedProperty){
		if(ActionContext.getContext().getSession().containsKey(PERSONNAGES_WORKS_KEY+personnageWorkId)){
			Map<String,String> expandedProperties = (Map<String, String>) ActionContext.getContext().getSession().get(PERSONNAGES_WORKS_KEY+personnageWorkId);
			expandedProperties.remove(expandedProperty);
		}
	}
	
	
	
	
}
