package org.esfinge.querybuilder.mongodb;

import org.esfinge.querybuilder.executor.QueryExecutor;
import org.esfinge.querybuilder.methodparser.QueryInfo;
import org.esfinge.querybuilder.methodparser.QueryRepresentation;
import org.esfinge.querybuilder.methodparser.QueryType;
import org.esfinge.querybuilder.methodparser.QueryVisitor;

import com.google.code.morphia.query.Query;

public class MongoDBQueryExecutor implements QueryExecutor {
	
	@Override
	public Object executeQuery(QueryInfo info, Object[] args) {
		
		QueryVisitor visitor = MongoDBVisitorFactory.createQueryVisitor(info, args);
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		@SuppressWarnings("rawtypes")
		Query q = (Query) qr.getQuery();
		
		if(info.getQueryType() == QueryType.RETRIEVE_SINGLE)
			return q.get();
		else
			return q.asList();
	}

}
