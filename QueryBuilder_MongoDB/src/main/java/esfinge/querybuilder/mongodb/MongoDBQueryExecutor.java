package esfinge.querybuilder.mongodb;

import esfinge.querybuilder.core.executor.QueryExecutor;
import esfinge.querybuilder.core.methodparser.QueryInfo;
import esfinge.querybuilder.core.methodparser.QueryType;
import esfinge.querybuilder.core.annotation.QueryExecutorType;
import org.mongodb.morphia.query.Query;

@QueryExecutorType("MONGODB")
public class MongoDBQueryExecutor implements QueryExecutor {

    @Override
    public Object executeQuery(QueryInfo info, Object[] args) {

        var visitor = MongoDBVisitorFactory.createQueryVisitor(info, args);
        var qr = visitor.getQueryRepresentation();
        @SuppressWarnings("rawtypes")
        var q = (Query) qr.getQuery();

        if (info.getQueryType() == QueryType.RETRIEVE_SINGLE) {
            return q.get();
        } else {
            return q.asList();
        }
    }

}
