package net.sf.esfinge.querybuilder.methodparser.conditions;

import java.util.ArrayList;
import java.util.List;

import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.QueryVisitor;
import net.sf.esfinge.querybuilder.methodparser.formater.ContainsParameterFormater;
import net.sf.esfinge.querybuilder.methodparser.formater.DefaultParameterFormater;
import net.sf.esfinge.querybuilder.methodparser.formater.EndsParameterFormater;
import net.sf.esfinge.querybuilder.methodparser.formater.FormaterFactory;
import net.sf.esfinge.querybuilder.methodparser.formater.ParameterFormater;
import net.sf.esfinge.querybuilder.methodparser.formater.StartsParameterFormater;
import net.sf.esfinge.querybuilder.utils.ServiceLocator;
import net.sf.esfinge.querybuilder.utils.StringUtils;

public class SimpleCondition implements QueryCondition{
	
	private String name;
	private ComparisonType operator = ComparisonType.EQUALS;
	private NullOption nullOption = NullOption.NONE;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public ComparisonType getOperator() {
		return operator;
	}
	public void setOperator(ComparisonType operator) {
		this.operator = operator;
	}
	public NullOption getNullOption() {
		return nullOption;
	}
	public void setNullOption(NullOption nullOption) {
		this.nullOption = nullOption;
	}
	@Override
	public int getParameterSize(){
		return 1;
	}
	
	@Override
	public List<String> getParameterNames() {
		List<String> l = new ArrayList<String>();
		l.add(name);
		return l;
	}
	
	@Override
	public List<String> getMethodParameterNames() {
		List<String> l = new ArrayList<String>();
		l.add(StringUtils.toCamelCase(name)+operator.getOpName());
		return l;
	}
	
	@Override
	public void visit(QueryVisitor visitor) {
		if(isDynamic())
			visitor.visitCondition(name, operator, nullOption);
		else
			visitor.visitCondition(name, operator);
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
		if(nullOption== NullOption.NONE)
			return false;
		return true;
	}
	@Override
	public List<String> getMethodParameterProps() {
		List<String> l = new ArrayList<String>();
		l.add(name);
		return l;
	}

}
