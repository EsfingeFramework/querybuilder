package esfinge.querybuilder.mongodb;

import dev.morphia.query.FindOptions;
import dev.morphia.query.Query;
import dev.morphia.query.Sort;
import esfinge.querybuilder.core.annotation.QueryExecutorType;
import esfinge.querybuilder.core.executor.QueryExecutor;
import esfinge.querybuilder.core.methodparser.OrderingDirection;
import esfinge.querybuilder.core.methodparser.QueryInfo;
import esfinge.querybuilder.core.methodparser.QueryOrder;
import esfinge.querybuilder.core.methodparser.QueryType;
import java.util.ArrayList;
import java.util.List;

@QueryExecutorType("MONGODB")
public class MongoDBQueryExecutor implements QueryExecutor {

    @Override
    public Object executeQuery(QueryInfo info, Object[] args) {

        var visitor = MongoDBVisitorFactory.createQueryVisitor(info, args);
        var qr = visitor.getQueryRepresentation();
        @SuppressWarnings("rawtypes")
        var q = (Query) qr.getQuery();
        var opt = getOrderBy(info.getOrder());
        if (info.getQueryType() == QueryType.RETRIEVE_SINGLE) {
            return q.iterator(opt).next();
        } else {
            return q.iterator(opt).toList();
        }
    }

    private FindOptions getOrderBy(List<QueryOrder> order) {
        List<Sort> sorts = new ArrayList<>();
        for (var qo : order) {
            if (qo.getDiretion() == OrderingDirection.DESC) {
                sorts.add(Sort.descending(qo.getProperty()));
            } else {
                sorts.add(Sort.ascending(qo.getProperty()));
            }
        }
        if (!sorts.isEmpty()) {
            return new FindOptions().sort(sorts.toArray(Sort[]::new));
        }
        return new FindOptions();
    }
}
