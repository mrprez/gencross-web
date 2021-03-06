package com.mrprez.gencross.web.dao.face;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.mrprez.gencross.web.bo.PersonnageDataBO;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.PersonnageXmlBO;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.UserBO;

public interface IPersonnageDAO {
	
	public void savePersonnage(PersonnageDataBO personnageData) throws Exception;
	
	public void savePersonnageWork(PersonnageWorkBO personnageWork) throws Exception;
	
	public PersonnageWorkBO loadPersonnageWork(int id) throws Exception;
	
	public PersonnageDataBO loadValidPersonnage(PersonnageWorkBO personnageWork) throws Exception;
	
	public List<PersonnageWorkBO> getPlayerPersonnageList(UserBO player) throws Exception;
	
	public List<PersonnageWorkBO> getGameMasterPersonnageList(UserBO gameMaster) throws Exception;

	public void deletePersonnage(PersonnageWorkBO personnageWork) throws Exception;
	
	public List<PersonnageWorkBO> getAllPersonnages() throws Exception;

	public Collection<PersonnageWorkBO> getLastModified(Date date) throws Exception;

	public Collection<PersonnageXmlBO> getXmlByType(String name) throws Exception;

	public void savePersonnageXml(PersonnageXmlBO personnageXml);

	public Collection<PersonnageWorkBO> getAddablePersonnages(TableBO table) throws Exception;

	public Collection<PersonnageWorkBO> getPersonnageListFromType(String type) throws Exception;
	
	public void evict(PersonnageXmlBO personnageXml) throws Exception;

	
}
