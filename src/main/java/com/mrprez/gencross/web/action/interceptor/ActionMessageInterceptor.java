package com.mrprez.gencross.web.action.interceptor;

import com.mrprez.gencross.web.bo.PersonnageWorkBO;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class ActionMessageInterceptor implements Interceptor {
	private static final long serialVersionUID = 1L;

	@Override
	public void destroy() {
		;
	}

	@Override
	public void init() {
		;
	}

	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		PersonnageWorkBO personnageWork = (PersonnageWorkBO) actionInvocation.getInvocationContext().getSession().get("personnageWork");
		if(personnageWork!=null){
			personnageWork.getPersonnage().clearActionMessage();
		}
		return actionInvocation.invoke();
	}

}
