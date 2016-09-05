package net.sf.esfinge.querybuilder.methodparser.formater;

import java.util.Formattable;

import net.sf.esfinge.querybuilder.methodparser.ComparisonType;

public class RelationalFormaterFactory implements FormaterFactory{

	@Override
	public ParameterFormater getFormater(ComparisonType operator) {
		switch(operator){
			case CONTAINS : return new ContainsParameterFormater();
			case STARTS : return new  StartsParameterFormater();
			case ENDS : return new  EndsParameterFormater();
			default : return new  DefaultParameterFormater();
		}
	}

}
