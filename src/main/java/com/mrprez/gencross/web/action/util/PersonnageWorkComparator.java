package com.mrprez.gencross.web.action.util;

import java.util.Comparator;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;



public class PersonnageWorkComparator implements Comparator<PersonnageWorkBO> {

	
	@Override
	public int compare(PersonnageWorkBO arg0, PersonnageWorkBO arg1) {
		if(arg0.getName()==null && arg1.getName()==null){
			return 0;
		}
		if(arg0.getName()==null){
			return -1;
		}
		if(arg1.getName()==null){
			return 1;
		}
		return arg0.getName().compareTo(arg1.getName());
	}

	
}
