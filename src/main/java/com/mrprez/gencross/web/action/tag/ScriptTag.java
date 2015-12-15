package com.mrprez.gencross.web.action.tag;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.struts2.ServletActionContext;

public class ScriptTag extends SimpleTagSupport {
	
	
	
	@Override
	public void doTag() throws JspException, IOException {
		HttpServletRequest request = ServletActionContext.getRequest();
		String context = (String) request.getAttribute("javax.servlet.include.context_path");
		String strutsViewUri = (String) request.getAttribute("struts.view_uri");
		String jsUri = strutsViewUri.replaceFirst("/jsp/", "/js/");
		jsUri = jsUri.substring(0, jsUri.lastIndexOf('.'))+".js";
		
		PageContext pageContext = (PageContext)getJspContext();
		if(new File(pageContext.getServletContext().getRealPath(jsUri)).exists()){
			getJspContext().getOut().write("<script language=\"JavaScript\" type=\"text/javascript\" src=\"");
			getJspContext().getOut().write(context);
			getJspContext().getOut().write(jsUri);
			getJspContext().getOut().write("\" charset=\"utf-8\"></script>");
			getJspContext().getOut().flush();
		}
	}

	

}
