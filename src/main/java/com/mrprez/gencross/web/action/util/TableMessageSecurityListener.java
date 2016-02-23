package com.mrprez.gencross.web.action.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.owasp.html.HtmlChangeListener;

import com.mrprez.gencross.web.bo.UserBO;

public class TableMessageSecurityListener implements HtmlChangeListener<UserBO> {
	private List<String> intrusionAttempts;

	@Override
	public void discardedAttributes(UserBO user, String tagName, String... attributeNames) {
		addIntrusionAttempts( user.getUsername()+" attempt to break-in system with table message"
				+ " containing tag <"+tagName+">"
				+ " with attributes ["+StringUtils.join(attributeNames, ", ")+"]");
	}

	@Override
	public void discardedTag(UserBO user, String tagName) {
		addIntrusionAttempts( user.getUsername()+" attempt to break-in system with table message"
				+ " containing tag <"+tagName+">");
	}
	
	private void addIntrusionAttempts(String intrusionAttempt){
		if(intrusionAttempts==null){
			intrusionAttempts = new ArrayList<String>();
		}
		intrusionAttempts.add(intrusionAttempt);
	}

	public List<String> getIntrusionAttempts() {
		return intrusionAttempts;
	}
	
	public boolean hasIntrusionAttempts(){
		return intrusionAttempts!=null;
	}
	
	 

}
