package net.sf.esfinge.querybuilder.cassandra.integration;

import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.dbutils.CassandraTestUtils;
import net.sf.esfinge.querybuilder.cassandra.dbutils.TestCassandraSessionProvider;
import net.sf.esfinge.querybuilder.cassandra.exceptions.WrongTypeOfExpectedResultException;
import net.sf.esfinge.querybuilder.cassandra.testresources.CassandraTestQuery;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;
import org.apache.thrift.transport.TTransportException;
import org.junit.*;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

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

    /* METHODS THAT CAN BE TESTED
        p = testQuery.getPersonByNameAndLastName("Max","Power");
        list = testQuery.getPersonByLastName("Ciccio");
        list = testQuery.getPersonByAge(30);
        list = testQuery.getPersonByAgeLesser(30);
        list = testQuery.getPersonOrderByName();
        list = testQuery.getPersonByAgeOrderByNameDesc(30);
        list = testQuery.getPersonByAgeAndLastNameOrderByNameDesc(30,"Max");
     */

    @Test
    public void selectAllQueryTest() {
        List<Person> list = testQuery.getPerson();
        assertEquals("The list should have 3 persons", 3, list.size());
    }

    @Test
    public void queryWithSingleParameterTest() {
        Person p = testQuery.getPersonById(1);
        assertEquals("It should get Homer", "Homer", p.getName());
    }

    @Test
    public void queryWithSingleParameterGreaterThanTest() {
        Person p = testQuery.getPersonByIdGreater(2);
        assertEquals("It should get Max", "Max", p.getName());
    }

    @Test(expected = WrongTypeOfExpectedResultException.class)
    public void queryWithWrongTypeOfExpectedResultTest() {
        Person p = testQuery.getPersonByIdGreater(0);
    }

    @Test
    public void queryWithSingleParameterWithNoExpectedResultTest() {
        Person p = testQuery.getPersonById(5);
        assertEquals("It should not retrieve any person", null, p);
    }

    @Test
    public void listParameterQuery(){
        List<Person> list = testQuery.getPersonByLastName("Simpson");
        assertEquals("The list should have 2 persons", 2, list.size());
        assertEquals("The first should be Max", "Homer", list.get(0).getName());
        assertEquals("The second should be Homer", "Bart", list.get(1).getName());
    }

    /*@Test
    public void queryWithTwoAndParameters(){
        Person p = tq.getPersonByNameAndLastName("Pedro","Silva");
        assertEquals("It should get Pedro with id=1", new Integer(1), p.getId());
    }

    @Test
    public void queryWithTwoOrParameters(){
        List<Person> list = tq.getPersonByNameOrLastName("Maria","Silva");
        assertEquals("The list should have 3 persons", 3, list.size());
    }

    @Test
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
    }

    @Test
    public void queryWithGreaterThan(){
        List<Person> list = tq.getPersonByAge(40);
        assertEquals("The list should have 2 persons", 2, list.size());
    }

    @Test
    public void queryWithLesserThan(){
        List<Person> list = tq.getPersonByAgeLesser(40);
        assertEquals("The list should have 3 persons", 3, list.size());
    }

    @Test
    public void queryWithNotEquals(){
        List<Person> list = tq.getPersonByLastNameNotEquals("Silva");
        assertEquals("The list should have 3 persons", 3, list.size());
    }

    @Test
    public void queryWithStringStarted(){
        List<Person> list = tq.getPersonByName("M");
        assertEquals("The list should have 2 persons", 2, list.size());
    }

    @Test
    public void queryWithTwoParametersWithComparisonTypes(){
        List<Person> list = tq. getPersonByNameStartsAndAgeGreater("M",30);
        assertEquals("The list should have 1 persons", 1, list.size());
    }

    @Test
    public void orderByQuery(){
        List<Person> list = tq. getPersonOrderByName();
        assertListOrder(list, "Antonio", "Marcos", "Maria", "Pedro", "Silvia");
    }

    @Test
    public void orderByWithParameter(){
        List<Person> list = tq. getPersonByAgeOrderByNameDesc(20);
        assertListOrder(list,"Silvia", "Maria", "Marcos", "Antonio");
    }

    private void assertListOrder(List<Person> list, String... names ){
        assertEquals(list.size(), names.length);
        for(int i=0; i<names.length; i++){
            assertEquals(list.get(i).getName(), names[i]);
        }
    }*/
}
