package net.sf.esfinge.querybuilder.cassandra.integration.main;

import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.exceptions.InvalidConnectorException;
import net.sf.esfinge.querybuilder.cassandra.exceptions.UnsupportedCassandraOperationException;
import net.sf.esfinge.querybuilder.cassandra.exceptions.WrongTypeOfExpectedResultException;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraBasicDatabaseTest;
import net.sf.esfinge.querybuilder.cassandra.testresources.CassandraSimpleTestQuery;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;
import net.sf.esfinge.querybuilder.exception.WrongParamNumberException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CassandraSimpleQueryBuilderTest extends CassandraBasicDatabaseTest {

    CassandraSimpleTestQuery testQuery = QueryBuilder.create(CassandraSimpleTestQuery.class);


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
        testQuery.getPersonByIdGreater(0);
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
        testQuery.getPersonByNameOrLastName("Pedro", "Silva");
    }

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

    @Test
    public void queryWithAllParametersTest() {
        List<Person> list = testQuery.getPersonByIdAndNameAndLastNameAndAge(1, "Pedro", "Silva", 20);
        assertEquals("The list should have 1 person", 1, list.size());
        assertEquals("The person should be Pedro", "Pedro", list.get(0).getName());
    }

    @Test(expected = WrongParamNumberException.class)
    public void queryWithLessParametersThanInNamingTest() {
        testQuery.getPersonByIdAndNameAndLastName(1, "Homer");
    }

    @Test(expected = WrongParamNumberException.class)
    public void queryWithMoreParametersThanInNamingTest() {
        testQuery.getPersonByIdAndName(1, "Homer", 48);
    }


}
