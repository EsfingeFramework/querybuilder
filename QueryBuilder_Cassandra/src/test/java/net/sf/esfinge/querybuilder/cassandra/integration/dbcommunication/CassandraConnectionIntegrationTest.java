package net.sf.esfinge.querybuilder.cassandra.integration.dbcommunication;

import com.datastax.driver.core.Session;
import net.sf.esfinge.querybuilder.cassandra.CassandraSessionProvider;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;
import org.apache.thrift.transport.TTransportException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class CassandraConnectionIntegrationTest {

    @Test
    public void cassandraDBConnectionTest() throws TTransportException, IOException, InterruptedException {
        // Uncomment next line to use cassandra unit db instead of a local one
        // EmbeddedCassandraServerHelper.startEmbeddedCassandra(20000L);

        CassandraSessionProvider client = ServiceLocator.getServiceImplementation(CassandraSessionProvider.class);
        Session session = client.getSession();

        assertEquals("TestCassandraSessionProvider", client.getClass().getSimpleName());
        assertNotNull(session.getCluster());
    }


}