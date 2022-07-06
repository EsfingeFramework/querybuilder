package net.sf.esfinge.querybuilder.cassandra.integration;

import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.dbutils.CassandraTestUtils;
import net.sf.esfinge.querybuilder.cassandra.exceptions.MissingAnnotationException;
import net.sf.esfinge.querybuilder.cassandra.exceptions.MissingKeySpaceNameException;
import net.sf.esfinge.querybuilder.cassandra.testresources.*;
import net.sf.esfinge.querybuilder.cassandra.testresources.wrongconfiguration.ClassWithMissingAnnotation;
import net.sf.esfinge.querybuilder.cassandra.testresources.wrongconfiguration.ClassWithMissingKeyspaceValue;
import net.sf.esfinge.querybuilder.cassandra.testresources.wrongconfiguration.MissingAnnotationTestQuery;
import net.sf.esfinge.querybuilder.cassandra.testresources.wrongconfiguration.MissingKeySpaceTestQuery;
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

	/*@After
	public void clear() {
		utils.clearDB();
	}

	@Test
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

	@Test(expected = MissingAnnotationException.class)
	public void listWithMissingAnnotationTest() {
		MissingAnnotationTestQuery testQuery = QueryBuilder.create(MissingAnnotationTestQuery.class);

		List<ClassWithMissingAnnotation> list = testQuery.list();
	}

	@Test(expected = MissingKeySpaceNameException.class)
	public void listWithMissingKeyspaceValueTest() {
		MissingKeySpaceTestQuery testQuery = QueryBuilder.create(MissingKeySpaceTestQuery.class);

		List<ClassWithMissingKeyspaceValue> list = testQuery.list();
	}

	/*@Test
	public void getById() {
	}

	@Test
	public void queryByExample() {
	}*/

}
