package com.mrprez.gencross.web.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;

import com.mrprez.gencross.web.bo.PersonnageDataBO;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.PersonnageXmlBO;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.dao.face.IPersonnageDAO;

public class PersonnageDAO extends AbstractDAO implements IPersonnageDAO {

	@Override
	public void savePersonnage(PersonnageDataBO personnageData) throws Exception {
		getSession().saveOrUpdate(personnageData);
	}
	
	@Override
	public void savePersonnageWork(PersonnageWorkBO personnageWork) throws Exception {
		getSession().saveOrUpdate(personnageWork);
	}
	
	@Override
	public PersonnageDataBO loadValidPersonnage(PersonnageWorkBO personnageWork) throws Exception {
		getSession().merge(personnageWork);
		PersonnageDataBO validPersonnageData = personnageWork.getValidPersonnageData();
		validPersonnageData.getPersonnage();
		return validPersonnageData;
	}

	@Override
	public PersonnageWorkBO loadPersonnageWork(int id) throws Exception {
		PersonnageWorkBO personnageWork = (PersonnageWorkBO)getSession().load(PersonnageWorkBO.class, id);
		if(personnageWork==null){
			return null;
		}
		personnageWork.getPersonnage();
		personnageWork.getBackground();
		return personnageWork;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PersonnageWorkBO> getPlayerPersonnageList(UserBO player) throws Exception {
		Criteria criteria = getSession().createCriteria(PersonnageWorkBO.class);
		criteria.add(Restrictions.eq("player", player));
		List<PersonnageWorkBO> result = criteria.list();
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PersonnageWorkBO> getGameMasterPersonnageList(UserBO gameMaster) throws Exception {
		Criteria criteria = getSession().createCriteria(PersonnageWorkBO.class);
		criteria.add(Restrictions.eq("gameMaster", gameMaster));
		List<PersonnageWorkBO> result = criteria.list();
		return result;
	}

	@Override
	public void deletePersonnage(PersonnageWorkBO personnageWork) throws Exception {
		getSession().update(personnageWork);
		getSession().delete(personnageWork);
		getSession().delete(personnageWork.getPersonnageData());
		getSession().delete(personnageWork.getValidPersonnageData());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PersonnageWorkBO> getAllPersonnages() throws Exception {
		Criteria criteria = getSession().createCriteria(PersonnageWorkBO.class);
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<PersonnageWorkBO> getLastModified(Date date) throws Exception {
		Criteria criteria = getSession().createCriteria(PersonnageWorkBO.class, "personnage_work");
		criteria.setFetchMode("personnageData", FetchMode.JOIN);
		criteria.createAlias("personnageData", "personnage_data").add(Restrictions.ge("personnage_data.lastUpdateDate", date));
		
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<PersonnageXmlBO> getAllXml() throws Exception {
		Criteria criteria = getSession().createCriteria(PersonnageXmlBO.class);
		return criteria.list();
	}
	
	@Override
	public void savePersonnageXml(PersonnageXmlBO personnageXml) {
		getSession().saveOrUpdate(personnageXml);
	}
	
	@Override
	public void unsavedChanges(PersonnageXmlBO personnageXml){
		getSession().evict(personnageXml);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<PersonnageWorkBO> getAddablePersonnages(TableBO table) throws Exception {
		getSession().saveOrUpdate(table);
		Criteria criteria = getSession().createCriteria(PersonnageWorkBO.class);
		criteria.add(Restrictions.eq("gameMaster", table.getGameMaster()));
		criteria.add(Restrictions.isNull("table"));
		criteria.add(Restrictions.eq("pluginName", table.getType()));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<PersonnageWorkBO> getPersonnageListFromType(String type) throws Exception {
		Criteria criteria = getSession().createCriteria(PersonnageWorkBO.class);
		criteria.add(Restrictions.eq("pluginName", type));
		return criteria.list();
	}

}
