package org.esfinge.querybuilder.methodparser.conditions;

import java.util.ArrayList;
import java.util.List;

import org.esfinge.querybuilder.methodparser.ComparisonType;
import org.esfinge.querybuilder.methodparser.QueryVisitor;
import org.esfinge.querybuilder.methodparser.formater.ContainsParameterFormater;
import org.esfinge.querybuilder.methodparser.formater.FormaterFactory;
import org.esfinge.querybuilder.methodparser.formater.ParameterFormater;
import org.esfinge.querybuilder.utils.ServiceLocator;

public class SimpleDefinedCondition implements QueryCondition{

	private String name;
	private ComparisonType operator = ComparisonType.EQUALS;
	private Object value;
	
	public SimpleDefinedCondition(String name, ComparisonType operator,
			Object value) {
		super();
		this.name = name;
		this.operator = operator;
		this.value = value;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public ComparisonType getOperator() {
		return operator;
	}
	public void setOperator(ComparisonType operator) {
		this.operator = operator;
	}
	@Override
	public int getParameterSize(){
		return 0;
	}
	
	@Override
	public List<String> getParameterNames() {
		List<String> l = new ArrayList<String>();
		l.add(name);
		return l;
	}
	
	@Override
	public List<String> getMethodParameterNames() {
		return new ArrayList<String>();
	}
	
	@Override
	public List<String> getMethodParameterProps() {
		return new ArrayList<String>();
	}

	@Override
	public void visit(QueryVisitor visitor) {
		visitor.visitCondition(name, operator, value);		
	}
	
	@Override
	public List<ParameterFormater> getParameterFormatters() {
		List<ParameterFormater> formatters = new ArrayList<ParameterFormater>();
		FormaterFactory factory = ServiceLocator.getServiceImplementation(FormaterFactory.class);
		formatters.add(factory.getFormater(operator));
		return formatters;
	}

	@Override
	public boolean isDynamic() {
		return false;
	}

}
