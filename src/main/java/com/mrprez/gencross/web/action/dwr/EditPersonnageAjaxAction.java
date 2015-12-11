package com.mrprez.gencross.web.action.dwr;

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
