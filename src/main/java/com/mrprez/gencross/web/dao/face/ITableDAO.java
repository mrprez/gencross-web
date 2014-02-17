package com.mrprez.gencross.web.dao.face;

import java.util.Collection;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.TableMessageBO;
import com.mrprez.gencross.web.bo.UserBO;

public interface ITableDAO extends IAbstractDAO {
	TableBO loadTable(Integer id) throws Exception;
	
	Collection<TableBO> getTableFromGM(UserBO gm) throws Exception;
	
	void saveTable(TableBO table) throws Exception;

	TableBO getPersonnageTable(PersonnageWorkBO personnageWork) throws Exception;
	
	TableBO findTableByName(String name) throws Exception;

	void deleteMessage(TableMessageBO message) throws Exception;


}
