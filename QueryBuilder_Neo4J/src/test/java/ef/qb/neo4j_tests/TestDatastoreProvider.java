package ef.qb.neo4j_tests;

import ef.qb.core.annotation.ServicePriority;
import ef.qb.neo4j.DatastoreProvider;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Neo4jSession;
import org.neo4j.ogm.session.SessionFactory;

@ServicePriority(0)
public class TestDatastoreProvider implements DatastoreProvider {

    private static final SessionFactory sessionFactory = new SessionFactory(
            new Configuration.Builder()
                    .uri("bolt://localhost:7687")
                    .credentials("neo4j", "adminadmin")
                    .connectionPoolSize(150)
                    .build(),
            "ef.qb.neo4j_tests.domain"
    );

    private static final Neo4jSession neo4j = (Neo4jSession) sessionFactory.openSession();

    @Override
    public Neo4jSession getDatastore() {
        return neo4j;
    }

}
