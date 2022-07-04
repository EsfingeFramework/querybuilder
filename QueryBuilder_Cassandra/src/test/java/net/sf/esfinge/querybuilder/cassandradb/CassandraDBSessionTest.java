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
        client.connect("127.0.0.1", 9042);
        this.session = client.getSession();

        System.out.println(session.getCluster());

        assertTrue(true);
    }


}