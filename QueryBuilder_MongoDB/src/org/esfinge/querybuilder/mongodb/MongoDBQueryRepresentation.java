package org.esfinge.querybuilder.mongodb;

import java.util.Map;
import java.util.Set;

import org.mongodb.morphia.query.Query;

import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;

@SuppressWarnings("rawtypes")
public class MongoDBQueryRepresentation implements QueryRepresentation{
	
	private Query mongoQuery;
	private boolean dynamic;
	private Map<String, Object> fixParameters;
	
	public MongoDBQueryRepresentation(Query mongoQuery, boolean dynamic,
			Map<String, Object> fixParameters) {
		this.mongoQuery = mongoQuery;
		this.dynamic = dynamic;
		this.fixParameters = fixParameters;
	}

	@Override
	public boolean isDynamic() {
		return dynamic;
	}

	@Override
	public Object getQuery() {
		return mongoQuery;
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
