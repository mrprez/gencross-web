package com.mrprez.gencross.web.action.util;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;

public class PersonnagePlayerComparator implements InversableComparator<PersonnageWorkBO> {
	private int direction;
	
	
	public PersonnagePlayerComparator(int sens) {
		super();
		this.direction = sens;
	}

	@Override
	public int compare(PersonnageWorkBO pw0, PersonnageWorkBO pw1) {
		if(pw0.getPlayer() == null && pw1.getPlayer() == null){
			return 0;
		}
		if(pw0.getPlayer() == null){
			return direction;
		}
		if(pw1.getPlayer() == null){
			return -direction;
		}
		
		return direction * pw0.getPlayer().getUsername().compareTo(pw1.getPlayer().getUsername());
	}

	@Override
	public int getDirection() {
		return direction;
	}

	@Override
	public String getName() {
		return "player";
	}
	
	

}
