package com.mrprez.gencross.web.test.action;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mrprez.gencross.web.action.TableListAction;
import com.mrprez.gencross.web.bs.face.IPersonnageBS;
import com.mrprez.gencross.web.bs.face.ITableBS;

@RunWith(MockitoJUnitRunner.class)
public class TableListActionTest extends AbstractActionTest {

	@Mock
	private IPersonnageBS personnageBS;

	@Mock
	private ITableBS tableBS;

	@InjectMocks
	private TableListAction tableListAction;



	@Test
	public void testExecute() throws Exception {
		// Prepare
		tableListAction.setPersonnageDeletion("string_1");
		tableListAction.setTableType("string_2");
		tableListAction.setTableId(3);
		tableListAction.setTableName("string_4");

		// Execute
		String result = tableListAction.execute();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals("failTest", tableListAction.getTableId());
		Assert.assertEquals("failTest", tableListAction.getPersonnageDeletion());
		Assert.assertEquals("failTest", tableListAction.getTableTypeList());
		Assert.assertEquals("failTest", tableListAction.getTableList());
		Assert.assertEquals("failTest", tableListAction.getTableType());
		Assert.assertEquals("failTest", tableListAction.getTableName());
	}


	@Test
	public void testAddTable() throws Exception {
		// Prepare
		tableListAction.setPersonnageDeletion("string_1");
		tableListAction.setTableType("string_2");
		tableListAction.setTableId(3);
		tableListAction.setTableName("string_4");

		// Execute
		String result = tableListAction.addTable();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals("failTest", tableListAction.getTableId());
		Assert.assertEquals("failTest", tableListAction.getPersonnageDeletion());
		Assert.assertEquals("failTest", tableListAction.getTableTypeList());
		Assert.assertEquals("failTest", tableListAction.getTableList());
		Assert.assertEquals("failTest", tableListAction.getTableType());
		Assert.assertEquals("failTest", tableListAction.getTableName());
	}


	@Test
	public void testRemoveTable() throws Exception {
		// Prepare
		tableListAction.setPersonnageDeletion("string_1");
		tableListAction.setTableType("string_2");
		tableListAction.setTableId(3);
		tableListAction.setTableName("string_4");

		// Execute
		String result = tableListAction.removeTable();

		// Check
		Assert.assertEquals("input", result);
		Assert.assertEquals("failTest", tableListAction.getTableId());
		Assert.assertEquals("failTest", tableListAction.getPersonnageDeletion());
		Assert.assertEquals("failTest", tableListAction.getTableTypeList());
		Assert.assertEquals("failTest", tableListAction.getTableList());
		Assert.assertEquals("failTest", tableListAction.getTableType());
		Assert.assertEquals("failTest", tableListAction.getTableName());
	}
}
