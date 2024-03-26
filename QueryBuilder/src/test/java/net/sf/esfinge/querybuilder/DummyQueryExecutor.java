package net.sf.esfinge.querybuilder;

import net.sf.esfinge.querybuilder.executor.QueryExecutor;
import net.sf.esfinge.querybuilder.methodparser.QueryInfo;

public class DummyQueryExecutor implements QueryExecutor {

    @Override
    public Object executeQuery(QueryInfo info, Object[] args) {
        return null;
    }

}
