package org.esfinge.querybuilder;

import org.esfinge.querybuilder.executor.QueryExecutor;
import org.esfinge.querybuilder.methodparser.QueryInfo;

public class DummyQueryExecutor implements QueryExecutor{

	@Override
	public Object executeQuery(QueryInfo info, Object[] args) {
		return null;
	}

}
