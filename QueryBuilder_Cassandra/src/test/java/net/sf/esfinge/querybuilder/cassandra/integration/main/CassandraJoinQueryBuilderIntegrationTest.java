package net.sf.esfinge.querybuilder.cassandra.integration.main;

import com.datastax.driver.core.Session;
import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraTestUtils;
import net.sf.esfinge.querybuilder.cassandra.testresources.CassandraJoinTestQuery;
import net.sf.esfinge.querybuilder.cassandra.testresources.Person;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CassandraJoinQueryBuilderIntegrationTest extends CassandraBasicQueryBuilderIntegrationTest{

    CassandraJoinTestQuery testQuery = QueryBuilder.create(CassandraJoinTestQuery.class);

    @Test
    public void queryWithOneParameterForJoinTest() {
        List<Person> list = testQuery.getPersonByAddressCity("Juiz de Fora");

        assertEquals(3, list.size());
    }

    @Test
    public void queryWithTwoParametersForJoinTest() {
        List<Person> list = testQuery.getPersonByAddressCityAndAddressState("Juiz de Fora", "MG");

        assertEquals(3, list.size());
    }

    @Test
    public void queryWithOneParameterForJoinAndOneNormalTest() {
        List<Person> list = testQuery.getPersonByLastNameAndAddressState("Silva", "MG");

        assertEquals("Pedro", list.get(0).getName());
    }

    @Test
    public void queryWithOneNormalParameterAndOneForJoinTest() {
        List<Person> list = testQuery.getPersonByAddressStateAndLastName("MG", "Silva");

        assertEquals("Pedro", list.get(0).getName());
    }

    @Test
    public void queryWithOrConnectorAndTwoParametersForJoinTest() {
        List<Person> list = testQuery.getPersonByAddressCityOrAddressState("Juiz de Fora", "MG");

        assertEquals(3, list.size());
    }

    @Test
    public void queryWithOrConnectorAndMixedParametersTest() {
        List<Person> list = testQuery.getPersonByAddressCityOrLastName("Juiz de Fora", "Silva");

        assertEquals(4, list.size());
    }

    @Test
    public void queryWithOrConnectorAndMixedParametersWithOrderingTest() {
        List<Person> list = testQuery.getPersonByAddressCityOrLastNameOrderById("Juiz de Fora", "Silva");

        assertEquals(4, list.size());
        assertEquals(new Integer(1), list.get(0).getId());
        assertEquals(new Integer(5), list.get(3).getId());
    }

    @Test
    public void queryWithOneParameterForJoinAndNullParameterTest() {
        Session session = CassandraTestUtils.getSession();

        String query = "INSERT INTO test.Person(id, name, lastname, age, address) VALUES (6, 'NullPerson', 'NullPerson', 30, {city: 'ACity', state: null})";

        session.execute(query);
        session.close();

        List<Person> list = testQuery.getPersonByAddressState(null);

        assertEquals("NullPerson", list.get(0).getName());
    }

    @Test
    public void complexQueryWithJoinTest() {
        List<Person> list = testQuery.getPersonByNameOrAddressCityAndAgeLesserOrLastNameOrderByName("Pedro", "SJCampos", 30, "Si");

        assertEquals("Marcos", list.get(0).getName());
        assertEquals("Maria", list.get(1).getName());
        assertEquals("Pedro", list.get(2).getName());
    }

}
