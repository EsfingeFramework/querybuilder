package persistence;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import ef.qb.cassandra.CassandraSessionProvider;

public class TestCassandraSessionProvider implements CassandraSessionProvider {

    private Cluster cluster;
    private Session session;

    public void connect() {
        String node = "127.0.0.1";
        int port = 9042;
        Cluster.Builder b = Cluster.builder().addContactPoint(node);
        b.withPort(port);
        cluster = b.build();
        session = cluster.connect();
    }

    @Override
    public Session getSession() {
        if (session == null) {
            connect();
        }
        return this.session;
    }

    public void close() {
        session.close();
        cluster.close();
    }

}
