package com.mrprez.gencross.web.action.interceptor;

import java.util.HashSet;
import java.util.Set;

import org.springframework.web.context.ContextLoader;

import com.mrprez.gencross.web.bo.RoleBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IAuthentificationBS;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class SecurityInterceptor implements Interceptor {
	private static final long serialVersionUID = 1L;
	public static final String LOGIN_ACTION = "Login";
	public static final String FORBIDEN = "Forbiden";
	private Set<RoleBO> roles;
	
	

	@Override
	public void destroy() {
		
	}

	@Override
	public void init() {
		
	}

	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		UserBO user = (UserBO)actionInvocation.getInvocationContext().getSession().get("user");
		if(user==null){
			return LOGIN_ACTION;
		}
		if(roles!=null){
			for(RoleBO userRole : user.getRoles()){
				if(roles.contains(userRole)){
					return actionInvocation.invoke();
				}
			}
			return FORBIDEN;
		}
		return actionInvocation.invoke();
	}
	
	public void setAllowedRoles(String allowedRoles) throws Exception{
		IAuthentificationBS authentificationBS = (IAuthentificationBS)ContextLoader.getCurrentWebApplicationContext().getBean("authentificationBS");
		String allowedRolesTab[] = allowedRoles.split(",");
		roles = new HashSet<RoleBO>(allowedRolesTab.length);
		for(int i=0; i<allowedRolesTab.length; i++){
			RoleBO role = authentificationBS.getRole(allowedRolesTab[i].trim());
			if(role==null){
				throw new Exception("Role inconnu");
			}
			roles.add(role);
		}
		
	}
	
	

}
