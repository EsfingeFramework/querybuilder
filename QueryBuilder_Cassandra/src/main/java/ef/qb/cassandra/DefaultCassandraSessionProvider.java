package ef.qb.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import ef.qb.core.annotation.ServicePriority;
import java.util.Properties;

@ServicePriority(0)
public class DefaultCassandraSessionProvider implements CassandraSessionProvider {

    private Cluster cluster;
    private Session session;

    public void connect() {

        try (var input = DefaultCassandraSessionProvider.class.getClassLoader().getResourceAsStream("META-INF/cassandra-config.properties")) {
            var properties = new Properties();
            properties.load(input);
            var node = properties.getProperty("cassandra.node");
            var port = Integer.parseInt(properties.getProperty("cassandra.port"));
            Cluster.Builder b = Cluster.builder().addContactPoint(node);
            b.withPort(port);
            cluster = b.build();
            session = cluster.connect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
