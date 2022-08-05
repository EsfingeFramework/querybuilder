package net.sf.esfinge.querybuilder.cassandra.integration.main;

import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraBasicDatabaseIntegrationTest;
import net.sf.esfinge.querybuilder.cassandra.testresources.CassandraSpecialComparisonTestQuery;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CassandraSpecialComparisonQueryBuilderIntegrationTest extends CassandraBasicDatabaseIntegrationTest {

    CassandraSpecialComparisonTestQuery testQuery = QueryBuilder.create(CassandraSpecialComparisonTestQuery.class);

/*
                         INSERT INTO test.person(id, name, lastname, age) VALUES (1, 'Pedro', 'Silva', 20);\n" +
                "        INSERT INTO test.person(id, name, lastname, age) VALUES (2, 'Maria', 'Ferreira', 23);\n" +
                "        INSERT INTO test.person(id, name, lastname, age) VALUES (3, 'Marcos', 'Silva', 50);\n" +
                "        INSERT INTO test.person(id, name, lastname, age) VALUES (4, 'Antonio', 'Marques', 33);\n" +
                "        INSERT INTO test.person(id, name, lastname, age) VALUES (5, 'Silvia', 'Bressan', 11);\n" +
 */

    @Test
    public void queryWithNotEqualsTest() {
        List<Person> list = testQuery.getPersonByLastNameNotEquals("Silva");

        assertEquals(3, list.size());
    }

    @Test
    public void queryWithStartsAnnotationTest() {
        List<Person> list = testQuery.getPersonByName("Ma");

        assertEquals(2, list.size());
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
    public void queryWithContainsTest() {
        List<Person> list = testQuery.getPersonByNameContains("ar");

        assertEquals(2, list.size());
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
