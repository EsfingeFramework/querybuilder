package net.sf.esfinge.querybuilder.cassandra.integration.main;

import com.datastax.driver.core.Session;
import net.sf.esfinge.querybuilder.QueryBuilder;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraTestUtils;
import net.sf.esfinge.querybuilder.cassandra.testresources.CassandraJoinTestQuery;
import net.sf.esfinge.querybuilder.cassandra.testresources.Worker;
import org.apache.thrift.transport.TTransportException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CassandraJoinQueryBuilderIntegrationTest {

    CassandraJoinTestQuery testQuery = QueryBuilder.create(CassandraJoinTestQuery.class);

    @BeforeClass
    public static void initDB() throws TTransportException, IOException, InterruptedException {
        CassandraTestUtils.initDB();
        CassandraTestUtils.createTablesWorker();
    }

    @Before
    public void populateTables() {
        CassandraTestUtils.populateTablesWorker();
    }

    @After
    public void cleanTables() {
        CassandraTestUtils.cleanTablesWorker();
    }

    // DROPPING THE DB IN THE INTEGRATION TESTS CAN LEAD TO UNEXPECTED ERRORS
    /*@AfterClass
    public static void dropDB() {
        CassandraTestUtils.dropDB();
    }*/

    @Test
    public void queryWithOneParameterForJoinTest() {
        List<Worker> list = testQuery.getWorkerByAddressCity("Juiz de Fora");

        assertEquals(3, list.size());
    }

    @Test
    public void queryWithTwoParametersForJoinTest() {
        List<Worker> list = testQuery.getWorkerByAddressCityAndAddressState("Juiz de Fora", "MG");

        assertEquals(3, list.size());
    }

    @Test
    public void queryWithOneParameterForJoinAndOneNormalTest() {
        List<Worker> list = testQuery.getWorkerByLastNameAndAddressState("Silva", "MG");

        assertEquals("Pedro", list.get(0).getName());
    }

    @Test
    public void queryWithOneNormalParameterAndOneForJoinTest() {
        List<Worker> list = testQuery.getWorkerByAddressStateAndLastName("MG", "Silva");

        assertEquals("Pedro", list.get(0).getName());
    }

    @Test
    public void queryWithOrConnectorAndTwoParametersForJoinTest() {
        List<Worker> list = testQuery.getWorkerByAddressCityOrAddressState("Juiz de Fora", "MG");

        assertEquals(3, list.size());
    }

    @Test
    public void queryWithOrConnectorAndMixedParametersTest() {
        List<Worker> list = testQuery.getWorkerByAddressCityOrLastName("Juiz de Fora", "Silva");

        assertEquals(4, list.size());
    }

    @Test
    public void queryWithOrConnectorAndMixedParametersWithOrderingTest() {
        List<Worker> list = testQuery.getWorkerByAddressCityOrLastNameOrderById("Juiz de Fora", "Silva");

        assertEquals(4, list.size());
        assertEquals(new Integer(1), list.get(0).getId());
        assertEquals(new Integer(5), list.get(3).getId());
    }

    @Test
    public void queryWithOneParameterForJoinAndNullParameterTest() {
        Session session = CassandraTestUtils.getSession();

        String query = "INSERT INTO test.worker(id, name, lastname, age, address) VALUES (6, 'NullPerson', 'NullPerson', 30, {city: 'ACity', state: null})";

        session.execute(query);
        session.close();

        List<Worker> list = testQuery.getWorkerByAddressState(null);

        assertEquals("NullPerson", list.get(0).getName());
    }

    @Test
    public void complexQueryWithJoinTest() {
        List<Worker> list = testQuery.getWorkerByNameOrAddressCityAndAgeLesserOrLastNameOrderByName("Pedro", "SJCampos", 30, "Si");

        assertEquals("Marcos", list.get(0).getName());
        assertEquals("Maria", list.get(1).getName());
        assertEquals("Pedro", list.get(2).getName());
    }

}
