package com.mrprez.gencross.web.action.interceptor;

import org.apache.log4j.Logger;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.web.bo.UserBO;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class VersionNumberInterceptor implements Interceptor {
	private static final long serialVersionUID = 1L;
	
	private String gencrossWebVersion;
	private String gencrossVersion;

	
	@Override
	public void destroy() {
		;
	}

	@Override
	public void init() {
		gencrossWebVersion = getClass().getPackage().getImplementationVersion();
		gencrossVersion = Personnage.class.getPackage().getImplementationVersion();
	}

	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		ActionContext.getContext().getSession().put("gencrossWebVersion", gencrossWebVersion);
		ActionContext.getContext().getSession().put("gencrossVersion", gencrossVersion);
		String result = actionInvocation.invoke();
		return result;
	}

}
