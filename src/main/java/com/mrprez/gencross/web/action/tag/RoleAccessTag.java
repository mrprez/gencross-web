package com.mrprez.gencross.web.action.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.mrprez.gencross.web.bo.RoleBO;
import com.mrprez.gencross.web.bo.UserBO;

public class RoleAccessTag extends BodyTagSupport {
	private static final long serialVersionUID = 1L;
	private RoleBO allowedRole;
	

	@Override
	public int doStartTag() throws JspException {
		UserBO user = (UserBO) pageContext.getSession().getAttribute("user");
		if(user==null){
			return SKIP_BODY;
		}
		if(user.getRoles().contains(allowedRole)){
			return EVAL_BODY_INCLUDE;
		}
		return SKIP_BODY;
	}


	public String getAllowedRole() {
		return allowedRole.getName();
	}
	public void setAllowedRole(String allowedRoleName) {
		this.allowedRole = new RoleBO();
		this.allowedRole.setName(allowedRoleName);
	}


	
	
	
	
	

}
