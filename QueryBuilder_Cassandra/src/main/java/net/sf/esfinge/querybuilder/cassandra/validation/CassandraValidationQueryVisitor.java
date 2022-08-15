package net.sf.esfinge.querybuilder.cassandra.validation;

import net.sf.esfinge.querybuilder.cassandra.CassandraQueryRepresentation;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ResultsProcessor;
import net.sf.esfinge.querybuilder.cassandra.querybuilding.resultsprocessing.ordering.OrderByClause;
import net.sf.esfinge.querybuilder.exception.InvalidQuerySequenceException;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.OrderingDirection;
import net.sf.esfinge.querybuilder.methodparser.QueryRepresentation;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import net.sf.esfinge.querybuilder.methodparser.conditions.NullOption;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CassandraValidationQueryVisitor implements QueryVisitor {
    private final CassandraChainQueryVisitor visitor;
    private CassandraQueryElement lastCalled;

    public CassandraValidationQueryVisitor(CassandraChainQueryVisitor visitor) {
        this.lastCalled = CassandraQueryElement.NONE;
        this.visitor = visitor;
    }

    public void visitEntity(String entity) {
        if (this.lastCalled != CassandraQueryElement.NONE) {
            throw new InvalidQuerySequenceException("Entity should be called only in the beginning");
        } else {
            this.lastCalled = CassandraQueryElement.ENTITY;
            this.visitor.visitEntity(entity);
        }
    }

    public void visitConector(String connector) {
        if (this.lastCalled != CassandraQueryElement.CONDITION && this.lastCalled != CassandraQueryElement.SPECIAL_COMPARISON) {
            throw new InvalidQuerySequenceException("Connector called in wrong sequence");
        } else {
            this.visitor.visitConector(connector);
            this.lastCalled = CassandraQueryElement.CONNECTOR;
        }
    }

    public void visitCondition(String propertyName, ComparisonType operator) {
        this.validateCondition(operator);
        this.visitor.visitCondition(propertyName, operator);
    }

    public void visitCondition(String propertyName, ComparisonType operator, NullOption nullOption) {
        this.validateCondition(operator);
        this.visitor.visitCondition(propertyName, operator, nullOption);
    }

    public void visitCondition(String propertyName, ComparisonType operator, Object fixedValue) {
        this.validateCondition(operator);
        this.visitor.visitCondition(propertyName, operator, fixedValue);
    }

    private void validateCondition(ComparisonType operator) {
        if (this.lastCalled != CassandraQueryElement.CONNECTOR && this.lastCalled != CassandraQueryElement.ENTITY) {
            throw new InvalidQuerySequenceException("Condition called in wrong sequence");
        } else {
            if (operator == ComparisonType.NOT_EQUALS || operator == ComparisonType.STARTS || operator == ComparisonType.ENDS || operator == ComparisonType.CONTAINS)
                this.lastCalled = CassandraQueryElement.SPECIAL_COMPARISON;
            else
                this.lastCalled = CassandraQueryElement.CONDITION;
        }
    }

    public void visitOrderBy(String property, OrderingDirection order) {
        if (this.lastCalled == CassandraQueryElement.NONE) {
            throw new InvalidQuerySequenceException("Order by can't be the first to be called");
        } else if (this.lastCalled == CassandraQueryElement.CONNECTOR) {
            throw new InvalidQuerySequenceException("Order by can't be after a connector");
        } else {
            this.visitor.visitOrderBy(property, order);
            this.lastCalled = CassandraQueryElement.ORDER_BY;
        }
    }

    public void visitEnd() {
        if (lastCalled == CassandraQueryElement.NONE)
            throw new InvalidQuerySequenceException(
                    "Cannot end an empty query sequence.");

        if (this.lastCalled == CassandraQueryElement.CONNECTOR) {
            throw new InvalidQuerySequenceException("Connector should not be the last element");
        } else {
            this.visitor.visitEnd();
        }
    }

    public boolean isDynamic() {
        return this.visitor.isDynamic();
    }

    public String getQuery() {
        return this.visitor.getQuery();
    }

    public String getQuery(Map<String, Object> params) {
        return this.visitor.getQuery(params);
    }

    public Set<String> getFixParameters() {
        return this.visitor.getFixParameters();
    }

    public Object getFixParameterValue(String param) {
        return this.visitor.getFixParameterValue(param);
    }

    public QueryRepresentation getQueryRepresentation() {
        return this.visitor.getQueryRepresentation();
    }

    public CassandraChainQueryVisitor getSecondaryVisitor() {
        return this.visitor.getSecondaryVisitor();
    }

    public List<CassandraChainQueryVisitor> getSecondaryVisitorsList() {
        List<CassandraChainQueryVisitor> visitors = new ArrayList<>();

        CassandraChainQueryVisitor current = this.visitor;

        if (current != null)
            visitors.add(current);

        while (current.getSecondaryVisitor() != null) {
            current = current.getSecondaryVisitor();
            visitors.add(current);
        }

        return visitors;
    }

    public List<OrderByClause> getOrderByClauses() {
        CassandraChainQueryVisitor current = this.visitor;

        while (current.getSecondaryVisitor() != null){
            current =  current.getSecondaryVisitor();
        }

        return ((CassandraQueryRepresentation)current.getQueryRepresentation()).getOrderByClauses();
    }
}