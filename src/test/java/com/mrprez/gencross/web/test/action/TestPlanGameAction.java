package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.PlanGameAction;
import com.mrprez.gencross.web.bs.face.IPlanGameBS;
import com.mrprez.gencross.web.bs.face.ITableBS;

@RunWith(MockitoJUnitRunner.class)
public class TestPlanGameAction {

	@Mock
	private IPlanGameBS planGameBS;

	@Mock
	private ITableBS tableBS;

	@InjectMocks
	private PlanGameAction planGameAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare
		planGameAction.setTableId(1);

		// Execute
		planGameAction.execute();

		// Check
		Assert.assertEquals("failTest", planGameAction.getTableId());
		Assert.assertEquals("failTest", planGameAction.getTable());
		Assert.assertEquals("failTest", planGameAction.getPlannedGamesList());
	}


	@Test
	public void testLoadPlannedGames() throws Exception {
		// Prepare
		planGameAction.setTableId(1);

		// Execute
		planGameAction.loadPlannedGames();

		// Check
		Assert.assertEquals("failTest", planGameAction.getTableId());
		Assert.assertEquals("failTest", planGameAction.getTable());
		Assert.assertEquals("failTest", planGameAction.getPlannedGamesList());
	}
}
