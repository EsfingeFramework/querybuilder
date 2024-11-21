package ef.qb.mongodb;

import dev.morphia.query.FindOptions;
import dev.morphia.query.Query;
import dev.morphia.query.Sort;
import ef.qb.core.annotation.QueryExecutorType;
import ef.qb.core.executor.QueryExecutor;
import ef.qb.core.methodparser.OrderingDirection;
import ef.qb.core.methodparser.QueryInfo;
import ef.qb.core.methodparser.QueryOrder;
import ef.qb.core.methodparser.QueryType;
import static ef.qb.core.utils.PersistenceTypeConstants.MONGODB;
import java.util.ArrayList;
import java.util.List;

@QueryExecutorType(MONGODB)
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
