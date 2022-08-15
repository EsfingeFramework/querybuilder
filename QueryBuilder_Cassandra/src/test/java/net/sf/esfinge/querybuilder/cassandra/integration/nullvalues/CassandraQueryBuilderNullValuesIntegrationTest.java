package net.sf.esfinge.querybuilder.cassandra.integration.nullvalues;

import com.datastax.driver.core.Session;
import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraBasicDatabaseIntegrationTest;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraTestUtils;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CassandraQueryBuilderNullValuesIntegrationTest extends CassandraBasicDatabaseIntegrationTest {

    private final CassandraNullValueTestQueries testQuery = QueryBuilder.create(CassandraNullValueTestQueries.class);

    @Test
    public void compareToNullWithOneNullQueryTest() {
        Session session = CassandraTestUtils.getSession();

        String query = "INSERT INTO test.person(id, name, lastname, age) VALUES (6, null, 'NullPerson', 20)";

        session.execute(query);
        session.close();

        List<Person> list = testQuery.getPersonByName(null);

        assertEquals("NullPerson", list.get(0).getLastName());
    }

    @Test
    public void compareToNullWithOneNullAndNonNullParameterQueryTest() {
        Session session = CassandraTestUtils.getSession();

        String query = "INSERT INTO test.person(id, name, lastname, age) VALUES (6, null, 'NullPerson', 20)";

        session.execute(query);
        session.close();

        List<Person> list = testQuery.getPersonByName("Silvia");

        assertEquals("Silvia", list.get(0).getName());
    }

    @Test
    public void compareToNullWithTwoNullsQueryTest() {
        Session session = CassandraTestUtils.getSession();

        String query = "INSERT INTO test.person(id, name, lastname, age) VALUES (6, null, null, 20)";

        session.execute(query);
        session.close();

        List<Person> list = testQuery.getPersonByLastNameAndName(null, null);

        assertEquals(new Integer(20), list.get(0).getAge());
    }

    @Test
    public void compareToNullWithFirstNullAnSecondNonNullQueryTest() {
        Session session = CassandraTestUtils.getSession();

        String query = "INSERT INTO test.person(id, name, lastname, age) VALUES (6, 'NonNullName', null, 20)";

        session.execute(query);
        session.close();

        List<Person> list = testQuery.getPersonByLastNameAndName(null, "NonNullName");

        assertEquals(new Integer(20), list.get(0).getAge());
    }

    @Test
    public void compareToNullWithSecondNullAnFirstNonNullQueryTest() {
        Session session = CassandraTestUtils.getSession();

        String query = "INSERT INTO test.person(id, name, lastname, age) VALUES (6, null, 'NonNullLastName', 20)";

        session.execute(query);
        session.close();

        List<Person> list = testQuery.getPersonByLastNameAndName("NonNullLastName", null);

        assertEquals(new Integer(20), list.get(0).getAge());
    }

    @Test
    public void compareToNullWithTwoParametersOneNullQueryTest() {
        Session session = CassandraTestUtils.getSession();

        String query = "INSERT INTO test.person(id, name, lastname, age) VALUES (6, 'NullPerson', null, 20)";

        session.execute(query);
        session.close();

        List<Person> list = testQuery.getPersonByLastNameAndAge(null, 20);

        assertEquals(new Integer(20), list.get(0).getAge());
    }

    @Test
    public void ignoreWhenNullQueryTest() {
        List<Person> list = testQuery.getPersonByAgeGreater(null);
        assertEquals("The list should have 5 persons", 5, list.size());

        list = testQuery.getPersonByAgeGreater(18);
        assertEquals("The list should have 4 persons", 4, list.size());
    }

    @Test
    public void ignoreWhenNullWithTwoParamsOneNullTest() {
        List<Person> list = testQuery.getPersonByNameAndLastName("Marcos", null);
        assertEquals("The list should have 1 person", 1, list.size());
        assertEquals("The person should be Marcos", "Marcos", list.get(0).getName());

        list = testQuery.getPersonByNameAndLastName(null, "Silva");
        assertEquals("The list should have 2 persons", 2, list.size());
        assertEquals("The first person should be Pedro", "Pedro", list.get(0).getName());
        assertEquals("The first person should be Marcos", "Marcos", list.get(1).getName());
    }

    @Test
    public void ignoreWhenNullWithTwoParamsNotNullTest() {
        List<Person> list = testQuery.getPersonByNameAndLastName("Marcos", "Silva");
        assertEquals("The list should have 1 person", 1, list.size());
        assertEquals("The first person should be Marcos", "Marcos", list.get(0).getName());
    }

    @Test
    public void ignoreWhenNullWithTwoParamsBothNullTest() {
        List<Person> list = testQuery.getPersonByNameAndLastName(null, null);
        assertEquals("The list should have 5 persons", 5, list.size());
    }


}
