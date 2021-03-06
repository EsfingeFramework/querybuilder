package org.esfinge.querybuilder.neo4j;

import java.util.Map;
import java.util.Set;

import org.esfinge.querybuilder.neo4j.oomapper.Query;

import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;

@SuppressWarnings("rawtypes")
public class Neo4JQueryRepresentation implements QueryRepresentation{
	
	private Query neo4JQuery;
	private boolean dynamic;
	private Map<String, Object> fixParameters;
	
	public Neo4JQueryRepresentation(Query neo4JQuery, boolean dynamic,
			Map<String, Object> fixParameters) {
		this.neo4JQuery = neo4JQuery;
		this.dynamic = dynamic;
		this.fixParameters = fixParameters;
	}

	@Override
	public boolean isDynamic() {
		return dynamic;
	}

	@Override
	public Object getQuery() {
		return neo4JQuery;
	}

	@Override
	public Object getQuery(Map<String, Object> params) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> getFixParameters() {
		return fixParameters.keySet();
	}

	@Override
	public Object getFixParameterValue(String param) {
		return fixParameters.get(param);
	}

}
