package net.sf.esfinge.querybuilder.cassandra;

import net.sf.esfinge.querybuilder.cassandra.querybuilding.ConditionStatement;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.OrderByClause;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.conditions.NullOption;

import java.sql.BatchUpdateException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CassandraQueryRepresentation implements QueryRepresentation {

    // TODO: WHAT DOES THIS CLASS DO??? IS IT REALLY NEEDED?

    private final String query;
    private final boolean dynamic;
    private final Map<String, Object> fixParametersMap;
    private List<ConditionStatement> conditions;

    private final List<OrderByClause> orderByClause;

    public CassandraQueryRepresentation(String query, boolean dynamic, Map<String, Object> fixParametersMap, List<ConditionStatement> conditions, List<OrderByClause> orderByClause) {
        this.query = query;
        this.dynamic = dynamic;
        this.fixParametersMap = fixParametersMap;
        this.conditions = conditions;
        this.orderByClause = orderByClause;
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
        StringBuilder builder = new StringBuilder();
        builder.append(query);

        for(ConditionStatement statement : conditions){
            if (map.get(statement.getPropertyName()) != null){
                if (!builder.toString().contains("WHERE"))
                    builder.append(" WHERE ");

                statement.setValue(map.get(statement.getPropertyName()));
                builder.append(statement.getConditionRepresentation());
            }
        }
        return builder.toString();
    }

    @Override
    public Set<String> getFixParameters() {
        return fixParametersMap.keySet();
    }

    @Override
    public Object getFixParameterValue(String paramName) {
        return fixParametersMap.get(paramName);
    }

    public List<OrderByClause> getOrderByClause() {
        return orderByClause;
    }
}
