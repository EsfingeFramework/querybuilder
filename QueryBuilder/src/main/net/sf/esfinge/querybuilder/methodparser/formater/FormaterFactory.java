package net.sf.esfinge.querybuilder.methodparser.formater;

import net.sf.esfinge.querybuilder.methodparser.ComparisonType;

public interface FormaterFactory {
	
	public ParameterFormater getFormater(ComparisonType operator);

}
