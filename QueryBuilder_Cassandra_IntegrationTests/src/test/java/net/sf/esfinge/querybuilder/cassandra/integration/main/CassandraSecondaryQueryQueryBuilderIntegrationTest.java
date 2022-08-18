package net.sf.esfinge.querybuilder.cassandra.integration.main;

import com.datastax.driver.core.Session;
import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.exceptions.SecondaryQueryLimitExceededException;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraBasicDatabasePersonIntegrationTest;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraTestUtils;
import net.sf.esfinge.querybuilder.cassandra.testresources.CassandraSecondaryQueryTestQuery;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CassandraSecondaryQueryQueryBuilderIntegrationTest extends CassandraBasicDatabasePersonIntegrationTest {

    CassandraSecondaryQueryTestQuery testQuery = QueryBuilder.create(CassandraSecondaryQueryTestQuery.class);

    @Test
    public void queryWithOneOrConnectorTest() {
        List<Person> list = testQuery.getPersonByNameOrLastName("Pedro", "Ferreira");

        assertEquals(2, list.size());
        assertEquals("Pedro", list.get(0).getName());
        assertEquals("Ferreira", list.get(1).getLastName());
    }

    @Test
    public void queryWithTwoOrConnectorsTest() {
        List<Person> list = testQuery.getPersonByNameOrLastNameOrAge("Pedro", "Ferreira", 50);

        assertEquals(3, list.size());
        assertEquals("Pedro", list.get(0).getName());
        assertEquals("Ferreira", list.get(1).getLastName());
        assertEquals(new Integer(50), list.get(2).getAge());
    }

    @Test
    public void queryWithTwoOrConnectorsAndDuplicateResultsTest() {
        List<Person> list = testQuery.getPersonByNameOrLastName("Pedro", "Silva");

        assertEquals(2, list.size());
        assertEquals("Pedro", list.get(0).getName());
        assertEquals("Marcos", list.get(1).getName());
    }

    @Test
    public void queryWithOrConnectorsGreaterThanSecondaryQueryLimitTest() {
        assertThrows(SecondaryQueryLimitExceededException.class, () -> testQuery.getPersonByIdOrNameOrLastNameOrAge(1, "Pedro", "Silva", 20));
    }

    @Test
    public void queryWithOrFollowedByAndConnectorTest() {
        List<Person> list = testQuery.getPersonByNameOrLastNameAndAgeGreater("Pedro", "Ferreira", 22);

        assertEquals(2, list.size());
        assertEquals("Pedro", list.get(0).getName());
        assertEquals("Maria", list.get(1).getName());
    }

    @Test
    public void queryWithAndFollowedByOrConnectorTest() {
        List<Person> list = testQuery.getPersonByNameAndLastNameOrAge("Pedro", "Ferreira", 23);

        assertEquals("Silvia", list.get(0).getName());
        assertEquals("Pedro", list.get(1).getName());
    }

    @Test
    public void queryWithOrConnectorAndOrderByClauseTest() {
        List<Person> list = testQuery.getPersonByAgeOrLastNameOrderByNameDesc(23, "Marques");

        assertEquals("Maria", list.get(0).getName());
        assertEquals("Antonio", list.get(1).getName());
    }

    @Test
    public void queryWithOrConnectorAndSpecialComparisonClauseTest() {
        List<Person> list = testQuery.getPersonByAgeOrNameStarts(25, "Ma");

        assertEquals(3, list.size());
        assertEquals("Antonio", list.get(0).getName());
        assertEquals("Marcos", list.get(1).getName());
        assertEquals("Maria", list.get(2).getName());
    }

    @Test
    public void queryWithOrConnectorAndCompareToNullTest() {
        Session session = CassandraTestUtils.getSession();

        String query = "INSERT INTO test.person(id, name, lastname, age) VALUES (6, 'NullPerson', 'NullPerson', null)";

        session.execute(query);
        session.close();

        List<Person> list = testQuery.getPersonByNameOrAge("Marcos", null);

        assertEquals("Marcos", list.get(0).getName());
        assertEquals("NullPerson", list.get(1).getName());
    }

    @Test
    public void queryWithComplexOrConnectorTest() {
        List<Person> list = testQuery.getPersonByNameStartsOrIdAndLastNameNotEqualsOrAgeOrderById("Ma", 3, "Whatever", 25);

        assertEquals(3, list.size());

        assertEquals(new Integer(2), list.get(0).getId());
        assertEquals(new Integer(4), list.get(2).getId());
    }

}
