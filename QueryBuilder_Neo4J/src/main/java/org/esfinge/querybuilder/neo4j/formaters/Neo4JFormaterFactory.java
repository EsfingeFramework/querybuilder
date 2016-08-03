package org.esfinge.querybuilder.neo4j.formaters;

import org.esfinge.querybuilder.annotation.ServicePriority;
import org.esfinge.querybuilder.methodparser.ComparisonType;
import org.esfinge.querybuilder.methodparser.formater.FormaterFactory;
import org.esfinge.querybuilder.methodparser.formater.ParameterFormater;

@ServicePriority(1)
public class Neo4JFormaterFactory implements FormaterFactory{

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
