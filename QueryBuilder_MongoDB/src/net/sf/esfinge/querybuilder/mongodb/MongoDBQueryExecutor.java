package net.sf.esfinge.querybuilder.mongodb;

import org.mongodb.morphia.query.Query;

import net.sf.esfinge.querybuilder.executor.QueryExecutor;
import net.sf.esfinge.querybuilder.methodparser.QueryInfo;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.QueryType;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;

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
