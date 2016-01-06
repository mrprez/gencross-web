package com.mrprez.gencross.web.action.dwr;

import org.apache.log4j.Logger;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.web.action.util.SessionUtil;
import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.web.bs.face.IPersonnageComparatorBS;
import com.opensymphony.xwork2.ActionContext;

public class EditPersonnageAjaxAction {
	
	private IPersonnageBS personnageBS;
	private IPersonnageComparatorBS personnageComparatorBS;
	
	
	public void expand(int personnageWorkId, String propertyAbsoluteName) throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		PersonnageWorkBO personnageWork = personnageBS.loadPersonnage(personnageWorkId, user);
		if(personnageWork!=null){
			SessionUtil.addExpandingInSession(personnageWorkId, propertyAbsoluteName);
		}
	}
	
	public void collapse(int personnageWorkId, String propertyAbsoluteName) throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		PersonnageWorkBO personnageWork = personnageBS.loadPersonnage(personnageWorkId, user);
		if(personnageWork!=null){
			SessionUtil.removeExpandingFromSession(personnageWorkId, propertyAbsoluteName);
		}
	}
	
	
	public PersonnageChange updateValue(int personnageWorkId, String propertyAbsoluteName, String newValue) throws Exception {
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		PersonnageWorkBO personnageWork = personnageBS.loadPersonnage(personnageWorkId, user);
		if(personnageWork==null){
			Logger.getLogger(getClass()).warn("Personnage not in session (personnageWorkId="+personnageWorkId+", user="+ActionContext.getContext().getSession().get("user")+")");
			throw new Exception("Personnage not loaded");
		}
		Personnage personnageRef = personnageWork.getPersonnage().clone();
		personnageBS.setNewValue(personnageWork, newValue, propertyAbsoluteName);
		return loadDifferences(personnageWork.getPersonnage(), personnageRef);
	}
	
	
	public PersonnageChange addPropertyFromOption(int personnageWorkId, String motherPropertyName, String optionName, String specification) throws Exception{
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		PersonnageWorkBO personnageWork = personnageBS.loadPersonnage(personnageWorkId, user);
		if(personnageWork==null){
			Logger.getLogger(getClass()).warn("Personnage not in session (personnageWorkId="+personnageWorkId+", user="+ActionContext.getContext().getSession().get("user")+")");
			throw new Exception("Personnage not loaded");
		}
		Personnage personnageRef = personnageWork.getPersonnage().clone();
		personnageBS.addPropertyFromOption(personnageWork, motherPropertyName, optionName, specification);
		
		return loadDifferences(personnageWork.getPersonnage(), personnageRef);
	}
	
	
	public PersonnageChange addFreeProperty(int personnageWorkId, String motherPropertyName, String newPropertyName) throws Exception{
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		PersonnageWorkBO personnageWork = personnageBS.loadPersonnage(personnageWorkId, user);
		if(personnageWork==null){
			Logger.getLogger(getClass()).warn("Personnage not in session (personnageWorkId="+personnageWorkId+", user="+ActionContext.getContext().getSession().get("user")+")");
			throw new Exception("Personnage not loaded");
		}
		Personnage personnageRef = personnageWork.getPersonnage().clone();
		personnageBS.addFreeProperty(personnageWork, motherPropertyName, newPropertyName);
		
		return loadDifferences(personnageWork.getPersonnage(), personnageRef);
	}
	
	
	public PersonnageChange removeProperty(int personnageWorkId, String absolutePropertyName) throws Exception{
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		PersonnageWorkBO personnageWork = personnageBS.loadPersonnage(personnageWorkId, user);
		if(personnageWork==null){
			Logger.getLogger(getClass()).warn("Personnage not in session (personnageWorkId="+personnageWorkId+", user="+ActionContext.getContext().getSession().get("user")+")");
			throw new Exception("Personnage not loaded");
		}
		Personnage personnageRef = personnageWork.getPersonnage().clone();
		personnageBS.removeProperty(personnageWork, absolutePropertyName);
		
		return loadDifferences(personnageWork.getPersonnage(), personnageRef);
	}
	
	
	public PersonnageChange modifyPointPool(int personnageWorkId, String poolName, int addedValue) throws Exception{
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		PersonnageWorkBO personnageWork = personnageBS.loadPersonnage(personnageWorkId, user);
		if(personnageWork==null){
			Logger.getLogger(getClass()).warn("Personnage not in session (personnageWorkId="+personnageWorkId+", user="+ActionContext.getContext().getSession().get("user")+")");
			throw new Exception("Personnage not loaded");
		}
		Personnage personnageRef = personnageWork.getPersonnage().clone();
		personnageBS.modifyPointPool(personnageWork, poolName, addedValue);
		
		return loadDifferences(personnageWork.getPersonnage(), personnageRef);
	}
	
	
	public PersonnageChange modifyHistory(int personnageWorkId, int historyIndex, String poolName, int cost) throws Exception{
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		PersonnageWorkBO personnageWork = personnageBS.loadPersonnage(personnageWorkId, user);
		if(personnageWork==null){
			Logger.getLogger(getClass()).warn("Personnage not in session (personnageWorkId="+personnageWorkId+", user="+ActionContext.getContext().getSession().get("user")+")");
			throw new Exception("Personnage not loaded");
		}
		Personnage personnageRef = personnageWork.getPersonnage().clone();
		personnageBS.modifyHistory(personnageWork, poolName, cost, historyIndex);

		return loadDifferences(personnageWork.getPersonnage(), personnageRef);
	}
	
	
	private PersonnageChange loadDifferences(Personnage personnage, Personnage personnageRef) throws Exception{
		PersonnageChange change = new PersonnageChange();
		change.setPropertyNames(personnageComparatorBS.findPropertiesDifferences(personnage, personnageRef));
		change.setErrorChanges( ! personnageComparatorBS.hasTheSameErrors(personnage, personnageRef) );
		change.setPointPoolNames(personnageComparatorBS.findPointPoolDifferences(personnage, personnageRef));
		change.setActionMessage(personnage.getActionMessage());
		personnage.clearActionMessage();
		
		if(!personnageComparatorBS.hasTheSameNextPhaseAvaibility(personnage, personnageRef)){
			change.setPhaseFinished(personnage.phaseFinished());
		}
		for(int index=personnageRef.getHistory().size(); index<personnage.getHistory().size(); index++){
			change.getNewHistoryIndexes().add(index);
		}
		
		return change;
	}
	
	
	public IPersonnageBS getPersonnageBS() {
		return personnageBS;
	}


	public void setPersonnageBS(IPersonnageBS personnageBS) {
		this.personnageBS = personnageBS;
	}


	public IPersonnageComparatorBS getPersonnageComparatorBS() {
		return personnageComparatorBS;
	}


	public void setPersonnageComparatorBS(IPersonnageComparatorBS personnageComparatorBS) {
		this.personnageComparatorBS = personnageComparatorBS;
	}

}
