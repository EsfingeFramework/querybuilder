package net.sf.esfinge.querybuilder.cassandra;

import com.datastax.driver.core.Session;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CassandraConnectionIntegrationTest {
    private Session session;

    @Test
    public void cassandraDBConnectionTest() {
        CassandraSessionProvider sp = ServiceLocator.getServiceImplementation(CassandraSessionProvider.class);
        this.session = sp.getSession();

        assertTrue(sp.getClass().getSimpleName().equals("TestCassandraSessionProvider"));
        assertNotNull(session.getCluster());
    }


}