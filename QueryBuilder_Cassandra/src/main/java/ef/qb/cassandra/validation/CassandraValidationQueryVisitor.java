package ef.qb.cassandra.validation;

import ef.qb.cassandra.CassandraQueryRepresentation;
import ef.qb.cassandra.querybuilding.resultsprocessing.join.JoinClause;
import ef.qb.cassandra.querybuilding.resultsprocessing.ordering.OrderByClause;
import static ef.qb.cassandra.validation.CassandraQueryElement.CONDITION;
import static ef.qb.cassandra.validation.CassandraQueryElement.CONNECTOR;
import static ef.qb.cassandra.validation.CassandraQueryElement.END;
import static ef.qb.cassandra.validation.CassandraQueryElement.ENTITY;
import static ef.qb.cassandra.validation.CassandraQueryElement.NONE;
import static ef.qb.cassandra.validation.CassandraQueryElement.ORDER_BY;
import static ef.qb.cassandra.validation.CassandraQueryElement.SPECIAL_COMPARISON;
import ef.qb.core.exception.InvalidQuerySequenceException;
import ef.qb.core.methodparser.ComparisonType;
import static ef.qb.core.methodparser.ComparisonType.CONTAINS;
import static ef.qb.core.methodparser.ComparisonType.ENDS;
import static ef.qb.core.methodparser.ComparisonType.NOT_EQUALS;
import static ef.qb.core.methodparser.ComparisonType.STARTS;
import ef.qb.core.methodparser.OrderingDirection;
import ef.qb.core.methodparser.QueryRepresentation;
import ef.qb.core.methodparser.QueryVisitor;
import ef.qb.core.methodparser.conditions.NullOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CassandraValidationQueryVisitor implements QueryVisitor {

    private final CassandraChainQueryVisitor visitor;
    private CassandraQueryElement lastCalled;

    public CassandraValidationQueryVisitor(CassandraChainQueryVisitor visitor) {
        this.lastCalled = NONE;
        this.visitor = visitor;
    }

    public void visitEntity(String entity) {
        if (this.lastCalled != NONE) {
            throw new InvalidQuerySequenceException("Entity should be called only in the beginning");
        } else {
            this.lastCalled = ENTITY;
            this.visitor.visitEntity(entity);
        }
    }

    public void visitConector(String connector) {
        if (this.lastCalled != CONDITION && this.lastCalled != SPECIAL_COMPARISON) {
            throw new InvalidQuerySequenceException("Connector called in wrong sequence");
        } else {
            this.visitor.visitConector(connector);
            this.lastCalled = CONNECTOR;
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
        if (this.lastCalled != CONNECTOR && this.lastCalled != ENTITY) {
            throw new InvalidQuerySequenceException("Condition called in wrong sequence");
        } else {
            if (operator == NOT_EQUALS || operator == STARTS || operator == ENDS || operator == CONTAINS) {
                this.lastCalled = SPECIAL_COMPARISON;
            } else {
                this.lastCalled = CONDITION;
            }
        }
    }

    public void visitOrderBy(String property, OrderingDirection order) {
        if (this.lastCalled == NONE) {
            throw new InvalidQuerySequenceException("Order by can't be the first to be called");
        } else if (this.lastCalled == CONNECTOR) {
            throw new InvalidQuerySequenceException("Order by can't be after a connector");
        } else {
            this.visitor.visitOrderBy(property, order);
            this.lastCalled = ORDER_BY;
        }
    }

    public void visitEnd() {
        if (lastCalled == END) {
            throw new InvalidQuerySequenceException(
                    "Cannot end an already ended query sequence.");
        }

        if (lastCalled == NONE) {
            throw new InvalidQuerySequenceException(
                    "Cannot end an empty query sequence.");
        }

        if (this.lastCalled == CONNECTOR) {
            throw new InvalidQuerySequenceException("Connector should not be the last element");
        } else {
            this.visitor.visitEnd();
        }

        lastCalled = END;
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

        if (current != null) {
            visitors.add(current);
        }

        while (current.getSecondaryVisitor() != null) {
            current = current.getSecondaryVisitor();
            visitors.add(current);
        }

        return visitors;
    }

    public List<OrderByClause> getOrderByClauses() {
        CassandraChainQueryVisitor current = this.visitor;

        while (current.getSecondaryVisitor() != null) {
            current = current.getSecondaryVisitor();
        }

        return ((CassandraQueryRepresentation) current.getQueryRepresentation()).getOrderByClauses();
    }

    public List<JoinClause> getJoinClauses() {
        CassandraChainQueryVisitor current = this.visitor;

        while (current.getSecondaryVisitor() != null) {
            current = current.getSecondaryVisitor();
        }

        return ((CassandraQueryRepresentation) current.getQueryRepresentation()).getJoinClauses();
    }
}
