package ef.qb.neo4j;

import ef.qb.core.annotation.ServicePriority;
import java.util.Properties;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Neo4jSession;
import org.neo4j.ogm.session.SessionFactory;

@ServicePriority(0)
public class DefaultDatastoreProvider implements DatastoreProvider {

    Neo4jSession neo4j;

    public DefaultDatastoreProvider() {
        try (var input = DefaultDatastoreProvider.class.getClassLoader().getResourceAsStream("META-INF/neo4j-config.properties")) {
            var properties = new Properties();
            properties.load(input);
            var uri = properties.getProperty("neo4j.uri");
            var user = properties.getProperty("neo4j.user");
            var password = properties.getProperty("neo4j.password");
            var pollsize = Integer.parseInt(properties.getProperty("neo4j.pollsize"));
            var model_packages = (properties.getProperty("neo4j.model_packages")).split(",");

            var sessionFactory = new SessionFactory(
                    new Configuration.Builder()
                            .uri(uri)
                            .credentials(user, password)
                            .connectionPoolSize(pollsize)
                            .build(),
                    model_packages
            );
            neo4j = (Neo4jSession) sessionFactory.openSession();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Neo4jSession getDatastore() {
        return neo4j;
    }

}
