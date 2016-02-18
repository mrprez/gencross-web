package com.mrprez.gencross.web;

import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.springframework.web.context.ContextLoader;

import com.mrprez.gencross.web.bs.face.ILoggerBS;

public class Log4jInitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	@Override
	public void init() throws ServletException {
		try{
			Enumeration<String> parameterNameList = this.getServletContext().getInitParameterNames();
			ILoggerBS loggerBS = (ILoggerBS) ContextLoader.getCurrentWebApplicationContext().getBean("loggerBS");
			while(parameterNameList.hasMoreElements()){
				String parameterName = parameterNameList.nextElement();
				if(parameterName.startsWith("log4j.")){
					String appenderName = parameterName.substring(6);
					Appender appender = loggerBS.getAppender(appenderName);
					if(appender instanceof FileAppender){
						FileAppender fileAppender = (FileAppender) appender;
						fileAppender.setFile(this.getServletContext().getInitParameter(parameterName));
						fileAppender.activateOptions();
					}
				}
			}
		}catch(Exception e){
			throw new ServletException(e);
		}
	}

	

}
