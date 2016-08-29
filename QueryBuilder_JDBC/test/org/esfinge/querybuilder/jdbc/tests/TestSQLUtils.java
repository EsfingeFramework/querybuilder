package org.esfinge.querybuilder.jdbc.tests;

import static org.junit.Assert.assertEquals;
import org.esfinge.querybuilder.utils.SQLUtils;
import org.junit.Test;

public class TestSQLUtils {

	private SQLUtils tsu = new SQLUtils("Person");
	private SQLUtils tsuWorker = new SQLUtils("Worker");

	@Test
	public void testEntitiesSearch() {

		String entities = tsu.getChildEntities();
		assertEquals("Should retrieve Person and Adress", "person, address",
				entities);
	}

	@Test
	public void testFieldsSearch() {

		String fields = tsu.getFieldsEntities();
		assertEquals(
				"Should retrieve Person and Adress fields",
				"person.id, person.name, person.lastName, person.age, address.id, address.city, address.state",
				fields);
	}

	@Test
	public void testListOfJoinColumns() {

		String fields = null;

		for (String str : tsu.getListOfJoinColumns()) {

			fields = str;

		}

		assertEquals("ADDRESS_ID", fields);

		assertEquals(true, tsu.isJoinColumn(fields));

	}

	@Test
	public void testJoinListExpressions() {

		String joinExpression = tsu.getJoinExpressions();
		assertEquals("Should retrieve person.address_id = address.id",
				"person.address_id = address.id", joinExpression);
	}

	@Test
	public void testColumnsToInsertCommands() {
		String columns = tsu.getColumnsToInsertComand();
		assertEquals("(id,name,lastname,age,address_id)", columns);
	}

	@Test
	public void testColumnsToUpdateCommands() {
		StringBuilder columns = new StringBuilder();

		columns.append("(");

		for (String c : tsu.getListOfColumnsToUpdateComand()) {

			columns.append(c.toLowerCase());
			columns.append(",");

		}

		columns.delete((columns.length() - 1), columns.length());
		columns.append(")");

		assertEquals("(name,lastname,age,address_id)", columns.toString());
	}

	@Test
	public void testPrimaryKey() {
		String pKey = tsu.getPrimaryKeyOfMainEntity();
		assertEquals("id", pKey);
	}

	@Test
	public void testEntitiesSearchWorker() {

		String entities = tsuWorker.getChildEntities();
		assertEquals("Should retrieve Worker", "worker", entities);
	}

	@Test
	public void testFieldsSearchWorker() {

		String fields = tsuWorker.getFieldsEntities();
		assertEquals("Should retrieve Worker fields",
				"worker.id, worker.name, worker.lastName, worker.age", fields);
	}

	@Test
	public void testJoinListExpressionsWorker() {

		String joinExpression = tsuWorker.getJoinExpressions();
		assertEquals("", joinExpression);
	}

	@Test
	public void testColumnsToInsertCommandsWorker() {
		String columns = tsuWorker.getColumnsToInsertComand();
		assertEquals("(id,name,lastname,age)", columns);
	}

	@Test
	public void testPrimaryKeyWorker() {
		String pKey = tsuWorker.getPrimaryKeyOfMainEntity();
		assertEquals("id", pKey);
	}

}