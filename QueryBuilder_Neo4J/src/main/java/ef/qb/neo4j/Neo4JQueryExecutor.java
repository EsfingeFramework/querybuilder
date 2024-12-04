package ef.qb.neo4j;

import ef.qb.core.annotation.QueryExecutorType;
import ef.qb.core.annotation.ServicePriority;
import ef.qb.core.executor.QueryExecutor;
import ef.qb.core.methodparser.QueryInfo;
import ef.qb.core.methodparser.QueryType;
import static ef.qb.core.utils.PersistenceTypeConstants.NEO4J;
import ef.qb.core.utils.ServiceLocator;
import org.neo4j.ogm.session.Neo4jSession;

@ServicePriority(1)
@QueryExecutorType(NEO4J)
public class Neo4JQueryExecutor implements QueryExecutor {

    private static final Neo4jSession neo4j = ServiceLocator.getServiceImplementation(DatastoreProvider.class).getDatastore();

    @Override
    public Object executeQuery(QueryInfo info, Object[] args) {

        var visitor = Neo4JVisitorFactory.createQueryVisitor(info, args);
        var queryRepresentation = visitor.getQueryRepresentation();
        var statments = (Neo4JQueryParameters) queryRepresentation.getQuery();
        var classInfo = neo4j.metaData().classInfo(statments.getLabel());
        var entityClass = classInfo.getUnderlyingClass();
        var collection = neo4j.loadAll(entityClass, statments.getFilters(), statments.getSortOrder());
        if (info.getQueryType() == QueryType.RETRIEVE_SINGLE) {
            if (!collection.isEmpty()) {
                return collection.iterator().next();
            } else {
                return null;
            }
        } else {
            return collection;
        }
    }

}
