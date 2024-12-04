package org.esfinge.querybuilder.methodparser.formater;

public class EndsParameterFormater implements ParameterFormater {

	@Override
	public Object formatParameter(Object param) {
		return "%"+param.toString();
	}

}
