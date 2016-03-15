package com.mrprez.gencross.web.test.action;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.PlanGameAction;
import com.mrprez.gencross.web.bo.PlannedGameBO;
import com.mrprez.gencross.web.bo.TableBO;
import com.mrprez.gencross.web.bo.UserBO;
import com.mrprez.gencross.web.bs.face.IPlanGameBS;
import com.mrprez.gencross.web.bs.face.ITableBS;
import com.mrprez.gencross.web.test.bs.AuthentificationBSTest;
import com.opensymphony.xwork2.ActionContext;

@RunWith(MockitoJUnitRunner.class)
public class PlanGameActionTest extends AbstractActionTest {

	@Mock
	private IPlanGameBS planGameBS;

	@Mock
	private ITableBS tableBS;

	@InjectMocks
	private PlanGameAction planGameAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare
		UserBO user = AuthentificationBSTest.buildUser("batman");
		ActionContext.getContext().getSession().put("user", user);
		Integer tableId = 2;
		TableBO table = new TableBO();
		Mockito.when(tableBS.getTableForGM(tableId, user)).thenReturn(table);
		
		// Execute
		planGameAction.setTableId(tableId);
		String result = planGameAction.execute();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals(table, planGameAction.getTable());
	}


	@Test
	public void testLoadPlannedGames() throws Exception {
		// Prepare
		Collection<PlannedGameBO> plannedGamesList = new ArrayList<PlannedGameBO>();
		Integer tableId = 2;
		Mockito.when(planGameBS.getPlannedGames(tableId)).thenReturn(plannedGamesList);
		HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
		ServletActionContext.setResponse(httpServletResponse);

		// Execute
		String result = planGameAction.loadPlannedGames();

		// Check
		Assert.assertEquals("jsonPlannedGames", result);
		Mockito.verify(httpServletResponse).setHeader("Cache-Control", "no-cache, no-store");
	}
}
