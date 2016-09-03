package org.esfinge.querybuilder.neo4j;

import org.esfinge.querybuilder.neo4j.oomapper.Query;

import net.sf.esfinge.querybuilder.annotation.ServicePriority;
import net.sf.esfinge.querybuilder.executor.QueryExecutor;
import net.sf.esfinge.querybuilder.methodparser.QueryInfo;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.QueryType;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;

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
