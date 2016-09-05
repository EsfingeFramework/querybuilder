package net.sf.esfinge.querybuilder.jdbc.integration.worker;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.jdbc.testresources.Worker;
import net.sf.esfinge.querybuilder.jdbc.util.DataBaseCommunicator;

public class RepositoryJDBCIntegrationTestWorker {

	@Before
	public void setupDatabase() throws Exception {
		new DataBaseCommunicator().initializeDatabase("/initial_db.xml");
	}

	@Test
	public void list() {
		TestQueryWorker tq = QueryBuilder.create(TestQueryWorker.class);
		List<Worker> list = tq.list();
		assertEquals("The list should have 5 workers", 5, list.size());
	}

	@Test
	public void queryByExample() {
		TestQueryWorker tq = QueryBuilder.create(TestQueryWorker.class);
		Worker p = new Worker();
		p.setLastName("Silva");
		List<Worker> list = tq.queryByExample(p);
		assertEquals("The list should have 2 workers", 2, list.size());
	}

	@Test
	public void queryByExampleWithTwoValues() {
		TestQueryWorker tq = QueryBuilder.create(TestQueryWorker.class);
		Worker p = new Worker();
		p.setLastName("Silva");
		p.setAge(20);
		List<Worker> list = tq.queryByExample(p);
		assertEquals("The list should have 1 worker", 1, list.size());
	}

	@Test
	public void getById() {
		TestQueryWorker tq = QueryBuilder.create(TestQueryWorker.class);
		Worker p = tq.getById(3);
		assertEquals("It should get Marcus", "Marcos", p.getName());
	}

	@Test
	public void delete() throws Exception {
		TestQueryWorker tq = QueryBuilder.create(TestQueryWorker.class);
		tq.delete(3);
		new DataBaseCommunicator().compareTables("/after_delete_db.xml",
				"WORKER");

	}

	@Test
	public void save() throws Exception {
		TestQueryWorker tq = QueryBuilder.create(TestQueryWorker.class);
		Worker p = new Worker();
		p.setName("Beatriz");
		p.setLastName("Tosetti");
		p.setAge(0);
		p.setId(6);
		tq.save(p);
		new DataBaseCommunicator().compareTables("/after_insert_db.xml",
				"WORKER");
	}

	@Test
	public void update() throws Exception {
		TestQueryWorker tq = QueryBuilder.create(TestQueryWorker.class);
		Worker p = tq.getById(3);
		p.setAge(p.getAge() + 1);
		tq.save(p);
		new DataBaseCommunicator().compareTables("/after_update_db.xml",
				"WORKER");
	}

}
