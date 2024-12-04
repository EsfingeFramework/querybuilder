package ef.qb.neo4j;

import ef.qb.core.methodparser.QueryRepresentation;
import java.util.Map;
import java.util.Set;

public class Neo4JQueryRepresentation implements QueryRepresentation {

    private Neo4JQueryParameters neo4JQuery;
    private boolean dynamic;
    private Map<String, Object> fixParameters;

    public Neo4JQueryRepresentation(Neo4JQueryParameters neo4JQuery, boolean dynamic, Map<String, Object> fixParameters) {
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
