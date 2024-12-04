package org.esfinge.querybuilder.methodparser;

import java.util.Map;
import java.util.Set;

import org.esfinge.querybuilder.methodparser.conditions.NullOption;

public interface QueryVisitor {

	void visitEntity(String entity);

	void visitConector(String conector);

	void visitCondition(String propertyName, ComparisonType operator);
	
	void visitCondition(String propertyName, ComparisonType operator, NullOption nullOption);

	void visitCondition(String propertyName, ComparisonType operator, Object fixedValue);

	void visitOrderBy(String property, OrderingDirection order);
	
	void visitEnd();
	
	@Deprecated
	public boolean isDynamic();
	
	@Deprecated
	public String getQuery();
	
	@Deprecated
	public String getQuery(Map<String, Object> params);

	@Deprecated
	public Set<String> getFixParameters();

	@Deprecated
	public Object getFixParameterValue(String param);
	
	public QueryRepresentation getQueryRepresentation();

}
