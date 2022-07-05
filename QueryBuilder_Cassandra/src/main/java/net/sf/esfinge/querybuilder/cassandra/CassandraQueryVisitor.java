package net.sf.esfinge.querybuilder.cassandra;

import net.sf.esfinge.querybuilder.cassandra.exceptions.InvalidConnectorException;
import net.sf.esfinge.querybuilder.exception.InvalidQuerySequenceException;
import net.sf.esfinge.querybuilder.methodparser.*;
import net.sf.esfinge.querybuilder.methodparser.conditions.NullOption;

import java.util.Map;
import java.util.Set;

public class CassandraQueryVisitor implements QueryVisitor {



    private String entity;
    private QueryElement lastCalled = QueryElement.NONE;
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


    }

    @Override
    public void visitCondition(String s, ComparisonType comparisonType) {

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

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM " + entity);


        addWhere(sb);
        addConditions(sb);
        addOrderBy(sb);

        query = sb.toString();
    }

    private void addOrderBy(StringBuilder sb) {
        // TODO: IMPLEMENT
    }

    private void addConditions(StringBuilder sb) {
        // TODO: IMPLEMENT
    }

    private void addWhere(StringBuilder sb) {
        // TODO: IMPLEMENT
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
