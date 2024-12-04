package ef.qb.neo4j;

import ef.qb.core.utils.ServiceLocator;
import org.neo4j.ogm.session.Neo4jSession;

public class Neo4JEntityClassProvider {

    private static final Neo4jSession neo = ServiceLocator.getServiceImplementation(DatastoreProvider.class).getDatastore();

    public Class<?> getEntityClass(String name) {
        var classInfo = neo.metaData().classInfo(name);
        return (classInfo == null) ? null : classInfo.getUnderlyingClass();
    }

}
