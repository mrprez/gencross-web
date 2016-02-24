package com.mrprez.gencross.web.test.action;

import java.util.HashMap;

import org.junit.Before;

import com.opensymphony.xwork2.ActionContext;

public abstract class AbstractActionTest {
	
	@Before
	public void setUp(){
		ActionContext.setContext(new ActionContext(new HashMap<String, Object>()));
		ActionContext.getContext().setSession(new HashMap<String, Object>());
	}

}
