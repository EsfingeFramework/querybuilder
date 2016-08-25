package org.esfinge.querybuilder.mongodb.formaters;

import net.sf.esfinge.querybuilder.annotation.ServicePriority;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.formater.FormaterFactory;
import net.sf.esfinge.querybuilder.methodparser.formater.ParameterFormater;

@ServicePriority(1)
public class MongoDBFormaterFactory implements FormaterFactory{

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
