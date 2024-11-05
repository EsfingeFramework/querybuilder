package ef.qb.core_tests;

import ef.qb.core.annotation.PersistenceType;
import ef.qb.core.executor.QueryExecutor;
import ef.qb.core.methodparser.QueryInfo;

@PersistenceType("Dummy")
public class DummyQueryExecutor implements QueryExecutor {

    @Override
    public Object executeQuery(QueryInfo info, Object[] args) {
        return null;
    }

}
