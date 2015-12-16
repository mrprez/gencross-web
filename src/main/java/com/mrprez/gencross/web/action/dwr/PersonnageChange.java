package com.mrprez.gencross.web.action.dwr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PersonnageChange {
	
	private Set<String> propertyNames = new HashSet<String>();
	
	private Set<String> pointPoolNames = new HashSet<String>();
	
	private boolean errorChanges = false;
	
	private String actionMessage;
	
	private Boolean phaseFinished;
	
	private List<Integer> newHistoryIndexes = new ArrayList<Integer>();

	
	
	public Set<String> getPropertyNames() {
		return propertyNames;
	}

	public void setPropertyNames(Set<String> propertyNames) {
		this.propertyNames = propertyNames;
	}

	public Set<String> getPointPoolNames() {
		return pointPoolNames;
	}

	public void setPointPoolNames(Set<String> pointPoolNames) {
		this.pointPoolNames = pointPoolNames;
	}

	public boolean getErrorChanges() {
		return errorChanges;
	}

	public void setErrorChanges(boolean errorChanges) {
		this.errorChanges = errorChanges;
	}

	public String getActionMessage() {
		return actionMessage;
	}

	public void setActionMessage(String actionMessage) {
		this.actionMessage = actionMessage;
	}

	public Boolean getPhaseFinished() {
		return phaseFinished;
	}

	public void setPhaseFinished(Boolean phaseFinished) {
		this.phaseFinished = phaseFinished;
	}

	public List<Integer> getNewHistoryIndexes() {
		return newHistoryIndexes;
	}

	public void setNewHistoryIndexes(List<Integer> newHistoryIndexes) {
		this.newHistoryIndexes = newHistoryIndexes;
	}

	

	
	
	
	

}
