package net.sf.esfinge.querybuilder.cassandra;

import net.sf.esfinge.querybuilder.cassandra.exceptions.InvalidConnectorException;
import net.sf.esfinge.querybuilder.exception.InvalidQuerySequenceException;
import net.sf.esfinge.querybuilder.methodparser.*;
import net.sf.esfinge.querybuilder.methodparser.conditions.NullOption;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CassandraQueryVisitor implements QueryVisitor {


    private String entity;
    private QueryElement lastCalled = QueryElement.NONE;
    private List<ConditionStatement> conditions = new ArrayList<>();
    private String query = "";

    @Override
    public void visitEntity(String s) {
        if (lastCalled != QueryElement.NONE)
            throw new InvalidQuerySequenceException(
                    "Entity should only be called in the beginning.");

        lastCalled = QueryElement.ENTITY;

        this.entity = s;
    }

    @Override
    public void visitConector(String s) {
        if (lastCalled != QueryElement.CONDITION)
            throw new InvalidQuerySequenceException(
                    "A connector should only be called after a condition.");

        if (!s.equalsIgnoreCase("AND") && !s.equalsIgnoreCase("OR"))
            throw new InvalidConnectorException("Invalid connector \"" + s + "\", valid values are: AND, OR");

        conditions.get(conditions.size() - 1).setNextConnector(s.toUpperCase());

        lastCalled = QueryElement.CONECTOR;
    }

    @Override
    public void visitCondition(String s, ComparisonType comparisonType) {
        if (lastCalled == QueryElement.CONDITION)
            throw new InvalidQuerySequenceException(
                    "A second condition can only be called after a connector.");

        conditions.add(new ConditionStatement(s, comparisonType));

        lastCalled = QueryElement.CONDITION;
    }

    @Override
    public void visitCondition(String s, ComparisonType comparisonType, NullOption nullOption) {

    }

    @Override
    public void visitCondition(String s, ComparisonType comparisonType, Object o) {

    }

    @Override
    public void visitOrderBy(String s, OrderingDirection orderingDirection) {

    }

    @Override
    public void visitEnd() {
        if (lastCalled == QueryElement.CONECTOR)
            throw new InvalidQuerySequenceException(
                    "A connector should not be called right before the end.");

        if (lastCalled == QueryElement.NONE)
            throw new InvalidQuerySequenceException(
                    "Cannot end an empty query sequence.");

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM " + entity);

        if (!conditions.isEmpty()) {
            sb.append(" WHERE ");
            sb.append(getConditions());
        }

        addOrderBy(sb);

        query = sb.toString();
    }

    private void addOrderBy(StringBuilder sb) {
        // TODO: IMPLEMENT
    }

    private String getConditions() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < conditions.size(); i++) {
            sb.append(conditions.get(i).toString() + " " + (i + 1) + "?");

            if (i < conditions.size() - 1)
                sb.append(" " + conditions.get(i).getNextConnector() + " ");
        }

        return sb.toString();
    }

    @Override
    public boolean isDynamic() {
        return false;
    }

    @Override
    public String getQuery() {
        return query;
    }

    @Override
    public String getQuery(Map<String, Object> map) {
        return null;
    }

    @Override
    public Set<String> getFixParameters() {
        return null;
    }

    @Override
    public Object getFixParameterValue(String s) {
        return null;
    }

    @Override
    public QueryRepresentation getQueryRepresentation() {
        return null;
    }
}
