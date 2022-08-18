package net.sf.esfinge.querybuilder.cassandra;

import net.sf.esfinge.querybuilder.cassandra.querybuilding.ConditionStatement;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.join.JoinClause;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ordering.OrderByClause;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonClause;
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
    private final List<OrderByClause> orderByClauses;
    private final List<SpecialComparisonClause> specialComparisonClauses;
    private final List<JoinClause> joinClauses;
    private final String entity;

    public CassandraQueryRepresentation(String query, boolean dynamic, Map<String, Object> fixParametersMap, List<ConditionStatement> conditions, List<OrderByClause> orderByClauses, List<SpecialComparisonClause> specialComparisonClauses, List<JoinClause> joinClauses, String entity) {
        this.query = query;
        this.dynamic = dynamic;
        this.fixParametersMap = fixParametersMap;
        this.conditions = conditions;
        this.orderByClauses = orderByClauses;
        this.specialComparisonClauses = specialComparisonClauses;
        this.joinClauses = joinClauses;
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

        // Set the statement value based on the parameters
        for (ConditionStatement statement : conditions) {
            String propertyName = statement.getPropertyName();

            // Check if the property name in the parameter map is named after the param name + comparison convention
            // if so, update the name of the property for the search this is used for the Query objects with comparison
            if (parameters.get(statement.getPropertyName()) == null) {
                if (parameters.get(propertyName + statement.getComparisonType().getOpName()) != null)
                    propertyName += statement.getComparisonType().getOpName();
            }

            statement.setValue(parameters.get(propertyName));

        }

        // Append the statement value according to the rules
        for (ConditionStatement statement : conditions) {
            if (!(statement.getValue() == null && statement.getNullOption() == NullOption.IGNORE_WHEN_NULL)) {
                if (!builder.toString().contains("WHERE"))
                    builder.append(" WHERE ");

                builder.append(statement);
                if (hasConditionNotToBeIgnoredNext(conditions.indexOf(statement))) {
                    builder.append(" ").append(statement.getNextConnector()).append(" ");
                }
            }
        }

        return builder + (builder.toString().contains("WHERE") ? " ALLOW FILTERING" : "");
    }

    public void updateConditions(Map<String, Object> parameters) {
        for (ConditionStatement statement : conditions) {
            if (parameters.get(statement.getPropertyName()) != null || statement.getNullOption() != NullOption.IGNORE_WHEN_NULL) {

                statement.setValue(parameters.get(statement.getPropertyName()));
            }
        }
    }

    private boolean hasConditionNotToBeIgnoredNext(int currentConditionIndex) {
        for (int i = currentConditionIndex + 1; i < conditions.size(); i++) {
            ConditionStatement c = conditions.get(i);

            if (c.getNullOption() != NullOption.IGNORE_WHEN_NULL || c.getValue() != null) {
                return true;
            }
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

    public List<OrderByClause> getOrderByClauses() {
        return orderByClauses;
    }

    public List<SpecialComparisonClause> getSpecialComparisonClauses() {
        return specialComparisonClauses;
    }

    public List<JoinClause> getJoinClauses() {
        return joinClauses;
    }
}
