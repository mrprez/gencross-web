package com.mrprez.gencross.web.dao.face;

import com.mrprez.gencross.Personnage;

public interface ISaveRepositoryResource {
	
	
	public void cleanOldRepository() throws Exception;
	
	public void moveFilesToOldRepository() throws Exception;
	
	public void savePersonnage(Personnage personnage, String name) throws Exception;

}
