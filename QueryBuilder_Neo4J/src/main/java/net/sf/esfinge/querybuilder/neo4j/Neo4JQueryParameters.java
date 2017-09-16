package net.sf.esfinge.querybuilder.neo4j;

import static net.sf.esfinge.querybuilder.neo4j.Neo4JRepository.DEPTH_LIST;

import java.util.Map;

import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.Filters;
import org.neo4j.ogm.cypher.query.SortOrder;
import org.neo4j.ogm.session.request.strategy.impl.NodeQueryStatements;

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
	
	@SuppressWarnings("unchecked")
	@Override
	public String toString() {
		String cypherQuery = new NodeQueryStatements<>().findByType(label, filters, DEPTH_LIST).getStatement();
		for (Filter filter : filters) {
			Map<String, Object> parameters = filter.getFunction().parameters();
			for (String key : parameters.keySet()) {
				Object value = parameters.get(key);
				if(value != null) cypherQuery = cypherQuery.replace(key, value.toString());
			}
		}
		return cypherQuery;
	}

}
