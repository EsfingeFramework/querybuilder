package org.esfinge.querybuilder.methodparser.conditions;

import java.util.List;

import org.esfinge.querybuilder.methodparser.QueryVisitor;
import org.esfinge.querybuilder.methodparser.formater.ParameterFormater;

public interface QueryCondition {

	public int getParameterSize();
	
	public List<String> getParameterNames();
	
	public List<String> getMethodParameterNames();
	
	public List<ParameterFormater> getParameterFormatters();

	public void visit(QueryVisitor visitor);

	public boolean isDynamic();

	public List<String> getMethodParameterProps();

}
