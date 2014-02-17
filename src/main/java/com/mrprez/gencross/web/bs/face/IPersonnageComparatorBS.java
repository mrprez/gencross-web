package com.mrprez.gencross.web.bs.face;

import java.util.Set;

import com.mrprez.gencross.Personnage;

public interface IPersonnageComparatorBS {
	
	Set<String> findPropertiesDifferences(Personnage personnage1, Personnage personnage2) throws Exception;
	
	boolean hasTheSameErrors(Personnage personnage1, Personnage personnage2) throws Exception;
	
	Set<Integer> findPointPoolDifferences(Personnage personnage1, Personnage personnage2) throws Exception;
	
	boolean hasTheSameNextPhaseAvaibility(Personnage personnage1, Personnage personnage2) throws Exception;

}
