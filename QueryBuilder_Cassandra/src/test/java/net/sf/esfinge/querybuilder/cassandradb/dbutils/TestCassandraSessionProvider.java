package net.sf.esfinge.querybuilder.cassandradb.dbutils;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import net.sf.esfinge.querybuilder.cassandradb.SessionProvider;

public class TestCassandraSessionProvider implements SessionProvider {

    private Cluster cluster;

    private Session session;

    public void connect(final String node, final Integer port) {

        Cluster.Builder b = Cluster.builder().addContactPoint(node);

        if (port != null) {
            b.withPort(port);
        }
        cluster = b.build();

        session = cluster.connect();
    }

    @Override
    public Session getSession() {
        return this.session;
    }

    public void close() {
        session.close();
        cluster.close();
    }

}
