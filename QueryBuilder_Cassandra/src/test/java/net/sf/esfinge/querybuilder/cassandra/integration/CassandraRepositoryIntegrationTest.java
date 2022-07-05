package net.sf.esfinge.querybuilder.cassandra.integration;

import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.dbutils.CassandraTestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CassandraRepositoryIntegrationTest {

	CassandraTestUtils utils = new CassandraTestUtils();
	CassandraTestQuery testQuery;

	@Before
	public void init() {
		utils.initDB();
		utils.populatePerson();
		testQuery = QueryBuilder.create(CassandraTestQuery.class);

	}

	@After
	public void clear() {
		utils.clearDB();
	}

	@Test
	public void list() {
		//List<Person> list = tq.list();
		//assertEquals("The list should have 5 persons", 5, list.size());
	}

	/*@Test
	public void queryByExample() {
		CassandraTestQuery tq = QueryBuilder.create(CassandraTestQuery.class);
		Person p = new Person();
		p.setLastName("Silva");
		List<Person> list = tq.queryByExample(p);
		assertEquals("The list should have 2 persons", 2, list.size());
	}

	@Test
	public void queryByExampleWithTwoValues() {
		CassandraTestQuery tq = QueryBuilder.create(CassandraTestQuery.class);
		Person p = new Person();
		p.setLastName("Silva");
		p.setAge(20);
		List<Person> list = tq.queryByExample(p);
		assertEquals("The list should have 1 person", 1, list.size());
	}
		
	@Test
	public void getById() {
		CassandraTestQuery tq = QueryBuilder.create(CassandraTestQuery.class);
		Person p = tq.getById(3);
		assertEquals("It should get Marcus", "Marcos", p.getName());
	}

	@Test
	public void delete() throws Exception {
		CassandraTestQuery tq = QueryBuilder.create(CassandraTestQuery.class);
		tq.delete(3);
		new DataBaseCommunicator().compareTables("/after_delete_db.xml","PERSON");

	}

	@Test
	public void save() throws Exception {
		CassandraTestQuery tq = QueryBuilder.create(CassandraTestQuery.class);
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
		CassandraTestQuery tq = QueryBuilder.create(CassandraTestQuery.class);
		Person p = tq.getById(3);
		p.setAge(p.getAge() + 1);
		tq.save(p);
		new DataBaseCommunicator().compareTables("/after_update_db.xml",
				"PERSON");
	}*/



}
