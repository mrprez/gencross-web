package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.TablePointsPoolsAction;
import com.mrprez.gencross.web.bs.face.ITableBS;

@RunWith(MockitoJUnitRunner.class)
public class TestTablePointsPoolsAction {

	@Mock
	private ITableBS tableBS;

	@InjectMocks
	private TablePointsPoolsAction tablePointsPoolsAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare
		tablePointsPoolsAction.setPointPoolModification("string_1");
		tablePointsPoolsAction.setId(2);
		tablePointsPoolsAction.setPointPoolName("string_3");

		// Execute
		tablePointsPoolsAction.execute();

		// Check
		Assert.assertEquals("failTest", tablePointsPoolsAction.getPointPoolName());
		Assert.assertEquals("failTest", tablePointsPoolsAction.getTable());
		Assert.assertEquals("failTest", tablePointsPoolsAction.getPjList());
		Assert.assertEquals("failTest", tablePointsPoolsAction.getId());
		Assert.assertEquals("failTest", tablePointsPoolsAction.getPointPoolList());
		Assert.assertEquals("failTest", tablePointsPoolsAction.getPointPoolModification());
	}


	@Test
	public void testFindMinMaxPjPoints() throws Exception {
		// Prepare
		tablePointsPoolsAction.setPointPoolModification("string_1");
		tablePointsPoolsAction.setId(2);
		tablePointsPoolsAction.setPointPoolName("string_3");

		// Execute
		tablePointsPoolsAction.findMinMaxPjPoints();

		// Check
		Assert.assertEquals("failTest", tablePointsPoolsAction.getPointPoolName());
		Assert.assertEquals("failTest", tablePointsPoolsAction.getTable());
		Assert.assertEquals("failTest", tablePointsPoolsAction.getPjList());
		Assert.assertEquals("failTest", tablePointsPoolsAction.getId());
		Assert.assertEquals("failTest", tablePointsPoolsAction.getPointPoolList());
		Assert.assertEquals("failTest", tablePointsPoolsAction.getPointPoolModification());
	}


	@Test
	public void testAddPoints() throws Exception {
		// Prepare
		tablePointsPoolsAction.setPointPoolModification("string_1");
		tablePointsPoolsAction.setId(2);
		tablePointsPoolsAction.setPointPoolName("string_3");

		// Execute
		tablePointsPoolsAction.addPoints();

		// Check
		Assert.assertEquals("failTest", tablePointsPoolsAction.getPointPoolName());
		Assert.assertEquals("failTest", tablePointsPoolsAction.getTable());
		Assert.assertEquals("failTest", tablePointsPoolsAction.getPjList());
		Assert.assertEquals("failTest", tablePointsPoolsAction.getId());
		Assert.assertEquals("failTest", tablePointsPoolsAction.getPointPoolList());
		Assert.assertEquals("failTest", tablePointsPoolsAction.getPointPoolModification());
	}
}
