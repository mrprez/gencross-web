package com.mrprez.gencross.web.action.util;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;

public class PersonnageNameComparator implements InversableComparator<PersonnageWorkBO> {
	private int direction;
	
	
	public PersonnageNameComparator(int sens) {
		super();
		this.direction = sens;
	}

	@Override
	public int compare(PersonnageWorkBO pw0, PersonnageWorkBO pw1) {
		return direction * pw0.getName().compareTo(pw1.getName());
	}

	@Override
	public int getDirection() {
		return direction;
	}

	@Override
	public String getName() {
		return "name";
	}
	

}
