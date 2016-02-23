package com.mrprez.gencross.web.test.bs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.bs.AdminBS;
import com.mrprez.gencross.web.dao.face.IMailResource;

@RunWith(MockitoJUnitRunner.class)
public class AdminBSTest {
	
	@Mock
	private IMailResource mailResource;
	
	@InjectMocks
	private AdminBS adminBS;
	
	
	@Test
	public void testSendMail() throws Exception{
		// Execute
		adminBS.sendMail("Subject", "message");
		
		// Check
		Mockito.verify(mailResource).sendAdminMail("Subject", "message");
	}

}
