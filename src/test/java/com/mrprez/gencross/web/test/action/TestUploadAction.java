package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.UploadAction;
import com.mrprez.gencross.web.bs.face.IGcrFileBS;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;

@RunWith(MockitoJUnitRunner.class)
public class TestUploadAction {

	@Mock
	private IGcrFileBS gcrFileBS;

	@Mock
	private IPersonnageBS personnageBS;

	@InjectMocks
	private UploadAction uploadAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare
		uploadAction.setPassword("string_1");
		uploadAction.setPersonnageName("string_2");
		uploadAction.setPersonnageId(3);

		// Execute
		uploadAction.execute();

		// Check
		Assert.assertEquals("failTest", uploadAction.getPersonnageName());
		Assert.assertEquals("failTest", uploadAction.getPersonnageId());
	}


	@Test
	public void testUpload() throws Exception {
		// Prepare
		uploadAction.setPassword("string_1");
		uploadAction.setPersonnageName("string_2");
		uploadAction.setPersonnageId(3);

		// Execute
		uploadAction.upload();

		// Check
		Assert.assertEquals("failTest", uploadAction.getPersonnageName());
		Assert.assertEquals("failTest", uploadAction.getPersonnageId());
	}


	@Test
	public void testIsGm() throws Exception {
		// Prepare
		uploadAction.setPassword("string_1");
		uploadAction.setPersonnageName("string_2");
		uploadAction.setPersonnageId(3);

		// Execute
		uploadAction.isGm();

		// Check
		Assert.assertEquals("failTest", uploadAction.getPersonnageName());
		Assert.assertEquals("failTest", uploadAction.getPersonnageId());
	}
}
