package com.mrprez.gencross.web.action.util;

import java.util.Comparator;

public interface InversableComparator<T> extends Comparator<T> {
	
	int getDirection();
	String getName();

}
