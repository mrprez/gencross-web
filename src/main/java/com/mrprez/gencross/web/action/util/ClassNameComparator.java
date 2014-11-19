package com.mrprez.gencross.web.action.util;

import java.util.Comparator;

public class ClassNameComparator implements Comparator<Class<?>> {

	@Override
	public int compare(Class<?> arg0, Class<?> arg1) {
		if(arg0==null && arg1==null){
			return 0;
		}
		if(arg0==null){
			return 1;
		}
		if(arg1==null){
			return -1;
		}
		
		return arg0.getName().compareTo(arg1.getName());
	}

}
