package esfinge.querybuilder.core_tests;

import esfinge.querybuilder.core.annotation.PersistenceType;
import esfinge.querybuilder.core.executor.QueryExecutor;
import esfinge.querybuilder.core.methodparser.QueryInfo;

@PersistenceType("Dummy")
public class DummyQueryExecutor implements QueryExecutor {

    @Override
    public Object executeQuery(QueryInfo info, Object[] args) {
        return null;
    }

}
