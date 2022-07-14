package net.sf.esfinge.querybuilder.cassandra.integration.main;

import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.exceptions.InvalidConnectorException;
import net.sf.esfinge.querybuilder.cassandra.exceptions.UnsupportedCassandraOperationException;
import net.sf.esfinge.querybuilder.cassandra.exceptions.WrongTypeOfExpectedResultException;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraBasicDatabaseTest;
import net.sf.esfinge.querybuilder.cassandra.testresources.CassandraTestQuery;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;
import net.sf.esfinge.querybuilder.exception.WrongParamNumberException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CassandraQueryBuilderIntegrationTest extends CassandraBasicDatabaseTest {

    CassandraTestQuery testQuery = QueryBuilder.create(CassandraTestQuery.class);


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
    public void listParameterQueryTest() {
        List<Person> list = testQuery.getPersonByLastName("Silva");
        assertEquals("The list should have 2 persons", 2, list.size());
        assertEquals("The first should be Pedro", "Pedro", list.get(0).getName());
        assertEquals("The second should be Marcos", "Marcos", list.get(1).getName());
    }

    @Test
    public void queryWithTwoAndParametersTest() {
        Person p = testQuery.getPersonByNameAndLastName("Pedro", "Silva");
        assertEquals("It should get Pedro Silva", new Integer(1), p.getId());
    }

    @Test(expected = InvalidConnectorException.class)
    public void queryWithTwoOrParametersTest() {
        List<Person> list = testQuery.getPersonByNameOrLastName("Pedro", "Silva");
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
    public void queryWithGreaterThanTest() {
        List<Person> list = testQuery.getPersonByAge(40);
        assertEquals("The list should have 1 person", 1, list.size());
    }

    @Test
    public void queryWithLesserThan() {
        List<Person> list = testQuery.getPersonByAgeLesser(40);
        assertEquals("The list should have 4 persons", 4, list.size());
    }

    @Test(expected = UnsupportedCassandraOperationException.class)
    public void queryWithNotEquals() {
        List<Person> list = testQuery.getPersonByLastNameNotEquals("Whatever");
    }

    @Test(expected = UnsupportedCassandraOperationException.class)
    public void queryWithStringStarted() {
        List<Person> list = testQuery.getPersonByName("M");
    }

    @Test
    public void queryWithAllParametersTest() {
        List<Person> list = testQuery.getPersonByIdAndNameAndLastNameAndAge(1, "Pedro", "Silva", 20);
        assertEquals("The list should have 1 person", 1, list.size());
        assertEquals("The person should be Pedro", "Pedro", list.get(0).getName());
    }

    @Test(expected = WrongParamNumberException.class)
    public void queryWithLessParametersThanInNamingTest() {
        List<Person> list = testQuery.getPersonByIdAndNameAndLastName(1, "Homer");
    }

    @Test(expected = WrongParamNumberException.class)
    public void queryWithMoreParametersThanInNamingTest() {
        List<Person> list = testQuery.getPersonByIdAndName(1, "Homer", 48);
    }

    @Test
    public void orderByQueryWithOneFieldTest() {
        List<Person> list = testQuery.getPersonOrderByName();

        String[] actualNames = list.stream().map(p -> p.getName()).toArray(String[]::new);
        String[] expectedNames = {"Antonio", "Marcos", "Maria", "Pedro", "Silvia"};

        assertArrayEquals(expectedNames, actualNames);
    }

    @Test
    public void orderByQueryWithOneFieldAndParameterDescendentTest() {
        List<Person> list = testQuery.getPersonByAgeOrderByNameDesc(21);

        String[] actualNames = list.stream().map(p -> p.getName()).toArray(String[]::new);
        String[] expectedNames = {"Maria", "Marcos", "Antonio"};

        assertArrayEquals(expectedNames, actualNames);
    }

    @Test
    public void orderByQueryWithTwoFieldsTest() {
        List<Person> list = testQuery.getPersonOrderByLastNameAndName();

        String[] actualNames = list.stream().map(p -> p.getName()).toArray(String[]::new);
        String[] expectedNames = {"Silvia", "Maria", "Antonio", "Marcos", "Pedro"};

        assertArrayEquals(expectedNames, actualNames);
    }

    @Test
    public void orderByQueryWithTwoFieldsWithOrderingTest() {
        List<Person> list = testQuery.getPersonOrderByLastNameDescAndNameAsc();

        String[] actualNames = list.stream().map(p -> p.getName()).toArray(String[]::new);
        String[] expectedNames = {"Marcos", "Pedro", "Antonio", "Maria", "Silvia"};

        assertArrayEquals(expectedNames, actualNames);
    }

    @Test
    public void complexOrderByQueryTest() {
        List<Person> list = testQuery.getPersonByAgeAndLastNameOrderByAgeAndLastNameDescAndName(51, "Silva");

        String[] actualNames = list.stream().map(p -> p.getName()).toArray(String[]::new);
        String[] expectedNames = {"Pedro", "Marcos"};

        assertArrayEquals(expectedNames, actualNames);
    }

}
