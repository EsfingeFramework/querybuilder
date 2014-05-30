package org.esfinge.querybuilder.jdbc.integration;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.esfinge.querybuilder.QueryBuilder;
import org.esfinge.querybuilder.jdbc.testresources.DataBaseCommunicator;
import org.esfinge.querybuilder.jdbc.testresources.Person;
import org.junit.Before;
import org.junit.Test;

public class RepositoryJDBCIntegrationTest {

	@Before
	public void setupDatabase() throws Exception {
		new DataBaseCommunicator().initializeDatabase("/initial_db.xml");
	}

	@Test
	public void list() {
		TestQuery tq = QueryBuilder.create(TestQuery.class);
		List<Person> list = tq.list();
		assertEquals("The list should have 5 persons", 5, list.size());
	}

	@Test
	public void queryByExample() {
		TestQuery tq = QueryBuilder.create(TestQuery.class);
		Person p = new Person();
		p.setLastName("Silva");
		List<Person> list = tq.queryByExample(p);
		assertEquals("The list should have 2 persons", 2, list.size());
	}

	@Test
	public void queryByExampleWithTwoValues() {
		TestQuery tq = QueryBuilder.create(TestQuery.class);
		Person p = new Person();
		p.setLastName("Silva");
		p.setAge(20);
		List<Person> list = tq.queryByExample(p);
		assertEquals("The list should have 1 person", 1, list.size());
	}
		
	@Test
	public void getById() {
		TestQuery tq = QueryBuilder.create(TestQuery.class);
		Person p = tq.getById(3);
		assertEquals("It should get Marcus", "Marcos", p.getName());
	}

	@Test
	public void delete() throws Exception {
		TestQuery tq = QueryBuilder.create(TestQuery.class);
		tq.delete(3);
		new DataBaseCommunicator().compareTables("/after_delete_db.xml","PERSON");

	}

	@Test
	public void save() throws Exception {
		TestQuery tq = QueryBuilder.create(TestQuery.class);
		Person p = new Person();
		p.setName("Beatriz");
		p.setLastName("Tosetti");
		p.setAge(0);
		p.setId(6);
		tq.save(p);
		new DataBaseCommunicator().compareTables("/after_insert_db.xml",
				"PERSON");
	}

	@Test
	public void update() throws Exception {
		TestQuery tq = QueryBuilder.create(TestQuery.class);
		Person p = tq.getById(3);
		p.setAge(p.getAge() + 1);
		tq.save(p);
		new DataBaseCommunicator().compareTables("/after_update_db.xml",
				"PERSON");
	}

}
