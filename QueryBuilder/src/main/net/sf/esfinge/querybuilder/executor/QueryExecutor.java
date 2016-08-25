package net.sf.esfinge.querybuilder.executor;

import net.sf.esfinge.querybuilder.methodparser.QueryInfo;

public interface QueryExecutor {
	
	public Object executeQuery(QueryInfo info, Object[] args);

}
