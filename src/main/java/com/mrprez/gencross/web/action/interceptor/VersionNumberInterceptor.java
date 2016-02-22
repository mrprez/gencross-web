package com.mrprez.gencross.web.action.interceptor;

import java.io.IOException;
import java.util.Properties;

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
		Properties properties = new Properties();
		try {
			properties.load(getClass().getResourceAsStream("versions.properties"));
		} catch (IOException ioe) {
			throw new RuntimeException("Cannot load versions.properties file", ioe);
		}
		gencrossWebVersion = properties.getProperty("com.mrprez.gencross.gencross-web.version");
		gencrossVersion = properties.getProperty("com.mrprez.gencross.gencross.version");
	}
	
	
	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		if( ! ActionContext.getContext().getApplication().containsKey("gencrossWebVersion") ){
			ActionContext.getContext().getApplication().put("gencrossWebVersion", gencrossWebVersion);
		}
		if( ! ActionContext.getContext().getApplication().containsKey("gencrossVersion") ){
			ActionContext.getContext().getApplication().put("gencrossVersion", gencrossVersion);
		}
		return actionInvocation.invoke();
	}

}
