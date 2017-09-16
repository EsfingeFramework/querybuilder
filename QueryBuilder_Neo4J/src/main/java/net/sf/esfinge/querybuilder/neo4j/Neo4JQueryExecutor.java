package net.sf.esfinge.querybuilder.neo4j;

import java.util.Collection;

import org.neo4j.ogm.metadata.ClassInfo;
import org.neo4j.ogm.session.Neo4jSession;

import net.sf.esfinge.querybuilder.annotation.ServicePriority;
import net.sf.esfinge.querybuilder.executor.QueryExecutor;
import net.sf.esfinge.querybuilder.methodparser.QueryInfo;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.QueryType;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;

@ServicePriority(1)
public class Neo4JQueryExecutor implements QueryExecutor {
	
	private static final Neo4jSession neo4j = ServiceLocator.getServiceImplementation(DatastoreProvider.class).getDatastore();
	
	@Override
	public Object executeQuery(QueryInfo info, Object[] args) {
		
		QueryVisitor visitor = Neo4JVisitorFactory.createQueryVisitor(info, args);
		QueryRepresentation queryRepresentation = visitor.getQueryRepresentation();

		Neo4JQueryParameters statments = (Neo4JQueryParameters) queryRepresentation.getQuery();

		ClassInfo classInfo = neo4j.metaData().classInfo(statments.getLabel());
		Class<?> entityClass = classInfo.getUnderlyingClass();
		
		Collection<?> collection = neo4j.loadAll(entityClass, statments.getFilters(), statments.getSortOrder());
		if(info.getQueryType() == QueryType.RETRIEVE_SINGLE) {
			if(!collection.isEmpty()) {
				return collection.iterator().next();
			} else {
				return null;
			}
		} else {
			return collection;
		}
	}

}
