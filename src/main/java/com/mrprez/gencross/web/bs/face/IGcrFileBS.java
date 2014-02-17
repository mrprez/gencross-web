package com.mrprez.gencross.web.bs.face;

import java.io.File;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;

public interface IGcrFileBS {

	byte[] createPersonnageGcrAsPlayer(Integer personnageWorkId, UserBO user) throws Exception;

	byte[] createPersonnageGcrAsPlayer(PersonnageWorkBO personnageWork, UserBO user) throws Exception;
	
	byte[] createPersonnageGcrAsGameMaster(Integer personnageWorkId, UserBO user, String password) throws Exception;

	byte[] createPersonnageGcrAsGameMaster(PersonnageWorkBO personnageWork, UserBO user, String password) throws Exception;

	PersonnageWorkBO createPersonnageAsPlayer(File gcrFile, String personnageName, UserBO user) throws Exception;
	
	PersonnageWorkBO createPersonnageAsGameMaster(File gcrFile, String personnageName, UserBO user, String password) throws Exception;

	String uploadGcrPersonnage(File gcrFile, int personnageId, UserBO user, String password) throws Exception;


}
