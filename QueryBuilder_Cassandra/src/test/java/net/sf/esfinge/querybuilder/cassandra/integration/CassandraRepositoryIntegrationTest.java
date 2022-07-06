package net.sf.esfinge.querybuilder.cassandra.integration;

import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.dbutils.CassandraTestUtils;
import net.sf.esfinge.querybuilder.cassandra.testresources.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

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

	/*@Test
	public void saveTest() {


	}

	@Test
	public void deleteTest() {
	}*/
	@Test
	public void listTest() {
		List<Person> list = testQuery.list();
		assertEquals("The list should have 2 persons", 2, list.size());
	}

	@Test
	public void listTest2() {
		List<Person> list = testQuery.list();
		assertEquals("The list should have 2 persons", 2, list.size());
	}

	/*@Test
	public void getByIdTest() {
	}

	@Test
	public void queryByExampleTest() {
	}*/

}
