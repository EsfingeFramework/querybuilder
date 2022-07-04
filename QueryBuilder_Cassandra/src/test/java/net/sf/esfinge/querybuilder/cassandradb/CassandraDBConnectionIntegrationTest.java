package net.sf.esfinge.querybuilder.cassandradb;

import com.datastax.driver.core.Session;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CassandraDBConnectionIntegrationTest {
    private Session session;

    @Test
    public void cassandraDBConnectionTest(){
        CassandraDBSessionProvider sp = ServiceLocator.getServiceImplementation(CassandraDBSessionProvider.class);
        this.session = sp.getSession();

        assertTrue(sp.getClass().getSimpleName().equals("TestCassandraSessionProvider"));
        assertNotNull(session.getCluster());
    }


}