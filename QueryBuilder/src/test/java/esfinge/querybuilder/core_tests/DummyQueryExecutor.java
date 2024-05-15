package esfinge.querybuilder.core_tests;

import esfinge.querybuilder.core.executor.QueryExecutor;
import esfinge.querybuilder.core.methodparser.QueryInfo;
import esfinge.querybuilder.core.utils.ImplementationName;

@ImplementationName("Dummy")
public class DummyQueryExecutor implements QueryExecutor {

    @Override
    public Object executeQuery(QueryInfo info, Object[] args) {
        return null;
    }

}
