package ef.qb.core.executor;

import ef.qb.core.methodparser.QueryInfo;

public interface QueryExecutor {

    Object executeQuery(QueryInfo info, Object[] args);

}
