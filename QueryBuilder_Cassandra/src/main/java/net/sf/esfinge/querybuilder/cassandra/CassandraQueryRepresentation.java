package net.sf.esfinge.querybuilder.cassandra;

import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;

import java.util.Map;
import java.util.Set;

public class CassandraQueryRepresentation implements QueryRepresentation {

    // TODO: WHAT DOES THIS CLASS DO??? IS IT REALLY NEEDED?

    private String query;
    private boolean dynamic;
    private Map<String, Object> fixParametersMap;

    public CassandraQueryRepresentation(String query, boolean dynamic, Map<String, Object> fixParametersMap) {
        this.query = query;
        this.dynamic = dynamic;
        this.fixParametersMap = fixParametersMap;
    }

    @Override
    public boolean isDynamic() {
        return dynamic;
    }

    @Override
    public Object getQuery() {
        return query;
    }

    @Override
    public Object getQuery(Map<String, Object> map) {
        return null;
    }

    @Override
    public Set<String> getFixParameters() {
        return fixParametersMap.keySet();
    }

    @Override
    public Object getFixParameterValue(String paramName) {
        return fixParametersMap.get(paramName);
    }
}
