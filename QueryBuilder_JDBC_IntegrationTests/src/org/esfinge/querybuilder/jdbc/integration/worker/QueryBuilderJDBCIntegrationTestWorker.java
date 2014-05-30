package org.esfinge.querybuilder.jdbc.integration.worker;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.esfinge.querybuilder.QueryBuilder;
import org.esfinge.querybuilder.jdbc.testresources.DataBaseCommunicator;
import org.esfinge.querybuilder.jdbc.testresources.Worker;
import org.junit.Before;
import org.junit.Test;

public class QueryBuilderJDBCIntegrationTestWorker {

	private TestQueryWorker tq = QueryBuilder.create(TestQueryWorker.class);;

	@Before
	public void setupDatabase() throws Exception {
		new DataBaseCommunicator().initializeDatabase("/initial_db.xml");
	}

	@Test
	public void simpleQuery() {
		List<Worker> list = tq.getWorker();
		assertEquals("The list should have 5 workers", 5, list.size());
	}

	@Test
	public void simpleParameterQuery() {
		Worker p = tq.getWorkerById(3);
		assertEquals("It should get Marcus", "Marcos", p.getName());
	}

	@Test
	public void listParameterQuery() {
		List<Worker> list = tq.getWorkerByLastName("Silva");
		assertEquals("The list should have 2 workers", 2, list.size());
		assertEquals("The first should be Pedro", "Pedro", list.get(0)
				.getName());
		assertEquals("The second should be Marcos", "Marcos", list.get(1)
				.getName());
	}

	@Test
	public void queryWithTwoAndParameters() {
		Worker p = tq.getWorkerByNameAndLastName("Pedro", "Silva");
		assertEquals("It should get Pedro with id=1", new Integer(1), p.getId());
	}

	@Test
	public void queryWithTwoOrParameters() {
		List<Worker> list = tq.getWorkerByNameOrLastName("Maria", "Silva");
		assertEquals("The list should have 3 workers", 3, list.size());
	}

	@Test
	public void queryWithGreaterThan() {
		List<Worker> list = tq.getWorkerByAge(40);
		assertEquals("The list should have 2 workers", 2, list.size());
	}

	@Test
	public void queryWithLesserThan() {
		List<Worker> list = tq.getWorkerByAgeLesser(40);
		assertEquals("The list should have 3 workers", 3, list.size());
	}

	@Test
	public void queryWithNotEquals() {
		List<Worker> list = tq.getWorkerByLastNameNotEquals("Silva");
		assertEquals("The list should have 3 workers", 3, list.size());
	}

	@Test
	public void queryWithStringStarted() {
		List<Worker> list = tq.getWorkerByName("M");
		assertEquals("The list should have 2 workers", 2, list.size());
	}

	@Test
	public void queryWithTwoParametersWithComparisonTypes() {
		List<Worker> list = tq.getWorkerByNameStartsAndAgeGreater("M", 30);
		assertEquals("The list should have 1 workers", 1, list.size());
	}

	@Test
	public void orderByQuery() {
		List<Worker> list = tq.getWorkerOrderByName();
		assertListOrder(list, "Antonio", "Marcos", "Maria", "Pedro", "Silvia");
	}

	@Test
	public void orderByWithParameter() {
		List<Worker> list = tq.getWorkerByAgeOrderByNameDesc(20);
		assertListOrder(list, "Silvia", "Maria", "Marcos", "Antonio");
	}

	private void assertListOrder(List<Worker> list, String... names) {
		assertEquals(list.size(), names.length);
		for (int i = 0; i < names.length; i++) {
			assertEquals(list.get(i).getName(), names[i]);
		}
	}
}
