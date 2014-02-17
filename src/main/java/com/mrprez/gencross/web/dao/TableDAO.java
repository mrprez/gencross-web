package com.mrprez.gencross.web.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.TableMessageBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.dao.face.ITableDAO;

public class TableDAO extends AbstractDAO implements ITableDAO {

	@SuppressWarnings("unchecked")
	@Override
	public Collection<TableBO> getTableFromGM(UserBO gm) throws Exception {
		Criteria criteria = getSession().createCriteria(TableBO.class);
		criteria.add(Restrictions.eq("gameMaster", gm));
		return criteria.list();
	}

	@Override
	public TableBO loadTable(Integer id) throws Exception {
		return (TableBO) getSession().get(TableBO.class, id);
	}

	@Override
	public void saveTable(TableBO table) throws Exception {
		getSession().saveOrUpdate(table);
	}
	
	@Override
	public TableBO getPersonnageTable(PersonnageWorkBO personnageWork) throws Exception {
		getSession().saveOrUpdate(personnageWork);
		return personnageWork.getTable();
	}

	@Override
	public TableBO findTableByName(String name) throws Exception {
		Criteria criteria = getSession().createCriteria(TableBO.class);
		criteria.add(Restrictions.eq("name", name));
		@SuppressWarnings("unchecked")
		List<TableBO> result = criteria.list();
		if(result.size() == 1){
			return result.get(0);
		}
		return null;
	}

	@Override
	public void deleteMessage(TableMessageBO message) throws Exception {
		getSession().delete(message);
	}
	
	

}
