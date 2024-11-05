package ef.qb.core.methodparser;

import java.util.Map;
import java.util.Set;
import ef.qb.core.exception.InvalidQuerySequenceException;
import ef.qb.core.methodparser.conditions.NullOption;

public class ValidationQueryVisitor implements QueryVisitor {

    private final QueryVisitor visitor;
    private QueryElement lastCalled = QueryElement.NONE;

    public ValidationQueryVisitor(QueryVisitor visitor) {
        this.visitor = visitor;
    }

    @Override
    public void visitEntity(String entity) {
        if (lastCalled != QueryElement.NONE) {
            throw new InvalidQuerySequenceException("Entity should be called only in the begining");
        }
        lastCalled = QueryElement.ENTITY;
        visitor.visitEntity(entity);
    }

    @Override
    public void visitConector(String conector) {
        if (lastCalled != QueryElement.CONDITION) {
            throw new InvalidQuerySequenceException("Conector called in wrong sequence");
        }
        visitor.visitConector(conector);
        lastCalled = QueryElement.CONECTOR;
    }

    @Override
    public void visitCondition(String propertyName, ComparisonType operator) {
        validateCondition();
        visitor.visitCondition(propertyName, operator);
    }

    @Override
    public void visitCondition(String propertyName, ComparisonType operator,
            NullOption nullOption) {
        validateCondition();
        visitor.visitCondition(propertyName, operator, nullOption);
    }

    @Override
    public void visitCondition(String propertyName, ComparisonType operator,
            Object fixedValue) {
        validateCondition();
        visitor.visitCondition(propertyName, operator, fixedValue);
    }

    private void validateCondition() {
        if (!(lastCalled == QueryElement.CONECTOR || lastCalled == QueryElement.ENTITY)) {
            throw new InvalidQuerySequenceException("Condition called in wrong sequence");
        }
        lastCalled = QueryElement.CONDITION;
    }

    @Override
    public void visitOrderBy(String property, OrderingDirection order) {
        if (lastCalled == QueryElement.NONE) {
            throw new InvalidQuerySequenceException("Order by can't be the first to be called");
        }
        if (lastCalled == QueryElement.CONECTOR) {
            throw new InvalidQuerySequenceException("Order by can't be after a conector");
        }
        visitor.visitOrderBy(property, order);
        lastCalled = QueryElement.ORDER_BY;
    }

    @Override
    public void visitEnd() {
        if (lastCalled == QueryElement.CONECTOR) {
            throw new InvalidQuerySequenceException("Conector should not be the last element");
        }
        visitor.visitEnd();
    }

    @Override
    public boolean isDynamic() {
        return visitor.isDynamic();
    }

    @Override
    public String getQuery() {
        return visitor.getQuery();
    }

    @Override
    public String getQuery(Map<String, Object> params) {
        return visitor.getQuery(params);
    }

    @Override
    public Set<String> getFixParameters() {
        return visitor.getFixParameters();
    }

    @Override
    public Object getFixParameterValue(String param) {
        return visitor.getFixParameterValue(param);
    }

    @Override
    public QueryRepresentation getQueryRepresentation() {
        return visitor.getQueryRepresentation();
    }

}
