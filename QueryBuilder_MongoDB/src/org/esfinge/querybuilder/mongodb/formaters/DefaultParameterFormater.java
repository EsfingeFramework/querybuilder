package org.esfinge.querybuilder.mongodb.formaters;

import net.sf.esfinge.querybuilder.methodparser.formater.ParameterFormater;

public class DefaultParameterFormater implements ParameterFormater {

	@Override
	public Object formatParameter(Object param) {
		return param;
	}

}
