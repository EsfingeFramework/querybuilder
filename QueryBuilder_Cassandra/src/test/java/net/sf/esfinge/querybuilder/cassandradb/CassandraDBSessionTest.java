package net.sf.esfinge.querybuilder.cassandradb;

import com.datastax.driver.core.Session;
import net.sf.esfinge.querybuilder.cassandradb.dbutils.TestCassandraSessionProvider;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CassandraDBSessionTest {
    private Session session;

    @Test
    public void cassandraDBConnectionTest(){
        TestCassandraSessionProvider client = new TestCassandraSessionProvider();
        this.session = client.getSession();

        assertNotNull(session.getCluster());
    }


}