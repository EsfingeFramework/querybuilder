package net.sf.esfinge.querybuilder.cassandra.integration;

import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.dbutils.CassandraTestUtils;
import net.sf.esfinge.querybuilder.cassandra.dbutils.TestCassandraSessionProvider;
import net.sf.esfinge.querybuilder.cassandra.exceptions.InvalidConnectorException;
import net.sf.esfinge.querybuilder.cassandra.exceptions.UnsupportedCassandraOperationException;
import net.sf.esfinge.querybuilder.cassandra.exceptions.WrongTypeOfExpectedResultException;
import net.sf.esfinge.querybuilder.cassandra.testresources.CassandraTestQuery;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;
import net.sf.esfinge.querybuilder.exception.WrongParamNumberException;
import org.apache.thrift.transport.TTransportException;
import org.junit.*;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class CassandraQueryBuilderIntegrationTest {

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
    public void selectAllQueryTest() {
        List<Person> list = testQuery.getPerson();
        assertEquals("The list should have 5 persons", 5, list.size());
    }

    @Test
    public void queryWithSingleParameterTest() {
        Person p = testQuery.getPersonById(1);
        assertEquals("It should get Pedro", "Pedro", p.getName());
    }

    @Test
    public void queryWithSingleParameterGreaterThanTest() {
        Person p = testQuery.getPersonByIdGreater(4);
        assertEquals("It should get Silvia", "Silvia", p.getName());
    }

    @Test(expected = WrongTypeOfExpectedResultException.class)
    public void queryWithWrongTypeOfExpectedResultTest() {
        Person p = testQuery.getPersonByIdGreater(0);
    }

    @Test
    public void queryWithSingleParameterWithNoExpectedResultTest() {
        Person p = testQuery.getPersonById(6);
        assertNull("It should not retrieve any person", p);
    }

    @Test
    public void listParameterQueryTest(){
        List<Person> list = testQuery.getPersonByLastName("Silva");
        assertEquals("The list should have 2 persons", 2, list.size());
        assertEquals("The first should be Pedro", "Pedro", list.get(0).getName());
        assertEquals("The second should be Marcos", "Marcos", list.get(1).getName());
    }

    @Test
    public void queryWithTwoAndParametersTest(){
        Person p = testQuery.getPersonByNameAndLastName("Pedro","Silva");
        assertEquals("It should get Pedro Silva", new Integer(1), p.getId());
    }

    @Test(expected = InvalidConnectorException.class)
    public void queryWithTwoOrParametersTest(){
        List<Person> list = testQuery.getPersonByNameOrLastName("Pedro","Silva");
    }

    /*@Test
     TODO: IMPLEMENT JOINS WITH OTHER TABLES
    public void queryWithOtherTable(){
        List<Person> list = tq.getPersonByAddressCity("Juiz de Fora");
        assertEquals("The list should have 2 persons", 2, list.size());
        assertEquals("The first should be Antonio", "Antonio", list.get(0).getName());
        assertEquals("The second should be Silvia", "Silvia", list.get(1).getName());
    }

    @Test
    public void compositeQueryWithOtherTable(){
        List<Person> list = tq.getPersonByLastNameAndAddressState("Silva","SP");
        assertEquals("The list should have 2 persons", 2, list.size());
        assertEquals("The first should be Pedro", "Pedro", list.get(0).getName());
        assertEquals("Marcos", list.get(1).getName());
    }*/

    @Test
    public void queryWithGreaterThanTest(){
        List<Person> list = testQuery.getPersonByAge(40);
        assertEquals("The list should have 1 person", 1, list.size());
    }

    @Test
    public void queryWithLesserThan(){
        List<Person> list = testQuery.getPersonByAgeLesser(40);
        assertEquals("The list should have 4 persons", 4, list.size());
    }

    @Test(expected = UnsupportedCassandraOperationException.class)
    public void queryWithNotEquals(){
        List<Person> list = testQuery.getPersonByLastNameNotEquals("Whatever");
    }

    @Test(expected = UnsupportedCassandraOperationException.class)
    public void queryWithStringStarted(){
        List<Person> list = testQuery.getPersonByName("M");
    }

    @Test
    public void queryWithAllParametersTest(){
        List<Person> list = testQuery.getPersonByIdAndNameAndLastNameAndAge(1,"Pedro","Silva",20);
        assertEquals("The list should have 1 person", 1, list.size());
        assertEquals("The person should be Pedro", "Pedro", list.get(0).getName());
    }

    @Test(expected = WrongParamNumberException.class)
    public void queryWithLessParametersThanInNamingTest(){
        List<Person> list = testQuery.getPersonByIdAndNameAndLastName(1,"Homer");
    }

    @Test(expected = WrongParamNumberException.class)
    public void queryWithMoreParametersThanInNamingTest(){
        List<Person> list = testQuery.getPersonByIdAndName(1,"Homer",48);
    }

    @Test
    public void orderByQueryWithOneFieldTest(){
        List<Person> list = testQuery.getPersonOrderByName();

        String[] actualNames = list.stream().map(p -> p.getName()).toArray(String[]::new);
        String[] expectedNames = {"Bart","Homer","Max"};

        //assertArrayEquals(expectedNames, actualNames);
    }

    @Test
    public void orderByQueryWithOneFieldAndParameterDescendentTest(){
        List<Person> list = testQuery.getPersonByAgeOrderByNameDesc(1);

        String[] actualNames = list.stream().map(p -> p.getName()).toArray(String[]::new);
        String[] expectedNames = {"Bart","Homer","Max"};


        //assertArrayEquals(expectedNames, actualNames);
    }

    @Test
    public void orderByQueryWithTwoFieldsTest(){
        List<Person> list = testQuery.getPersonOrderByNameAndLastName();

        String[] actualNames = list.stream().map(p -> p.getName()).toArray(String[]::new);
        String[] expectedNames = {"Bart","Homer","Max"};


        //assertArrayEquals(expectedNames, actualNames);
    }

    @Test
    public void orderByQueryWithTwoFieldsWithOrderingTest(){
        List<Person> list = testQuery.getPersonOrderByNameDescAndLastNameAsc();

        String[] actualNames = list.stream().map(p -> p.getName()).toArray(String[]::new);
        String[] expectedNames = {"Bart","Homer","Max"};


        //assertArrayEquals(expectedNames, actualNames);
    }

    @Test
    public void complexOrderByQueryTest(){
        List<Person> list = testQuery.getPersonByAgeAndLastNameOrderByAgeAndLastNameDescAndName(1,"Simpson");

        String[] actualNames = list.stream().map(p -> p.getName()).toArray(String[]::new);
        String[] expectedNames = {"Bart","Homer","Max"};


        //assertArrayEquals(expectedNames, actualNames);
    }

}
