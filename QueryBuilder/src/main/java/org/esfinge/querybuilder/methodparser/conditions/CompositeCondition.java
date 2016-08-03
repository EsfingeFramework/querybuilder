package org.esfinge.querybuilder.methodparser.conditions;

import java.util.List;

import org.esfinge.querybuilder.methodparser.QueryVisitor;
import org.esfinge.querybuilder.methodparser.formater.ParameterFormater;

public class CompositeCondition implements QueryCondition{
	
	private QueryCondition firstCondition;
	private QueryCondition secondCondition;
	private String conector;
	
	public CompositeCondition(QueryCondition firstCondition, String conector,
			QueryCondition secondCondition) {
		super();
		this.firstCondition = firstCondition;
		this.conector = conector;
		this.secondCondition = secondCondition;
	}

	@Override
	public int getParameterSize() {
		return firstCondition.getParameterSize() + secondCondition.getParameterSize();
	}

	@Override
	public List<String> getParameterNames() {
		List<String> result = firstCondition.getParameterNames();
		result.addAll(secondCondition.getParameterNames());
		return result;
	}

	@Override
	public List<String> getMethodParameterNames() {
		List<String> result = firstCondition.getMethodParameterNames();
		result.addAll(secondCondition.getMethodParameterNames());
		return result;
	}

	@Override
	public void visit(QueryVisitor visitor) {
		firstCondition.visit(visitor);
		visitor.visitConector(conector);
		secondCondition.visit(visitor);
	}

	@Override
	public List<ParameterFormater> getParameterFormatters() {
		List<ParameterFormater> result = firstCondition.getParameterFormatters();
		result.addAll(secondCondition.getParameterFormatters());
		return result;
	}

	@Override
	public boolean isDynamic() {
		return firstCondition.isDynamic() || secondCondition.isDynamic();
	}

	@Override
	public List<String> getMethodParameterProps() {
		List<String> result = firstCondition.getMethodParameterProps();
		result.addAll(secondCondition.getMethodParameterProps());
		return result;
	}
	
}
