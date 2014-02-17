package com.mrprez.gencross.web.action.util;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;

public class PersonnageTableComparator implements InversableComparator<PersonnageWorkBO> {
	private int direction;
	
	
	public PersonnageTableComparator(int sens) {
		super();
		this.direction = sens;
	}

	@Override
	public int compare(PersonnageWorkBO pw0, PersonnageWorkBO pw1) {
		if(pw0.getTable() == null && pw1.getTable() == null){
			return 0;
		}
		if(pw0.getTable() == null){
			return direction;
		}
		if(pw1.getTable() == null){
			return -direction;
		}
		
		return direction * pw0.getTable().compareTo(pw1.getTable());
	}

	@Override
	public int getDirection() {
		return direction;
	}

	@Override
	public String getName() {
		return "table";
	}
	
	

}
