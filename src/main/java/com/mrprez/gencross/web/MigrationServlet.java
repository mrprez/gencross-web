package com.mrprez.gencross.web;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.web.context.ContextLoader;

import com.mrprez.gencross.web.bo.ParamBO;
import com.mrprez.gencross.web.bs.face.IParamsBS;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;

public class MigrationServlet implements Servlet {
	
	@Override
	public void destroy() {
	}

	@Override
	public ServletConfig getServletConfig() {
		return null;
	}

	@Override
	public String getServletInfo() {
		return null;
	}

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		try{
			IParamsBS paramsBS = (IParamsBS) ContextLoader.getCurrentWebApplicationContext().getBean("paramsBS");
			ParamBO param = paramsBS.getParam(ParamBO.MIGRATION);
			if(param!=null && (Boolean)param.getValue()) {
				IPersonnageBS personnageBS = (IPersonnageBS) ContextLoader.getCurrentWebApplicationContext().getBean("personnageBS");
				personnageBS.migrate();
			}
		}catch(Exception e){
			throw new ServletException(e);
		}
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
	}

}
