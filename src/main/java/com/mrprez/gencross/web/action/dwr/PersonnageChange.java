package com.mrprez.gencross.web.action.dwr;

import java.util.HashSet;
import java.util.Set;

public class PersonnageChange {
	
	private Set<String> propertyNames = new HashSet<String>();
	
	private Set<String> pointPoolNames = new HashSet<String>();
	
	private boolean errorChanges = false;
	
	private String actionMessage;
	
	private Boolean phaseFinished;
	
	private boolean historyChanges = false;

	
	
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

	public boolean isHistoryChanges() {
		return historyChanges;
	}

	public void setHistoryChanges(boolean historyChanges) {
		this.historyChanges = historyChanges;
	}

	public Boolean getPhaseFinished() {
		return phaseFinished;
	}

	public void setPhaseFinished(Boolean phaseFinished) {
		this.phaseFinished = phaseFinished;
	}
	
	
	

}
