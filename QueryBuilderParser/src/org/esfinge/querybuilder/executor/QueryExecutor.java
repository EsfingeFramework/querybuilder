package org.esfinge.querybuilder.executor;

import org.esfinge.querybuilder.methodparser.QueryInfo;

public interface QueryExecutor {
	
	public Object executeQuery(QueryInfo info, Object[] args);

}
