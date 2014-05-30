package org.esfinge.querybuilder.mongodb.formaters;

import org.esfinge.querybuilder.methodparser.formater.ParameterFormater;

public class ContainsParameterFormater implements ParameterFormater {

	@Override
	public Object formatParameter(Object param) {
		return ".*"+param.toString()+".*";
	}

}
