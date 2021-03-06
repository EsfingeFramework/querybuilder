package net.sf.esfinge.querybuilder.neo4j.formaters;

import net.sf.esfinge.querybuilder.annotation.ServicePriority;
import net.sf.esfinge.querybuilder.methodparser.ComparisonType;
import net.sf.esfinge.querybuilder.methodparser.formater.DefaultParameterFormater;
import net.sf.esfinge.querybuilder.methodparser.formater.FormaterFactory;
import net.sf.esfinge.querybuilder.methodparser.formater.ParameterFormater;

@ServicePriority(1)
public class Neo4JFormaterFactory implements FormaterFactory{

	@Override
	public ParameterFormater getFormater(ComparisonType operator) {
		switch(operator){
			default : return new DefaultParameterFormater();
		}
	}

}
