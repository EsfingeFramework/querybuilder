package net.sf.esfinge.querybuilder.mongodb.formaters;

import net.sf.esfinge.querybuilder.methodparser.formater.ParameterFormater;

public class EndsParameterFormater implements ParameterFormater {

	@Override
	public Object formatParameter(Object param) {
		return ".*"+param.toString();
	}

}
