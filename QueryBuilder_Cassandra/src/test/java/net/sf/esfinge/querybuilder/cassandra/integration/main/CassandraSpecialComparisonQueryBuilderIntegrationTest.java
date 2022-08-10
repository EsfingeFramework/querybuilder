package net.sf.esfinge.querybuilder.cassandra.integration.main;

import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.exceptions.MethodInvocationException;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraBasicDatabaseIntegrationTest;
import net.sf.esfinge.querybuilder.cassandra.testresources.CassandraSpecialComparisonTestQuery;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CassandraSpecialComparisonQueryBuilderIntegrationTest extends CassandraBasicDatabaseIntegrationTest {

    CassandraSpecialComparisonTestQuery testQuery = QueryBuilder.create(CassandraSpecialComparisonTestQuery.class);

    @Test
    public void queryWithNotEqualsTest() {
        List<Person> list = testQuery.getPersonByLastNameNotEquals("Silva");

        assertEquals(3, list.size());
    }

    @Test
    public void queryWithNotEqualsAndNullValueTest() {
        List<Person> list = testQuery.getPersonByLastNameNotEquals(null);

        assertEquals(5, list.size());
    }

    @Test
    public void queryWithStartsAnnotationTest() {
        List<Person> list = testQuery.getPersonByName("Ma");

        assertEquals(2, list.size());
    }

    @Test
    public void queryWithStartsAndNullValueTest() {
        assertThrows(MethodInvocationException.class, () -> testQuery.getPersonByName(null));
    }

    @Test
    public void queryWithStartsAnnotationAndNoResultsTest() {
        List<Person> list = testQuery.getPersonByName("Whatever");

        assertEquals(0, list.size());
    }

    @Test
    public void queryWithEndsTest() {
        List<Person> list = testQuery.getPersonByNameEnds("o");

        assertEquals(2, list.size());
    }

    @Test
    public void queryWithEndsAndNullValueTest() {
        assertThrows(MethodInvocationException.class, () -> testQuery.getPersonByNameEnds(null));
    }

    @Test
    public void queryWithContainsTest() {
        List<Person> list = testQuery.getPersonByNameContains("ar");

        assertEquals(2, list.size());
    }

    @Test
    public void queryWithContainsAndNullValueTest() {
        assertThrows(MethodInvocationException.class, () -> testQuery.getPersonByNameContains(null));
    }

    @Test
    public void queryWithStartsAndSimpleComparisonTest() {
        List<Person> list = testQuery.getPersonByNameStartsAndAgeGreater("Ma", 30);

        assertEquals("Marcos", list.get(0).getName());
    }

    @Test
    public void complexSpecialComparisonTest() {
        List<Person> list = testQuery.getPersonByNameStartsAndAgeGreaterAndLastNameNotEquals("Ma", 30, "Whatever");

        assertEquals("Marcos", list.get(0).getName());
    }

}
