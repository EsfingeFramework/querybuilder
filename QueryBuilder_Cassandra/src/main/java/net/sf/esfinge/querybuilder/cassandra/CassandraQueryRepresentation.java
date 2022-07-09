package net.sf.esfinge.querybuilder.cassandra;

import net.sf.esfinge.querybuilder.cassandra.querybuilding.ConditionStatement;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.OrderByClause;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.conditions.NullOption;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CassandraQueryRepresentation implements QueryRepresentation {

    private final String query;
    private final boolean dynamic;
    private final Map<String, Object> fixParametersMap;
    private final List<ConditionStatement> conditions;
    private final List<OrderByClause> orderByClause;
    String entity;

    public CassandraQueryRepresentation(String query, boolean dynamic, Map<String, Object> fixParametersMap, List<ConditionStatement> conditions, List<OrderByClause> orderByClause, String entity) {
        this.query = query;
        this.dynamic = dynamic;
        this.fixParametersMap = fixParametersMap;
        this.conditions = conditions;
        this.orderByClause = orderByClause;
        this.entity = entity;
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
    public Object getQuery(Map<String, Object> parameters) {
        updateConditions(parameters);

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM <#keyspace-name#>.").append(entity);

        for(ConditionStatement statement : conditions){
            if (parameters.get(statement.getPropertyName()) != null || statement.getNullOption() != NullOption.IGNORE_WHEN_NULL){

                statement.setValue(parameters.get(statement.getPropertyName()));

                if (!(statement.getValue() == null && statement.getNullOption() == NullOption.IGNORE_WHEN_NULL)){
                    if (!builder.toString().contains("WHERE"))
                        builder.append(" WHERE ");

                    builder.append(statement);

                    if (hasConditionNotToBeIgnoredNext(conditions.indexOf(statement))){
                        builder.append(" ").append(statement.getNextConnector()).append(" ");
                    }
                }
            }
        }

        return builder + (builder.toString().contains("WHERE") ? " ALLOW FILTERING" : "");
    }

    public void updateConditions(Map<String, Object> parameters){
        for(ConditionStatement statement : conditions) {
            if (parameters.get(statement.getPropertyName()) != null || statement.getNullOption() != NullOption.IGNORE_WHEN_NULL) {

                statement.setValue(parameters.get(statement.getPropertyName()));
            }
        }
    }

    private boolean hasConditionNotToBeIgnoredNext(int currentConditionIndex){
        for (int i = currentConditionIndex + 1; i < conditions.size(); i++){
            if (conditions.get(i).getNullOption() != NullOption.IGNORE_WHEN_NULL || conditions.get(i).getValue() != null)
                return true;
        }

        return false;
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
