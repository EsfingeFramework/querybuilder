package net.sf.esfinge.querybuilder.jdbc.worker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import net.sf.esfinge.querybuilder.jdbc.JDBCQueryVisitor;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.conditions.NullOption;

import org.junit.Test;

public class TestDynamicQueriesJdbcQueryVisitorWorker {

	private JDBCQueryVisitor visitor = new JDBCQueryVisitor();

	@Test
	public void notDynamicQuery() {
		visitor.visitEntity("Worker");
		visitor.visitCondition("name", ComparisonType.EQUALS, NullOption.NONE);
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();

		assertFalse("Query should not be dynamic", qr.isDynamic());
		String query = qr.getQuery().toString();

		assertEquals(
				query,
				"select worker.id, worker.name, worker.lastname, worker.age from worker where worker.name = 1?");
	}

	@Test
	public void ignoreWhenNullQuery() {
		visitor.visitEntity("Worker");
		visitor.visitCondition("worker.name", ComparisonType.EQUALS,
				NullOption.IGNORE_WHEN_NULL);
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();

		assertTrue("Query should be dynamic", qr.isDynamic());

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("worker.name", null);

		String query1 = qr.getQuery(params).toString();
		assertEquals(query1,
				"select worker.id, worker.name, worker.lastName, worker.age from worker");

		params.put("worker.name", "James");

		String query2 = qr.getQuery(params).toString();
		assertEquals(
				query2,
				"select worker.id, worker.name, worker.lastName, worker.age from worker where worker.name = 'James'");
	}

	@Test
	public void compareToNullQuery() {
		visitor.visitEntity("Worker");
		visitor.visitCondition("worker.name", ComparisonType.EQUALS,
				NullOption.COMPARE_TO_NULL);
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();

		assertTrue("Query should be dynamic", qr.isDynamic());

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("worker.name", null);

		String query1 = qr.getQuery(params).toString();
		assertEquals(
				query1,
				"select worker.id, worker.name, worker.lastName, worker.age from worker where worker.name = null");

		params.put("worker.name", "James");
		String query2 = qr.getQuery(params).toString();
		assertEquals(
				query2,
				"select worker.id, worker.name, worker.lastName, worker.age from worker where worker.name = 'James'");
	}

	@Test
	public void twoCompareToNullQuery() {
		visitor.visitEntity("Worker");
		visitor.visitCondition("worker.name", ComparisonType.EQUALS,
				NullOption.COMPARE_TO_NULL);
		visitor.visitConector("and");
		visitor.visitCondition("worker.lastname", ComparisonType.EQUALS,
				NullOption.COMPARE_TO_NULL);
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("worker.name", null);
		params.put("worker.lastname", null);

		String query1 = qr.getQuery(params).toString();
		assertEquals(
				query1,
				"select worker.id, worker.name, worker.lastName, worker.age from worker where worker.name = null and worker.lastname = null");

		params.put("worker.name", "James");

		String query2 = qr.getQuery(params).toString();
		assertEquals(
				query2,
				"select worker.id, worker.name, worker.lastName, worker.age from worker where worker.name = 'James' and worker.lastname = null");

		params.put("worker.lastname", "McLoud");

		String query3 = qr.getQuery(params).toString();
		assertEquals(
				query3,
				"select worker.id, worker.name, worker.lastName, worker.age from worker where worker.name = 'James' and worker.lastname = 'McLoud'");

		params.put("worker.name", null);

		String query4 = qr.getQuery(params).toString();
		assertEquals(
				query4,
				"select worker.id, worker.name, worker.lastName, worker.age from worker where worker.name = null and worker.lastname = 'McLoud'");
	}

	@Test
	public void twoIgnoreWhenNullQuery() {
		visitor.visitEntity("Worker");
		visitor.visitCondition("worker.name", ComparisonType.EQUALS,
				NullOption.IGNORE_WHEN_NULL);
		visitor.visitConector("and");
		visitor.visitCondition("worker.lastname", ComparisonType.EQUALS,
				NullOption.IGNORE_WHEN_NULL);
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("worker.name", null);
		params.put("worker.lastname", null);

		String query1 = qr.getQuery(params).toString();
		assertEquals(query1,
				"select worker.id, worker.name, worker.lastName, worker.age from worker");

		params.put("worker.name", "James");

		String query2 = qr.getQuery(params).toString();
		assertEquals(
				query2,
				"select worker.id, worker.name, worker.lastName, worker.age from worker where worker.name = 'James'");

		params.put("worker.lastname", "McLoud");

		String query3 = qr.getQuery(params).toString();
		assertEquals(
				query3,
				"select worker.id, worker.name, worker.lastName, worker.age from worker where worker.name = 'James' and worker.lastname = 'McLoud'");

		params.put("worker.name", null);

		String query4 = qr.getQuery(params).toString();
		assertEquals(
				query4,
				"select worker.id, worker.name, worker.lastName, worker.age from worker where worker.lastname = 'McLoud'");
	}

	@Test
	public void twoIgnoreWhenNullQueryPlusOther() {
		visitor.visitEntity("Worker");
		visitor.visitCondition("worker.name", ComparisonType.EQUALS,
				NullOption.IGNORE_WHEN_NULL);
		visitor.visitConector("and");
		visitor.visitCondition("worker.age", ComparisonType.GREATER_OR_EQUALS);
		visitor.visitConector("and");
		visitor.visitCondition("worker.lastname", ComparisonType.EQUALS,
				NullOption.IGNORE_WHEN_NULL);
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("worker.name", null);
		params.put("worker.age", 18);
		params.put("worker.lastname", null);

		String query1 = qr.getQuery(params).toString();
		assertEquals(
				query1,
				"select worker.id, worker.name, worker.lastName, worker.age from worker where worker.age >= 18");

		params.put("worker.name", "James");

		String query2 = qr.getQuery(params).toString();
		assertEquals(
				query2,
				"select worker.id, worker.name, worker.lastName, worker.age from worker where worker.name = 'James' and worker.age >= 18");

		params.put("worker.lastname", "McLoud");

		String query3 = qr.getQuery(params).toString();
		assertEquals(
				query3,
				"select worker.id, worker.name, worker.lastName, worker.age from worker where worker.name = 'James' and worker.age >= 18 and worker.lastname = 'McLoud'");

		params.put("worker.name", null);

		String query4 = qr.getQuery(params).toString();
		assertEquals(
				query4,
				"select worker.id, worker.name, worker.lastName, worker.age from worker where worker.age >= 18 and worker.lastname = 'McLoud'");
	}

	@Test
	public void threeIgnoreWhenNullQuery() {
		visitor.visitEntity("Worker");
		visitor.visitCondition("worker.name", ComparisonType.EQUALS,
				NullOption.IGNORE_WHEN_NULL);
		visitor.visitConector("and");
		visitor.visitCondition("worker.age", ComparisonType.GREATER_OR_EQUALS,
				NullOption.IGNORE_WHEN_NULL);
		visitor.visitConector("and");
		visitor.visitCondition("worker.lastname", ComparisonType.EQUALS,
				NullOption.IGNORE_WHEN_NULL);
		visitor.visitEnd();
		QueryRepresentation qr = visitor.getQueryRepresentation();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("worker.name", null);
		params.put("worker.age", null);
		params.put("worker.lastname", null);

		String query1 = qr.getQuery(params).toString();
		assertEquals(query1,
				"select worker.id, worker.name, worker.lastName, worker.age from worker");

		params.put("worker.lastname", "McLoud");

		String query2 = qr.getQuery(params).toString();
		assertEquals(
				query2,
				"select worker.id, worker.name, worker.lastName, worker.age from worker where worker.lastname = 'McLoud'");

		params.put("worker.age", 18);

		String query3 = qr.getQuery(params).toString();
		assertEquals(
				query3,
				"select worker.id, worker.name, worker.lastName, worker.age from worker where worker.age >= 18 and worker.lastname = 'McLoud'");

		params.put("worker.name", "James");

		String query4 = qr.getQuery(params).toString();
		assertEquals(
				query4,
				"select worker.id, worker.name, worker.lastName, worker.age from worker where worker.name = 'James' and worker.age >= 18 and worker.lastname = 'McLoud'");
	}

}