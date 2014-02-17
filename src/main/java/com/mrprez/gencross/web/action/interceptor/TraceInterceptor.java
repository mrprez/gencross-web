package com.mrprez.gencross.web.action.interceptor;

import org.apache.log4j.Logger;

import com.mrprez.gencross.web.bo.UserBO;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class TraceInterceptor implements Interceptor {
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
		UserBO user = (UserBO) ActionContext.getContext().getSession().get("user");
		if(user!=null){
			Logger.getLogger("responseDelay").trace("START Transaction => Thread="+Thread.currentThread().getName()+", user="+user.getUsername()+", action="+ActionContext.getContext().getName());
		}
		String result = actionInvocation.invoke();
		if(user!=null){
			Logger.getLogger("responseDelay").trace("END   Transaction => Thread="+Thread.currentThread().getName()+", user="+user.getUsername()+", action="+ActionContext.getContext().getName());
		}
		return result;
	}

}
