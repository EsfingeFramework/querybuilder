package net.sf.esfinge.querybuilder.cassandra.integration.dbcommunication;

import com.datastax.driver.core.Session;
import net.sf.esfinge.querybuilder.cassandra.CassandraSessionProvider;
import net.sf.esfinge.querybuilder.cassandra.integration.dbutils.CassandraTestUtils;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;
import org.apache.thrift.transport.TTransportException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CassandraConnectionIntegrationTest {

    @Test
    public void cassandraDBConnectionTest() throws TTransportException, IOException, InterruptedException {
        CassandraTestUtils.initCassandaUnit();

        CassandraSessionProvider client = ServiceLocator.getServiceImplementation(CassandraSessionProvider.class);
        Session session = client.getSession();

        assertEquals("TestCassandraSessionProvider", client.getClass().getSimpleName());
        assertNotNull(session.getCluster());

        session.close();
    }


}