package com.mrprez.gencross.web.action.util;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;

public class PersonnageTypeComparator implements InversableComparator<PersonnageWorkBO> {
	private int direction;
	
	
	public PersonnageTypeComparator(int sens) {
		super();
		this.direction = sens;
	}

	@Override
	public int compare(PersonnageWorkBO pw0, PersonnageWorkBO pw1) {
		return direction * pw0.getPluginName().compareTo(pw1.getPluginName());
	}

	@Override
	public int getDirection() {
		return direction;
	}

	@Override
	public String getName() {
		return "type";
	}
	

}
