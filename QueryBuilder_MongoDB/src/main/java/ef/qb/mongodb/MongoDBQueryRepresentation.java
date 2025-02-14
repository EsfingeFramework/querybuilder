package ef.qb.mongodb;

import dev.morphia.query.Query;
import ef.qb.core.methodparser.QueryRepresentation;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class MongoDBQueryRepresentation implements QueryRepresentation {

    private final Query mongoQuery;
    private final boolean dynamic;
    private final Map<String, Object> fixParameters;

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
