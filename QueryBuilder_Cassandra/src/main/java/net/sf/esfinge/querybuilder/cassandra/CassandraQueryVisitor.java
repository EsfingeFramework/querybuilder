package net.sf.esfinge.querybuilder.cassandra;

import net.sf.esfinge.querybuilder.exception.InvalidQuerySequenceException;
import net.sf.esfinge.querybuilder.methodparser.*;
import net.sf.esfinge.querybuilder.methodparser.conditions.NullOption;

import java.util.Map;
import java.util.Set;

public class CassandraQueryVisitor implements QueryVisitor {

    private String entity;
    private QueryElement lastCalled = QueryElement.NONE;

    @Override
    public void visitEntity(String s) {
        if (lastCalled != QueryElement.NONE)
            throw new InvalidQuerySequenceException(
                    "Entity should be called only in the beginning.");

        this.entity = entity;
    }

    @Override
    public void visitConector(String s) {

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

    }

    @Override
    public boolean isDynamic() {
        return false;
    }

    @Override
    public String getQuery() {
        return null;
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
