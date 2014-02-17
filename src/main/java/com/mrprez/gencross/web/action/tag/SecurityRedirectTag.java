package com.mrprez.gencross.web.action.tag;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.mrprez.gencross.web.bo.RoleBO;
import com.mrprez.gencross.web.bo.UserBO;

public class SecurityRedirectTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	private RoleBO role;
	private String target;
	
	
	
	@Override
	public int doEndTag() throws JspException {
		try {
			UserBO user = (UserBO) pageContext.getSession().getAttribute("user");
			if(user==null){
				pageContext.forward(target);
				return SKIP_PAGE;
			}
			if(role!=null && !user.getRoles().contains(role)){
				pageContext.forward(target);
				return SKIP_PAGE;
			}
			
			return EVAL_PAGE;
		} catch (ServletException e) {
			throw new JspException(e);
		} catch (IOException e) {
			throw new JspException(e);
		}
	}


	public String getRole() {
		return role.getName();
	}
	public void setRole(String roleName) {
		this.role = new RoleBO(roleName);
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	

}
