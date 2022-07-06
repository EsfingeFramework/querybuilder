package net.sf.esfinge.querybuilder.cassandra.integration;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.dbutils.CassandraTestUtils;
import net.sf.esfinge.querybuilder.cassandra.dbutils.TestCassandraSessionProvider;
import net.sf.esfinge.querybuilder.cassandra.testresources.CassandraTestQuery;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CassandraRepositoryIntegrationTest {

    CassandraTestUtils utils = new CassandraTestUtils();
    CassandraTestQuery testQuery;
    TestCassandraSessionProvider provider;


    @Before
    public void init() {
        utils.initDB();
        utils.populateDB();
        testQuery = QueryBuilder.create(CassandraTestQuery.class);
		provider = new TestCassandraSessionProvider();
    }

    @After
    public void clear() {
        utils.clearDB();
    }

    @Test
    public void saveTest() {
        Person expected = new Person();
        expected.setId(3);
        expected.setName("testname");
        expected.setLastName("testlastname");
        expected.setAge(30);

        testQuery.save(expected);

        MappingManager manager = new MappingManager(provider.getSession());
        Mapper<Person> mapper = manager.mapper(Person.class);

        Person actual = mapper.get(3);

		assertEquals(actual,expected);
    }

    /*@Test
    public void deleteTest() {
    }*/
    @Test
    public void listTest() {
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
