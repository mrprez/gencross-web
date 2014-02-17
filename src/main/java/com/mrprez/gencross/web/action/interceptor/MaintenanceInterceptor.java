package com.mrprez.gencross.web.action.interceptor;

import java.util.Date;

import com.mrprez.gencross.web.bo.RoleBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class MaintenanceInterceptor implements Interceptor {
	private static final long serialVersionUID = 1L;
	private static boolean maintenance = false;
	private static Date lastMaintenanceDate;
	

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
		if(maintenance){
			UserBO userBO = (UserBO)ActionContext.getContext().getSession().get("user");
			if(userBO!=null && userBO.getRoles().contains(new RoleBO("manager"))){
				return actionInvocation.invoke();
			}
			ActionContext.getContext().getSession().clear();
			return "Maintenance";
		}
		if(ActionContext.getContext().getSession().get("loginDate")!=null && lastMaintenanceDate!=null){
			Date loginDate = (Date) ActionContext.getContext().getSession().get("loginDate");
			if(loginDate.before(lastMaintenanceDate)){
				ActionContext.getContext().getSession().clear();
				return SecurityInterceptor.LOGIN_ACTION;
			}
		}
		return actionInvocation.invoke();
	}

	public static boolean isMaintenance() {
		return maintenance;
	}

	public static void setMaintenance(boolean maintenance) {
		if(MaintenanceInterceptor.maintenance && !maintenance){
			lastMaintenanceDate = new Date();
		}
		MaintenanceInterceptor.maintenance = maintenance;
	}
	
	

}
