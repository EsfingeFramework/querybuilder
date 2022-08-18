package net.sf.esfinge.querybuilder.cassandra;

import net.sf.esfinge.querybuilder.cassandra.exceptions.InvalidConnectorException;
import net.sf.esfinge.querybuilder.cassandra.exceptions.JoinDepthLimitExceededException;
import net.sf.esfinge.querybuilder.cassandra.exceptions.UnsupportedCassandraOperationException;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.ConditionStatement;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.QueryBuildingUtils;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.join.JoinClause;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.join.JoinComparisonType;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ordering.OrderByClause;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonClause;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.specialcomparison.SpecialComparisonType;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.OrderingDirection;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import net.sf.esfinge.querybuilder.methodparser.conditions.NullOption;

import java.util.*;

public class CassandraQueryVisitor implements QueryVisitor {

    private final List<ConditionStatement> conditions = new ArrayList<>();
    private final List<OrderByClause> orderByClauses = new ArrayList<>();
    private final List<SpecialComparisonClause> specialComparisonClauses = new ArrayList<>();
    private final List<JoinClause> joinClauses = new ArrayList<>();
    private final int argumentPositionOffset;
    CassandraQueryVisitor previousVisitor;
    private String entity;
    private String query = "";
    private int numberOfFixedValues = 0;

    public CassandraQueryVisitor() {
        argumentPositionOffset = 0;
    }

    public CassandraQueryVisitor(CassandraQueryVisitor previousVisitor) {
        // When call this constructor when we have a secondary query, in that case
        // we need to pass the previous visitor in order to update the offset for
        // the position of the arguments of the secondary queries

        this.argumentPositionOffset = previousVisitor.getConditions().size() +
                previousVisitor.getSpecialComparisonClauses().size() +
                previousVisitor.getJoinClauses().size() -
                previousVisitor.getNumberOfFixedValues() +
                previousVisitor.getArgumentPositionOffset();

        this.previousVisitor = previousVisitor;
    }

    @Override
    public void visitEntity(String entity) {
        this.entity = entity;
    }

    @Override
    public void visitConector(String connector) {
        // Attention! In Cassandra OR statements are not supported as in relational
        // Databases, here they are implemented at the application logic
        if (connector.equalsIgnoreCase("AND")) {
            // If there are no conditions clauses or if the last nextConnector in the previous condition
            // is already set, then the last condition was a SpecialComparison and there is no need to store
            // the nextConnector (OR statements are computed at the application logic)
            if (!conditions.isEmpty() && conditions.get(conditions.size() - 1).getNextConnector() == null)
                conditions.get(conditions.size() - 1).setNextConnector(connector.toUpperCase());
        } else
            throw new InvalidConnectorException("Invalid connector for primary visitor \"" + connector + "\", valid values are: {'AND','and'}");
    }

    @Override
    public void visitCondition(String parameter, ComparisonType comparisonType) {
        if (parameter.contains(".")) {
            verifyJoinLimit(parameter);
            String joinTypeName = parameter.substring(0, parameter.indexOf("."));
            String joinAttributeName = parameter.substring(parameter.indexOf(".") + 1);

            joinClauses.add(new JoinClause(joinTypeName, joinAttributeName, JoinComparisonType.fromComparisonType(comparisonType)));
            joinClauses.get(joinClauses.size() - 1).setArgPosition(conditions.size() + specialComparisonClauses.size() + joinClauses.size() - 1 - numberOfFixedValues + argumentPositionOffset);
        } else {
            // Cassandra supports only these conditional operators in the WHERE clause:
            // IN, =, >, >=, <, or <=, but not all in certain situations.
            // The other comparison types are implemented at the application logic, namely:
            // <> (NOT EQUALS), STARTS, ENDS AND CONTAINS
            if (comparisonType == ComparisonType.NOT_EQUALS || comparisonType == ComparisonType.STARTS || comparisonType == ComparisonType.ENDS || comparisonType == ComparisonType.CONTAINS) {
                //throw new UnsupportedCassandraOperationException("Comparison type " + comparisonType + " not supported in Cassandra");
                specialComparisonClauses.add(new SpecialComparisonClause(parameter, SpecialComparisonType.fromComparisonType(comparisonType)));

                // Need to set the position of the argument, otherwise cannot keep track of which argument is associated with this condition
                specialComparisonClauses.get(specialComparisonClauses.size() - 1).setArgPosition(conditions.size() + specialComparisonClauses.size() + joinClauses.size() - 1 - numberOfFixedValues + argumentPositionOffset);
            } else {
                conditions.add(new ConditionStatement(parameter, comparisonType));
                conditions.get(conditions.size() - 1).setConditionIndex(conditions.size() + specialComparisonClauses.size() + joinClauses.size() - 1 - numberOfFixedValues + argumentPositionOffset);
            }
        }
    }

    @Override
    public void visitCondition(String parameter, ComparisonType comparisonType, NullOption nullOption) {
        if (parameter.contains(".")) {
            verifyJoinLimit(parameter);
            String joinTypeName = parameter.substring(0, parameter.indexOf("."));
            String joinAttributeName = parameter.substring(parameter.indexOf(".") + 1);

            if (nullOption == NullOption.COMPARE_TO_NULL) {
                if (comparisonType == ComparisonType.NOT_EQUALS)
                    joinClauses.add(new JoinClause(joinTypeName, joinAttributeName, JoinComparisonType.fromComparisonType(comparisonType)));
                else if (comparisonType == ComparisonType.EQUALS)
                    joinClauses.add(new JoinClause(joinTypeName, joinAttributeName, JoinComparisonType.COMPARE_TO_NULL));
                else
                    throw new UnsupportedCassandraOperationException("Cannot apply comparison type \"" + comparisonType.name() + "\" to null value");
            }

            joinClauses.get(joinClauses.size() - 1).setArgPosition(conditions.size() + specialComparisonClauses.size() + joinClauses.size() - 1 - numberOfFixedValues + argumentPositionOffset);
        } else {
            // Cassandra doesn't support querying based on null values, even for secondary indexes
            // (like you can in a relational database)
            if (nullOption == NullOption.COMPARE_TO_NULL) {
                if (comparisonType == ComparisonType.NOT_EQUALS)
                    specialComparisonClauses.add(new SpecialComparisonClause(parameter, SpecialComparisonType.fromComparisonType(comparisonType)));
                else if (comparisonType == ComparisonType.EQUALS)
                    specialComparisonClauses.add(new SpecialComparisonClause(parameter, SpecialComparisonType.COMPARE_TO_NULL));
                else
                    throw new UnsupportedCassandraOperationException("Cannot apply comparison type \"" + comparisonType.name() + "\" to null value");

                // Need to set the position of the argument, otherwise cannot keep track of which argument is associated with this condition
                specialComparisonClauses.get(specialComparisonClauses.size() - 1).setArgPosition(conditions.size() + specialComparisonClauses.size() + joinClauses.size() - 1 - numberOfFixedValues + argumentPositionOffset);
            } else {
                visitCondition(parameter, comparisonType);

                conditions.get(conditions.size() - 1).setNullOption(nullOption);
            }
        }
    }

    @Override
    public void visitCondition(String parameter, ComparisonType comparisonType, Object value) {
        visitCondition(parameter, comparisonType);

        if (parameter.contains("."))
            joinClauses.get(joinClauses.size() - 1).setValue(value);
        else if (comparisonType == ComparisonType.NOT_EQUALS || comparisonType == ComparisonType.STARTS || comparisonType == ComparisonType.ENDS || comparisonType == ComparisonType.CONTAINS){
            specialComparisonClauses.get(specialComparisonClauses.size() - 1).setValue(value);
        } else
            conditions.get(conditions.size() - 1).setValue(value);

        numberOfFixedValues++;
    }

    private void verifyJoinLimit(String parameter) {
        if (QueryBuildingUtils.countOccurrenceOfCharacterInString(parameter, '.') > 1)
            throw new JoinDepthLimitExceededException("Join queries are supported only to the depth of 1");
    }

    @Override
    public void visitOrderBy(String parameter, OrderingDirection orderingDirection) {
        // Cassandra only allows ORDER by in the same direction as we
        // define in the "CLUSTERING ORDER BY" clause of CREATE TABLE.
        // Thus, it is implemented at the application logic
        orderByClauses.add(new OrderByClause(parameter, orderingDirection));
    }

    @Override
    public void visitEnd() {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM <#keyspace-name#>.").append(entity);

        if (!conditions.isEmpty()) {
            if (hasOneNoIgnorableProperty()) {
                builder.append(" WHERE ");
                builder.append(getConditionsQuery());
            }
        }

        query = builder + (builder.toString().contains("WHERE") ? " ALLOW FILTERING" : "");
    }

    private boolean hasOneNoIgnorableProperty() {
        for (ConditionStatement cond : conditions) {
            if (cond.getNullOption() != NullOption.IGNORE_WHEN_NULL) {
                return true;
            }
        }
        return false;
    }

    private String getConditionsQuery() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < conditions.size(); i++) {
            sb.append(conditions.get(i).toString());

            if (i < conditions.size() - 1) {
                if (!(conditions.get(i).isIgnoredCondition())) {
                    if (hasConditionNotToBeIgnoredNext(i))
                        sb.append(" ").append(conditions.get(i).getNextConnector()).append(" ");
                }
            }
        }

        return sb.toString();
    }

    private boolean hasConditionNotToBeIgnoredNext(int currentConditionIndex) {
        for (int i = currentConditionIndex + 1; i < conditions.size(); i++) {
            if (conditions.get(i).getNullOption() != NullOption.IGNORE_WHEN_NULL || conditions.get(i).getValue() != null)
                return true;
        }

        return false;
    }

    @Override
    public boolean isDynamic() {
        for (ConditionStatement cond : conditions) {
            if (cond.getNullOption() != NullOption.NONE) {
                return true;
            }
        }

        for (SpecialComparisonClause c : specialComparisonClauses) {
            if (c.getSpecialComparisonType() == SpecialComparisonType.COMPARE_TO_NULL)
                return true;
        }

        return false;
    }

    @Override
    public String getQuery() {
        return query;
    }

    @Override
    @Deprecated
    public String getQuery(Map<String, Object> map) {
        throw new UnsupportedOperationException("getQuery method in CassandraQueryVisitor not supported, use CassandraQueryRepresentation instead.");
    }

    @Override
    @Deprecated
    public Set<String> getFixParameters() {
        throw new UnsupportedOperationException("getFixParameters method in CassandraQueryVisitor not supported, use CassandraQueryRepresentation instead.");
    }

    @Override
    @Deprecated
    public Object getFixParameterValue(String s) {
        throw new UnsupportedOperationException("getFixParameterValue method in CassandraQueryVisitor not supported, use CassandraQueryRepresentation instead...");
    }

    private Map<String, Object> getFixParametersMap() {
        Map<String, Object> fixParameters = new HashMap<>();
        for (ConditionStatement cond : conditions) {
            if (cond.getValue() != null) {
                fixParameters.put(cond.getPropertyName(), cond.getValue());
            }
        }
        return fixParameters;
    }

    @Override
    public QueryRepresentation getQueryRepresentation() {
        return new CassandraQueryRepresentation(getQuery(), isDynamic(), getFixParametersMap(), conditions, orderByClauses, specialComparisonClauses, joinClauses, entity);
    }

    public String getEntity() {
        return this.entity;
    }


    public List<ConditionStatement> getConditions() {
        return conditions;
    }

    public List<SpecialComparisonClause> getSpecialComparisonClauses() {
        return specialComparisonClauses;
    }

    public int getNumberOfFixedValues() {
        return numberOfFixedValues;
    }

    public int getArgumentPositionOffset() {
        return argumentPositionOffset;
    }

    public List<JoinClause> getJoinClauses() {
        return joinClauses;
    }
}
