package net.sf.esfinge.querybuilder.cassandra.integration.nullvalues;

import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.exceptions.UnsupportedCassandraOperationException;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraBasicDatabaseTest;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraTestUtils;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.TestCassandraSessionProvider;
import net.sf.esfinge.querybuilder.cassandra.testresources.CassandraTestQuery;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;
import org.apache.thrift.transport.TTransportException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CassandraQueryBuilderNullValuesTest extends CassandraBasicDatabaseTest {
	
	private CassandraTestNullValueQueries testQuery = QueryBuilder.create(CassandraTestNullValueQueries.class);;

	@Test(expected = UnsupportedCassandraOperationException.class)
	public void compareToNullQueryTest(){
		List<Person> list = testQuery.getPersonByName(null);
	}

	@Test
	public void ignoreWhenNullQueryTest(){
		List<Person> list = testQuery.getPersonByAgeGreater(null);
		assertEquals("The list should have 5 persons", 5, list.size());

		list = testQuery.getPersonByAgeGreater(18);
		assertEquals("The list should have 4 persons", 4, list.size());
	}
	
	/*@Test
	public void ignoreWhenNullWithTwoParams(){
		List<Person> list = testQuery.getPersonByNameAndLastName("M", null);
		assertEquals("The list should have 2 persons", 2, list.size());
		assertEquals("The list should have id = 2", new Integer(2), list.get(0).getId());
		assertEquals("The list should have id = 3", new Integer(3), list.get(1).getId());
		
		list = testQuery.getPersonByNameAndLastName(null, "S");
		assertEquals("The list should have 2 persons", 2, list.size());
		assertEquals("The list should have id = 1", new Integer(1), list.get(0).getId());
		assertEquals("The list should have id = 3", new Integer(3), list.get(1).getId());
		
		list = testQuery.getPersonByNameAndLastName("M", "S");
		assertEquals("The list should have 1 person", 1, list.size());
		assertEquals("The list should have id = 3", new Integer(3), list.get(0).getId());
	}*/

}
