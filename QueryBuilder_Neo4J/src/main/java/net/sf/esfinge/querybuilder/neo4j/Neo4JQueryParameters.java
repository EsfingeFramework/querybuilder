package net.sf.esfinge.querybuilder.neo4j;

import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.Filters;
import org.neo4j.ogm.cypher.query.PagingAndSortingQuery;
import org.neo4j.ogm.cypher.query.SortOrder;
import org.neo4j.ogm.session.Neo4jSession;
import org.neo4j.ogm.session.request.strategy.QueryStatements;

public class Neo4JQueryParameters {

	private String label;
	private Filters filters;
	private SortOrder sortOrder;

	public Neo4JQueryParameters(String label, Filters filters, SortOrder sortOrder) {
		this.label = label;
		this.filters = filters;
		this.sortOrder = sortOrder;
	}
	
	public String getLabel() {
		return label;
	}
	
	public Filters getFilters() {
		return filters;
	}
	
	public SortOrder getSortOrder() {
		return sortOrder;
	}
	
	public String resolveQuery(Class<?> type, Neo4jSession session) {
		StringBuilder sb = new StringBuilder();
		
		int depth = Neo4JRepository.DEPTH_LIST;
        QueryStatements<?> queryStatements = session.queryStatementsFor(type, depth);
		session.resolvePropertyAnnotations(type, filters);
        
		PagingAndSortingQuery query;
		if (filters.isEmpty()) {
            query = queryStatements.findByType(label, depth);
            query.setSortOrder(sortOrder);
            sb.append(query.getStatement());
        } else {
            query = queryStatements.findByType(label, filters, depth);
            query.setSortOrder(sortOrder);
            String statement = query.getStatement();
            for (Filter filter : filters) {
            	for (String key : filter.parameters().keySet()) {
            		Object object = filter.parameters().get(key);
            		if(object instanceof String) {
            			statement = statement.replace(String.format("{ `%s` }", key), String.format("'%s'", object));
            		} else {
            			statement = statement.replace(String.format("{ `%s` }", key), object.toString());
            		}
            	}
			}
        	sb.append(statement);
        }
		
		return sb.toString();
	}

}
