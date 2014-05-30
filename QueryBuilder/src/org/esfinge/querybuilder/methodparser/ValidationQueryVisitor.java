package org.esfinge.querybuilder.methodparser;

import java.util.Map;
import java.util.Set;

import org.esfinge.querybuilder.exception.InvalidQuerySequenceException;
import org.esfinge.querybuilder.methodparser.conditions.NullOption;

public class ValidationQueryVisitor implements QueryVisitor{
	
	private QueryVisitor visitor;
	private QueryElement lastCalled = QueryElement.NONE;
	
	public ValidationQueryVisitor(QueryVisitor visitor) {
		this.visitor = visitor;
	}

	public void visitEntity(String entity) {
		if(lastCalled != QueryElement.NONE)
			throw new InvalidQuerySequenceException("Entity should be called only in the begining");
		lastCalled = QueryElement.ENTITY;
		visitor.visitEntity(entity);
	}

	public void visitConector(String conector) {
		if(lastCalled != QueryElement.CONDITION)
			throw new InvalidQuerySequenceException("Conector called in wrong sequence");
		visitor.visitConector(conector);
		lastCalled = QueryElement.CONECTOR;
	}

	public void visitCondition(String propertyName, ComparisonType operator) {
		validateCondition();
		visitor.visitCondition(propertyName, operator);
	}

	public void visitCondition(String propertyName, ComparisonType operator,
			NullOption nullOption) {
		validateCondition();
		visitor.visitCondition(propertyName, operator, nullOption);
	}

	public void visitCondition(String propertyName, ComparisonType operator,
			Object fixedValue) {
		validateCondition();
		visitor.visitCondition(propertyName, operator, fixedValue);
	}
	
	private void validateCondition(){
		if(!(lastCalled == QueryElement.CONECTOR || lastCalled == QueryElement.ENTITY)){
			throw new InvalidQuerySequenceException("Condition called in wrong sequence");
		}
		lastCalled = QueryElement.CONDITION;
	}

	public void visitOrderBy(String property, OrderingDirection order) {
		if(lastCalled == QueryElement.NONE)
			throw new InvalidQuerySequenceException("Order by can't be the first to be called");
		if(lastCalled == QueryElement.CONECTOR)
			throw new InvalidQuerySequenceException("Order by can't be after a conector");
		visitor.visitOrderBy(property, order);
		lastCalled = QueryElement.ORDER_BY;	
	}

	public void visitEnd() {
		if(lastCalled == QueryElement.CONECTOR)
			throw new InvalidQuerySequenceException("Conector should not be the last element");
		visitor.visitEnd();
	}

	public boolean isDynamic() {
		return visitor.isDynamic();
	}

	public String getQuery() {
		return visitor.getQuery();
	}

	public String getQuery(Map<String, Object> params) {
		return visitor.getQuery(params);
	}

	public Set<String> getFixParameters() {
		return visitor.getFixParameters();
	}

	public Object getFixParameterValue(String param) {
		return visitor.getFixParameterValue(param);
	}

	@Override
	public QueryRepresentation getQueryRepresentation() {
		return visitor.getQueryRepresentation();
	}

	

}
