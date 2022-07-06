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

	@Before
	public void init() {
		utils.initDB();
		utils.populatePerson();
	}

	@After
	public void clear() {
		utils.clearDB();
	}

	/*@Test
	public void save() {


	}

	@Test
	public void delete() {
	}*/
	@Test
	public void listTest() {
		CassandraTestQuery testQuery = QueryBuilder.create(CassandraTestQuery.class);
		List<Person> list = testQuery.list();
		assertEquals("The list should have 2 persons", 2, list.size());
	}

	/*@Test
	public void getById() {
	}

	@Test
	public void queryByExample() {
	}*/

}
