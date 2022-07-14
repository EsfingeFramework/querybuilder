package net.sf.esfinge.querybuilder.cassandra.integration.dbcommunication;

import com.datastax.driver.core.Session;
import net.sf.esfinge.querybuilder.cassandra.CassandraSessionProvider;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraTestUtils;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;
import org.apache.thrift.transport.TTransportException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CassandraConnectionIntegrationTest {
    private Session session;

    @Test
    public void cassandraDBConnectionTest() throws TTransportException, IOException, InterruptedException {
        CassandraTestUtils.initCassandaUnit();

        this.session = CassandraTestUtils.getSession();

        CassandraSessionProvider client = ServiceLocator.getServiceImplementation(CassandraSessionProvider.class);
        this.session = client.getSession();

        assertTrue(client.getClass().getSimpleName().equals("TestCassandraSessionProvider"));
        assertNotNull(session.getCluster());

        session.close();
    }


}