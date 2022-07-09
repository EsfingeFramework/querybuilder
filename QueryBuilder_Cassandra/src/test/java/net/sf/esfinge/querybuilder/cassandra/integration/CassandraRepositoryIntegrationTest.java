package net.sf.esfinge.querybuilder.cassandra.integration;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.dbutils.CassandraTestUtils;
import net.sf.esfinge.querybuilder.cassandra.dbutils.TestCassandraSessionProvider;
import net.sf.esfinge.querybuilder.cassandra.testresources.CassandraTestQuery;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;
import org.apache.thrift.transport.TTransportException;
import org.junit.*;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CassandraRepositoryIntegrationTest {

    CassandraTestQuery testQuery;
    TestCassandraSessionProvider provider;

    @BeforeClass
    public static void initDB() throws TTransportException, IOException, InterruptedException {
        // Uncomment next line to use cassandra unit db instead of a local one
        // EmbeddedCassandraServerHelper.startEmbeddedCassandra(20000L);
        CassandraTestUtils.initDB();
    }

    @AfterClass
    public static void dropDB() {
        CassandraTestUtils.dropDB();
    }

    @Before
    public void populateTables() {
        CassandraTestUtils.populateTables();

        testQuery = QueryBuilder.create(CassandraTestQuery.class);
        provider = new TestCassandraSessionProvider();
    }

    @After
    public void cleanTables() {
        CassandraTestUtils.cleanTables();
    }

    @Test
    public void saveTest() {
        Person expected = new Person();
        expected.setId(4);
        expected.setName("testname");
        expected.setLastName("testlastname");
        expected.setAge(30);

        testQuery.save(expected);

        MappingManager manager = new MappingManager(provider.getSession());
        Mapper<Person> mapper = manager.mapper(Person.class);

        Person actual = mapper.get(4);

        assertEquals("New Person should be added to the persons", expected, actual);
    }

    @Test
    public void deleteTest() {
        testQuery.delete(2);

        MappingManager manager = new MappingManager(provider.getSession());
        Mapper<Person> mapper = manager.mapper(Person.class);

        Person actual = mapper.get(2);

        assertEquals("Should not retrieve any person", null, actual);
    }

    @Test
    public void listTest() {
        List<Person> list = testQuery.list();
        assertEquals("The list should have 3 persons", 3, list.size());
    }

    @Test
    public void getByIdTest() {
        Person expected = new Person();
        expected.setId(3);
        expected.setName("Max");
        expected.setLastName("Power");
        expected.setAge(48);

        Person actual = testQuery.getById(3);

        assertEquals("Should retrieve person Max Power", expected, actual);
    }

    @Test
    public void queryByExampleTest() {
        Person example = new Person();
        example.setName("Max");

        List<Person> list = testQuery.queryByExample(example);

        assertEquals("The list should have 1 person", 1, list.size());
    }

    @Test
    public void queryByTwoExamplesTest() {
        Person example = new Person();
        example.setName("Max");
        example.setAge(48);

        List<Person> list = testQuery.queryByExample(example);

        assertEquals("The list should have 1 person", 1, list.size());
    }

}
