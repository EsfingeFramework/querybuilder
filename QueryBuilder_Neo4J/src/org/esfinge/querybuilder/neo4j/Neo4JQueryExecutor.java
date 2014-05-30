package org.esfinge.querybuilder.neo4j;

import org.esfinge.querybuilder.annotation.ServicePriority;
import org.esfinge.querybuilder.executor.QueryExecutor;
import org.esfinge.querybuilder.methodparser.QueryInfo;
import org.esfinge.querybuilder.methodparser.QueryRepresentation;
import org.esfinge.querybuilder.methodparser.QueryType;
import org.esfinge.querybuilder.methodparser.QueryVisitor;
import org.esfinge.querybuilder.neo4j.oomapper.Query;

@ServicePriority(1)
public class Neo4JQueryExecutor implements QueryExecutor {
	
	@Override
	public Object executeQuery(QueryInfo info, Object[] args) {
		
		QueryVisitor visitor = Neo4JVisitorFactory.createQueryVisitor(info, args);
		QueryRepresentation qr = visitor.getQueryRepresentation();
		
		@SuppressWarnings("rawtypes")
		Query q = (Query) qr.getQuery();
		
		if(info.getQueryType() == QueryType.RETRIEVE_SINGLE)
			return q.getSingle();
		else
			return q.asList();
	}

}
