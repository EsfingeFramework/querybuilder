package esfinge.querybuilder.core.executor;

import esfinge.querybuilder.core.methodparser.QueryInfo;

public interface QueryExecutor {

    Object executeQuery(QueryInfo info, Object[] args);

}
