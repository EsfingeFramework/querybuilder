package org.esfinge.querybuilder.jdbc.nullvalues.worker;

import static org.junit.Assert.*;

import java.util.List;

import org.esfinge.querybuilder.QueryBuilder;
import org.esfinge.querybuilder.jdbc.testresources.DataBaseCommunicator;
import org.esfinge.querybuilder.jdbc.testresources.Worker;
import org.junit.Before;
import org.junit.Test;

public class QueryBuilderJDBCNullValuesTestWorker {

	private TestNullValueQueriesWorker tq = QueryBuilder
			.create(TestNullValueQueriesWorker.class);;

	@Before
	public void setupDatabase() throws Exception {
		new DataBaseCommunicator()
				.initializeDatabase("/initial_db_nullvalues.xml");
	}

	@Test
	public void compareToNullQuery() {
		List<Worker> list = tq.getWorkerByName(null);
		assertEquals("The list should have 1 worker", 1, list.size());
		assertEquals("The list should have id = 4", new Integer(4), list.get(0)
				.getId());

		list = tq.getWorkerByName("Silvia");
		assertEquals("The list should have 1 worker", 1, list.size());
		assertEquals("The list should have id = 5", new Integer(5), list.get(0)
				.getId());
	}

	@Test
	public void compareToNullQueryWithOtherParams() {
		List<Worker> list = tq.getWorkerByNameAndLastName("M", null);
		assertEquals("The list should have 1 worker", 1, list.size());
		assertEquals("The list should have id = 2", new Integer(2), list.get(0)
				.getId());
	}

	@Test
	public void ignoreWhenNull() {
		List<Worker> list = tq.getWorkerByAgeGreater(0);
		assertEquals("The list should have 5 workers", 5, list.size());

		list = tq.getWorkerByAgeGreater(18);
		assertEquals("The list should have 2 workers", 2, list.size());
	}

	@Test
	public void ignoreWhenNullWithTwoParams() {
		List<Worker> list = tq
				.getWorkerByNameStartsAndLastNameStarts("M", null);
		assertEquals("The list should have 2 workers", 2, list.size());
		assertEquals("The list should have id = 2", new Integer(2), list.get(0)
				.getId());
		assertEquals("The list should have id = 3", new Integer(3), list.get(1)
				.getId());

		list = tq.getWorkerByNameStartsAndLastNameStarts(null, "S");
		list = tq.getWorkerByNameStartsAndLastNameStarts(null, "S");
		assertEquals("The list should have 2 workers", 2, list.size());
		assertEquals("The list should have id = 1", new Integer(1), list.get(0)
				.getId());
		assertEquals("The list should have id = 3", new Integer(3), list.get(1)
				.getId());

		list = tq.getWorkerByNameStartsAndLastNameStarts("M", "S");
		assertEquals("The list should have 1 worker", 1, list.size());
		assertEquals("The list should have id = 3", new Integer(3), list.get(0)
				.getId());
	}

}
