package org.esfinge.querybuilder.jdbc.domainterms.worker;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.esfinge.querybuilder.QueryBuilder;
import org.esfinge.querybuilder.jdbc.testresources.DataBaseCommunicator;
import org.esfinge.querybuilder.jdbc.testresources.Worker;
import org.junit.Before;
import org.junit.Test;

public class QueryBuilderJDBCDomainTermsTestWorker {

	private TestDomainQueryWorker tq = QueryBuilder.create(TestDomainQueryWorker.class);

	@Before
	public void setupDatabase() throws Exception {
		new DataBaseCommunicator().initializeDatabase("/initial_db_domain.xml");
	}

	@Test
	public void domainUnitQuery() {
		List<Worker> list = tq.getWorkerById(new Integer(1));
		assertEquals("The list should have 1 workers", 1, list.size());
		assertEquals("The list should have Pedro", "Pedro", list.get(0)
				.getName().trim());
	}

	@Test
	public void domainQuery() {
		List<Worker> list = tq.getWorkerTeenager();
		assertEquals("The list should have 2 workers", 2, list.size());
		assertEquals("The list should have Pedro", "Pedro", list.get(0)
				.getName().trim());
		assertEquals("The list should have Maria", "Maria", list.get(1)
				.getName().trim());
	}

	@Test
	public void domainTermWithTwoWords() {
		List<Worker> list = tq.getWorkerOldGuys();
		assertEquals("The list should have 2 person", 2, list.size());
		assertEquals("The list should have Marcos", "Marcos", list.get(0)
				.getName().trim());
		assertEquals("The list should have Silvia", "Silvia", list.get(1)
				.getName().trim());
	}

}
