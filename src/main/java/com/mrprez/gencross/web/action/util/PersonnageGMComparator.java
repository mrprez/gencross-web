package com.mrprez.gencross.web.action.util;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;

public class PersonnageGMComparator implements InversableComparator<PersonnageWorkBO> {
	private int direction;
	
	
	public PersonnageGMComparator(int sens) {
		super();
		this.direction = sens;
	}

	@Override
	public int compare(PersonnageWorkBO pw0, PersonnageWorkBO pw1) {
		if(pw0.getGameMaster() == null && pw1.getGameMaster() == null){
			return 0;
		}
		if(pw0.getGameMaster() == null){
			return direction;
		}
		if(pw1.getGameMaster() == null){
			return -direction;
		}
		
		return direction * pw0.getGameMaster().getUsername().compareTo(pw1.getGameMaster().getUsername());
	}
	
	@Override
	public int getDirection(){
		return direction;
	}

	@Override
	public String getName() {
		return "GM";
	}

}
