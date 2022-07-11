package net.sf.esfinge.querybuilder.cassandra.integration.domainterms;

import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraTestUtils;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.TestCassandraSessionProvider;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;
import org.apache.thrift.transport.TTransportException;
import org.junit.*;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CassandraQueryBuilderDomainTermsTest {

	CassandraTestDomainQuery testQuery;

	TestCassandraSessionProvider provider;

	@BeforeClass
	public static void initDB() throws TTransportException, IOException, InterruptedException {
		CassandraTestUtils.initDB();
	}

	@AfterClass
	public static void dropDB() {
		CassandraTestUtils.dropDB();
	}

	@Before
	public void populateTables() {
		CassandraTestUtils.populateTables();

		testQuery = QueryBuilder.create(CassandraTestDomainQuery.class);
		provider = new TestCassandraSessionProvider();
	}

	@After
	public void cleanTables() {
		CassandraTestUtils.cleanTables();
	}

	@Test
	public void domainQueryTest(){
		List<Person> list = testQuery.getPersonTeenager();
		assertEquals("The list should have 2 persons", 2, list.size());
		assertEquals("The list should have Pedro", "Pedro", list.get(0).getName());
		assertEquals("The list should have Maria", "Maria", list.get(1).getName());
	}

	@Test
	public void domainTermWithTwoWordsTest(){
		List<Person> list = testQuery.getPersonAdultGuys();
		assertEquals("The list should have 2 person", 2, list.size());
		assertEquals("The list should have Antonio", "Antonio", list.get(0).getName());
		assertEquals("The list should have Marcos", "Marcos", list.get(1).getName());
	}

	@Test
	public void twoDomainTermsTest(){
		List<Person> list = testQuery.getPersonSilvaFamilyAdultGuys();
		assertEquals("The list should have 1 person", 1, list.size());
		assertEquals("The list should have Marcos", "Marcos", list.get(0).getName());
	}

	@Test
	public void domainTermWithParameter(){
		List<Person> list = testQuery.getPersonSilvaFamilyByAge(25);
		assertEquals("The list should have 1 person", 1, list.size());
		assertEquals("The list should have Marcos", "Marcos", list.get(0).getName());
	}


}
