package org.esfinge.querybuilder.neo4j.formaters;

import org.esfinge.querybuilder.methodparser.formater.ParameterFormater;

public class DefaultParameterFormater implements ParameterFormater {

	@Override
	public Object formatParameter(Object param) {
		return param;
	}

}
