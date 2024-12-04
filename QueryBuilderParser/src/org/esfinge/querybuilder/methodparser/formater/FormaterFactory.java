package org.esfinge.querybuilder.methodparser.formater;

import org.esfinge.querybuilder.methodparser.ComparisonType;

public interface FormaterFactory {
	
	public ParameterFormater getFormater(ComparisonType operator);

}
