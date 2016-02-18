package com.mrprez.gencross.web.action.interceptor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

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
		gencrossWebVersion = findPomVersion("/META-INF/maven/com.mrprez.gencross/gencross-web/pom.properties");
		gencrossVersion = findPomVersion("/META-INF/maven/com.mrprez.gencross/gencross/pom.properties");
	}
	
	private String findPomVersion(String propertiesPomPath){
		try{
			InputStream is = getClass().getResourceAsStream(propertiesPomPath);
			if(is==null){
				return null;
			}
			Properties properties = new Properties();
			properties.load(is);
			return properties.getProperty("version");
		}catch(IOException ioe){
			Logger.getLogger(this.getClass()).error("Cannot load file "+propertiesPomPath, ioe);
			return "Cannot load file "+propertiesPomPath;
		}
	}
	

	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		ActionContext.getContext().getSession().put("gencrossWebVersion", gencrossWebVersion);
		ActionContext.getContext().getSession().put("gencrossVersion", gencrossVersion);
		String result = actionInvocation.invoke();
		return result;
	}

}
